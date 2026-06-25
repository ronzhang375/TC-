package com.lottery.mall.module.commission.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渠道商订单 VO（用于渠道商查看自己渠道的订单）
 */
@Data
@Schema(description = "渠道商订单")
public class ChannelOrderVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "商品信息")
    private String productInfo;

    @Schema(description = "订单金额")
    private BigDecimal orderAmount;

    @Schema(description = "佣金比例")
    private BigDecimal commissionRate;

    @Schema(description = "佣金金额")
    private BigDecimal commissionAmount;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "订单状态名称")
    private String orderStatusName;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;
}
