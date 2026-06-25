package com.lottery.mall.module.product.domain.vo;

import com.lottery.mall.module.product.domain.ProductSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情 VO
 */
@Data
@Schema(description = "商品详情")
public class ProductDetailVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "地区ID")
    private Long regionId;

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

    @Schema(description = "退款规则")
    private String refundRule;

    @Schema(description = "规格列表")
    private List<ProductSpec> specs;
}
