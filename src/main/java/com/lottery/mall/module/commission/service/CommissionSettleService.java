package com.lottery.mall.module.commission.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.DateUtils;
import com.lottery.mall.module.channel.domain.ChannelInfo;
import com.lottery.mall.module.channel.service.ChannelService;
import com.lottery.mall.module.commission.domain.CommissionBill;
import com.lottery.mall.module.commission.domain.CommissionDetail;
import com.lottery.mall.module.commission.mapper.CommissionBillMapper;
import com.lottery.mall.module.commission.mapper.CommissionDetailMapper;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.mapper.OrderMainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 佣金结算 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommissionSettleService extends ServiceImpl<CommissionBillMapper, CommissionBill> {

    private final CommissionDetailMapper commissionDetailMapper;
    private final ChannelService channelService;
    private final OrderMainMapper orderMainMapper;

    /**
     * 生成周结账单（每周一执行）
     */
    @Transactional
    public void generateWeeklyBills() {
        LocalDate lastMonday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate lastSunday = lastMonday.plusDays(6);
        generateBillsForPeriod(lastMonday, lastSunday, "week");
    }

    /**
     * 生成月结账单（每月1号执行）
     */
    @Transactional
    public void generateMonthlyBills() {
        LocalDate lastMonthFirst = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = lastMonthFirst.with(TemporalAdjusters.lastDayOfMonth());
        generateBillsForPeriod(lastMonthFirst, lastMonthEnd, "month");
    }

    /**
     * 生成指定周期的账单
     */
    @Transactional
    public void generateBillsForPeriod(LocalDate startDate, LocalDate endDate, String settlementType) {
        log.info("开始生成{}账单: {} ~ {}", settlementType, startDate, endDate);

        // 获取所有启用的渠道商
        List<ChannelInfo> channels = channelService.getAllEnabled();

        int count = 0;
        for (ChannelInfo channel : channels) {
            // 查询该渠道商在指定周期内已完成的订单
            List<OrderMain> orders = orderMainMapper.selectList(
                    new LambdaQueryWrapper<OrderMain>()
                            .eq(OrderMain::getChannelId, channel.getId())
                            .eq(OrderMain::getOrderStatus, 4) // 已完成
                            .ge(OrderMain::getCreateTime, startDate.atStartOfDay())
                            .le(OrderMain::getCreateTime, endDate.atTime(23, 59, 59))
            );

            if (orders.isEmpty()) {
                continue;
            }

            // 计算总佣金
            BigDecimal totalCommission = BigDecimal.ZERO;
            List<CommissionDetail> details = new ArrayList<>();

            for (OrderMain order : orders) {
                BigDecimal commission = order.getCommissionAmount() != null ? order.getCommissionAmount() : BigDecimal.ZERO;
                totalCommission = totalCommission.add(commission);

                CommissionDetail detail = new CommissionDetail();
                detail.setOrderId(order.getId());
                detail.setOrderNo(order.getOrderNo());
                detail.setChannelId(channel.getId());
                detail.setChannelName(channel.getChannelName());
                detail.setOrderAmount(order.getPayAmount());
                detail.setCommissionRate(channel.getCommissionRate());
                detail.setCommissionAmount(commission);
                detail.setSettlementType(settlementType);
                details.add(detail);
            }

            // 创建账单
            CommissionBill bill = new CommissionBill();
            bill.setBillNo(DateUtils.generateBillNo(settlementType));
            bill.setChannelId(channel.getId());
            bill.setChannelName(channel.getChannelName());
            bill.setSettlementType(settlementType);
            bill.setPeriodStart(startDate);
            bill.setPeriodEnd(endDate);
            bill.setOrderCount(orders.size());
            bill.setOrderAmount(orders.stream().map(OrderMain::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            bill.setCommissionAmount(totalCommission);
            bill.setBillStatus(0); // 待确认
            save(bill);

            // 保存明细
            for (CommissionDetail detail : details) {
                detail.setBillId(bill.getId());
            }
            commissionDetailMapper.insertBatch(details);

            count++;
            log.info("生成账单: channel={}, billNo={}, amount={}", channel.getChannelName(), bill.getBillNo(), totalCommission);
        }

        log.info("账单生成完成: 共生成 {} 个账单", count);
    }

    /**
     * 查询账单列表（运营后台）
     */
    public PageResult<CommissionBill> getBillList(PageRequest pageRequest, Long channelId, Integer status) {
        Page<CommissionBill> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<CommissionBill> queryWrapper = new LambdaQueryWrapper<>();
        if (channelId != null) {
            queryWrapper.eq(CommissionBill::getChannelId, channelId);
        }
        if (status != null) {
            queryWrapper.eq(CommissionBill::getBillStatus, status);
        }
        queryWrapper.orderByDesc(CommissionBill::getCreateTime);
        Page<CommissionBill> result = page(page, queryWrapper);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 渠道商查询自己的账单
     */
    public PageResult<CommissionBill> getChannelBills(Long channelId, PageRequest pageRequest) {
        Page<CommissionBill> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<CommissionBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionBill::getChannelId, channelId)
                .orderByDesc(CommissionBill::getCreateTime);
        Page<CommissionBill> result = page(page, queryWrapper);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取账单详情
     */
    public CommissionBill getDetail(Long billId) {
        CommissionBill bill = getById(billId);
        if (bill == null) {
            throw new BusinessException(ResultCode.ERROR, "账单不存在");
        }
        return bill;
    }

    /**
     * 获取账单明细
     */
    public List<CommissionDetail> getBillDetails(Long billId) {
        return commissionDetailMapper.selectList(
                new LambdaQueryWrapper<CommissionDetail>()
                        .eq(CommissionDetail::getBillId, billId));
    }

    /**
     * 确认账单
     */
    @Transactional
    public void confirmBill(Long billId) {
        CommissionBill bill = getById(billId);
        if (bill == null) {
            throw new BusinessException(ResultCode.ERROR, "账单不存在");
        }
        if (bill.getBillStatus() != 0) {
            throw new BusinessException(ResultCode.ERROR, "账单状态异常");
        }
        bill.setBillStatus(1); // 待结算
        bill.setConfirmTime(java.time.LocalDateTime.now());
        updateById(bill);
    }

    /**
     * 结算账单（标记已打款）
     */
    @Transactional
    public void settleBill(Long billId) {
        CommissionBill bill = getById(billId);
        if (bill == null) {
            throw new BusinessException(ResultCode.ERROR, "账单不存在");
        }
        if (bill.getBillStatus() != 1) {
            throw new BusinessException(ResultCode.ERROR, "账单状态异常");
        }
        bill.setBillStatus(3); // 已打款
        bill.setSettleTime(java.time.LocalDateTime.now());
        bill.setPayTime(java.time.LocalDateTime.now());
        updateById(bill);

        // TODO: 调用支付接口打款给渠道商
        log.info("结算账单: billId={}, amount={}", billId, bill.getCommissionAmount());
    }
}