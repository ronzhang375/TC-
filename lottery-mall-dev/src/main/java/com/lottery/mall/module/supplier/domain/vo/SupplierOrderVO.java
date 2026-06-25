package com.lottery.mall.module.supplier.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供货商订单 VO
 */
@Data
@Schema(description = "供货商订单")
public class SupplierOrderVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "收货人姓名")
    private String consigneeName;

    @Schema(description = "收货人电话")
    private String consigneePhone;

    @Schema(description = "收货地址")
    private String fullAddress;

    @Schema(description = "商品信息")
    private String productInfo;

    @Schema(description = "订单总额")
    private BigDecimal totalAmount;

    @Schema(description = "配送方式")
    private String deliveryTypeName;

    @Schema(description = "订单状态")
    private Integer orderStatus;

    @Schema(description = "订单状态名称")
    private String orderStatusName;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;
}
