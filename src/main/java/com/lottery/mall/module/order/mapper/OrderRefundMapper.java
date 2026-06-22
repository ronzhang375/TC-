package com.lottery.mall.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.mall.module.order.domain.OrderRefund;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款申请 Mapper
 */
@Mapper
public interface OrderRefundMapper extends BaseMapper<OrderRefund> {
}