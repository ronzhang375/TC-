package com.lottery.mall.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.DateUtils;
import com.lottery.mall.module.order.domain.OrderItem;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.domain.OrderRefund;
import com.lottery.mall.module.order.domain.dto.RefundApplyDTO;
import com.lottery.mall.module.order.mapper.OrderItemMapper;
import com.lottery.mall.module.order.mapper.OrderMainMapper;
import com.lottery.mall.module.order.mapper.OrderRefundMapper;
import com.lottery.mall.module.product.service.ProductSpecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 退款 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService extends ServiceImpl<OrderRefundMapper, OrderRefund> {

    private final OrderMainMapper orderMainMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductSpecService productSpecService;

    /**
     * 申请退款
     */
    @Transactional
    public OrderRefund apply(Long userId, RefundApplyDTO dto) {
        // 1. 校验订单
        OrderMain order = orderMainMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getPayStatus() != 1) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单未支付或已退款");
        }
        // 只有已发货/配送中/已完成 才能申请退款
        if (order.getOrderStatus() < 2) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单未发货，无法申请退款");
        }

        // 2. 检查是否已存在未完成的退款申请
        OrderRefund exist = getOne(new LambdaQueryWrapper<OrderRefund>()
                .eq(OrderRefund::getOrderId, order.getId())
                .in(OrderRefund::getRefundStatus, 0, 1, 3)); // 待审核、审核通过、退款中
        if (exist != null) {
            throw new BusinessException(ResultCode.ERROR, "该订单已有退款申请进行中");
        }

        // 3. 创建退款申请
        OrderRefund refund = new OrderRefund();
        refund.setOrderId(order.getId());
        refund.setOrderNo(order.getOrderNo());
        refund.setUserId(userId);
        refund.setRefundNo(DateUtils.generateRefundNo());
        refund.setRefundAmount(dto.getRefundAmount() != null ? dto.getRefundAmount() : order.getPayAmount());
        refund.setRefundType(dto.getRefundType());
        refund.setRefundReason(dto.getRefundReason());
        refund.setRefundDesc(dto.getRefundDesc() != null ? dto.getRefundDesc() : "");
        refund.setRefundStatus(0); // 待审核
        save(refund);

        log.info("申请退款: userId={}, orderId={}, refundNo={}", userId, order.getId(), refund.getRefundNo());
        return refund;
    }

    /**
     * 审核通过
     */
    @Transactional
    public void approve(Long refundId, String auditBy) {
        OrderRefund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(ResultCode.ERROR, "退款申请不存在");
        }
        if (refund.getRefundStatus() != 0) {
            throw new BusinessException(ResultCode.ERROR, "退款状态异常");
        }

        refund.setRefundStatus(1); // 审核通过
        refund.setAuditBy(auditBy);
        refund.setAuditTime(java.time.LocalDateTime.now());
        updateById(refund);

        // TODO: 调用微信退款接口
        log.info("审核退款通过: refundId={}, auditBy={}", refundId, auditBy);
    }

    /**
     * 审核拒绝
     */
    public void reject(Long refundId, String rejectReason, String auditBy) {
        OrderRefund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(ResultCode.ERROR, "退款申请不存在");
        }
        if (refund.getRefundStatus() != 0) {
            throw new BusinessException(ResultCode.ERROR, "退款状态异常");
        }

        refund.setRefundStatus(2); // 审核拒绝
        refund.setRejectReason(rejectReason);
        refund.setAuditBy(auditBy);
        refund.setAuditTime(java.time.LocalDateTime.now());
        updateById(refund);

        log.info("审核退款拒绝: refundId={}, reason={}", refundId, rejectReason);
    }

    /**
     * 退款完成（微信回调成功后调用）
     */
    @Transactional
    public void refundSuccess(Long refundId) {
        OrderRefund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(ResultCode.ERROR, "退款申请不存在");
        }
        if (refund.getRefundStatus() == 4) {
            return; // 已完成，避免重复
        }

        refund.setRefundStatus(4); // 已完成
        refund.setRefundTime(java.time.LocalDateTime.now());
        updateById(refund);

        // 更新订单状态为已退款
        OrderMain order = orderMainMapper.selectById(refund.getOrderId());
        if (order != null) {
            order.setOrderStatus(6); // 已退款
            order.setPayStatus(2); // 已退款
            orderMainMapper.updateById(order);

            // 回滚库存
            java.util.List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
            for (OrderItem item : items) {
                productSpecService.rollbackStock(item.getSpecId(), item.getQuantity());
            }
        }

        log.info("退款完成: refundId={}, amount={}", refundId, refund.getRefundAmount());
    }

    /**
     * 用户查询自己的退款申请列表
     */
    public PageResult<OrderRefund> getUserRefunds(Long userId, PageRequest pageRequest) {
        Page<OrderRefund> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderRefund::getUserId, userId)
                .orderByDesc(OrderRefund::getCreateTime);
        Page<OrderRefund> result = page(page, queryWrapper);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 管理员查询所有退款申请
     */
    public PageResult<OrderRefund> getAllRefunds(PageRequest pageRequest, Integer status) {
        Page<OrderRefund> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(OrderRefund::getRefundStatus, status);
        }
        queryWrapper.orderByDesc(OrderRefund::getCreateTime);
        Page<OrderRefund> result = page(page, queryWrapper);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取退款详情
     */
    public OrderRefund getDetail(Long refundId) {
        OrderRefund refund = getById(refundId);
        if (refund == null) {
            throw new BusinessException(ResultCode.ERROR, "退款申请不存在");
        }
        return refund;
    }
}