package com.lottery.mall.module.order.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单项 VO
 */
@Data
@Schema(description = "订单项")
public class OrderItemVO {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "规格ID")
    private Long specId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "商品图片")
    private String image;

    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "小计金额")
    private BigDecimal totalAmount;
}
