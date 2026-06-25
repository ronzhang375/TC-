package com.lottery.mall.module.commission.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 佣金账单实体
 */
@Data
@TableName("commission_bill")
public class CommissionBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 账单ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 账单编号 */
    private String billNo;

    /** 渠道商ID */
    private Long channelId;

    /** 渠道商名称 */
    private String channelName;

    /** 结算周期：week=周结 month=月结 */
    private String settlementType;

    /** 结算周期开始日期 */
    private LocalDate periodStart;

    /** 结算周期结束日期 */
    private LocalDate periodEnd;

    /** 订单数量 */
    private Integer orderCount;

    /** 订单总金额 */
    private BigDecimal orderAmount;

    /** 佣金金额 */
    private BigDecimal commissionAmount;

    /** 账单状态：0待确认 1待结算 2已结算 3已打款 */
    private Integer billStatus;

    /** 确认时间 */
    private LocalDateTime confirmTime;

    /** 结算时间 */
    private LocalDateTime settleTime;

    /** 打款时间 */
    private LocalDateTime payTime;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
