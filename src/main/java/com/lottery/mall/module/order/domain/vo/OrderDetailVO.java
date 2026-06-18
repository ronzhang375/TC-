package com.lottery.mall.module.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情 VO
 */
@Data
@Schema(description = "订单详情")
public class OrderDetailVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态：0待支付 1待接单 2待发货 3配送中 4已完成 5已取消 6已退款")
    private Integer orderStatus;

    @Schema(description = "订单状态名称")
    private String orderStatusName;

    @Schema(description = "支付状态：0未支付 1已支付 2已退款")
    private Integer payStatus;

    @Schema(description = "支付方式")
    private String payType;

    @Schema(description = "收货人姓名")
    private String consigneeName;

    @Schema(description = "收货人电话")
    private String consigneePhone;

    @Schema(description = "收货地址")
    private String fullAddress;

    @Schema(description = "配送方式")
    private String deliveryTypeName;

    @Schema(description = "商品总额")
    private BigDecimal totalAmount;

    @Schema(description = "配送费")
    private BigDecimal deliveryFee;

    @Schema(description = "实付金额")
    private BigDecimal payAmount;

    @Schema(description = "佣金金额")
    private BigDecimal commissionAmount;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "订单项列表")
    private List<OrderItemVO> items;
}
