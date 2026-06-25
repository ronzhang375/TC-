package com.lottery.mall.module.commission.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 佣金明细实体
 */
@Data
@TableName("commission_detail")
public class CommissionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long billId;

    private Long orderId;

    private String orderNo;

    private Long channelId;

    private String channelName;

    private BigDecimal orderAmount;

    private BigDecimal commissionRate;

    private BigDecimal commissionAmount;

    private String settlementType;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}