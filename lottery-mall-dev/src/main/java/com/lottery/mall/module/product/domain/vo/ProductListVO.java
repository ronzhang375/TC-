package com.lottery.mall.module.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品列表 VO
 */
@Data
@Schema(description = "商品列表")
public class ProductListVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "商品图片")
    private String[] images;

    @Schema(description = "最低价格")
    private BigDecimal minPrice;

    @Schema(description = "最高价格")
    private BigDecimal maxPrice;

    @Schema(description = "销量")
    private Integer salesCount;
}
