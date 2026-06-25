package com.lottery.mall.module.commission.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.channel.domain.ChannelInfo;
import com.lottery.mall.module.channel.service.ChannelService;
import com.lottery.mall.module.commission.domain.vo.ChannelOrderVO;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.mapper.OrderMainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 渠道商佣金 Service（供渠道商查询自己的订单和佣金）
 */
@Service
@RequiredArgsConstructor
public class ChannelCommissionService {

    private final ChannelService channelService;
    private final OrderMainMapper orderMainMapper;

    /**
     * 获取渠道商的订单列表
     */
    public PageResult<ChannelOrderVO> getChannelOrders(Long channelId, PageRequest pageRequest) {
        ChannelInfo channel = channelService.getById(channelId);
        if (channel == null) {
            throw new BusinessException(ResultCode.CHANNEL_NOT_FOUND);
        }

        Page<OrderMain> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getChannelId, channelId)
                .orderByDesc(OrderMain::getCreateTime);

        Page<OrderMain> result = orderMainMapper.selectPage(page, queryWrapper);
        List<ChannelOrderVO> voList = result.getRecords().stream()
                .map(this::toChannelOrderVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取渠道商的订单列表（按状态筛选）
     */
    public PageResult<ChannelOrderVO> getChannelOrdersByStatus(Long channelId, PageRequest pageRequest, Integer orderStatus) {
        ChannelInfo channel = channelService.getById(channelId);
        if (channel == null) {
            throw new BusinessException(ResultCode.CHANNEL_NOT_FOUND);
        }

        Page<OrderMain> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getChannelId, channelId);

        if (orderStatus != null) {
            queryWrapper.eq(OrderMain::getOrderStatus, orderStatus);
        }
        queryWrapper.orderByDesc(OrderMain::getCreateTime);

        Page<OrderMain> result = orderMainMapper.selectPage(page, queryWrapper);
        List<ChannelOrderVO> voList = result.getRecords().stream()
                .map(this::toChannelOrderVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取渠道商的佣金汇总
     */
    public ChannelCommissionSummary getCommissionSummary(Long channelId) {
        ChannelInfo channel = channelService.getById(channelId);
        if (channel == null) {
            throw new BusinessException(ResultCode.CHANNEL_NOT_FOUND);
        }

        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getChannelId, channelId)
                .eq(OrderMain::getOrderStatus, 4); // 已完成的订单

        List<OrderMain> completedOrders = orderMainMapper.selectList(queryWrapper);

        BigDecimal totalCommission = BigDecimal.ZERO;
        int orderCount = completedOrders.size();

        for (OrderMain order : completedOrders) {
            if (order.getCommissionAmount() != null) {
                totalCommission = totalCommission.add(order.getCommissionAmount());
            }
        }

        ChannelCommissionSummary summary = new ChannelCommissionSummary();
        summary.setChannelId(channelId);
        summary.setChannelName(channel.getChannelName());
        summary.setCommissionRate(channel.getCommissionRate());
        summary.setTotalOrderCount(orderCount);
        summary.setTotalCommission(totalCommission);

        return summary;
    }

    private ChannelOrderVO toChannelOrderVO(OrderMain order) {
        ChannelOrderVO vo = new ChannelOrderVO();
        vo.setOrderId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderAmount(order.getTotalAmount());
        vo.setCommissionRate(order.getCommissionAmount()
                .divide(order.getTotalAmount(), 4, java.math.RoundingMode.HALF_UP));
        vo.setCommissionAmount(order.getCommissionAmount());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setOrderStatusName(getStatusName(order.getOrderStatus()));
        vo.setCreateTime(order.getCreateTime());
        return vo;
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "待接单";
            case 2: return "待发货";
            case 3: return "配送中";
            case 4: return "已完成";
            case 5: return "已取消";
            case 6: return "已退款";
            default: return "未知";
        }
    }

    @lombok.Data
    public static class ChannelCommissionSummary {
        private Long channelId;
        private String channelName;
        private BigDecimal commissionRate;
        private Integer totalOrderCount;
        private BigDecimal totalCommission;
    }
}
