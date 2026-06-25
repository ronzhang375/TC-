package com.lottery.mall.module.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 创建订单 DTO
 */
@Data
@Schema(description = "创建订单参数")
public class CreateOrderDTO {

    @Schema(description = "收货地址ID")
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @Schema(description = "渠道ID（可选）")
    private Long channelId;

    @Schema(description = "地区ID")
    @NotNull(message = "地区不能为空")
    private Long regionId;

    @Schema(description = "配送方式：1同城快递 2门店自取")
    @NotNull(message = "配送方式不能为空")
    private Integer deliveryType;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "购物车项ID列表（合并下单时）")
    private List<Long> cartItemIds;

    @Schema(description = "直接购买时的商品信息")
    private DirectBuyItem directItem;

    @Data
    public static class DirectBuyItem {
        private Long productId;
        private Long specId;
        private Integer quantity;
    }
}
