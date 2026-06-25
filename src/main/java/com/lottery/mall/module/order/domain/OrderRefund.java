package com.lottery.mall.module.order.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款申请实体
 */
@Data
@TableName("order_refund")
public class OrderRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String orderNo;

    private Long userId;

    private String refundNo;

    private BigDecimal refundAmount;

    private Integer refundType;

    private String refundReason;

    private String refundDesc;

    private Integer refundStatus;

    private LocalDateTime refundTime;

    private String rejectReason;

    private String auditBy;

    private LocalDateTime auditTime;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}