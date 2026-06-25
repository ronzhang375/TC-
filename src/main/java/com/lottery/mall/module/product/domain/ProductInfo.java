package com.lottery.mall.module.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息实体
 */
@Data
@TableName("product_info")
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品名称 */
    private String productName;

    /** 分类ID */
    private Long categoryId;

    /** 所属地区ID */
    private Long regionId;

    /** 商品描述 */
    private String description;

    /** 商品图片，多个用逗号分隔 */
    private String images;

    /** 最低价格 */
    private BigDecimal minPrice;

    /** 最高价格 */
    private BigDecimal maxPrice;

    /** 销量 */
    private Integer salesCount;

    /** 上架状态：0下架 1上架 */
    private Integer isOnSale;

    /** 退款规则：all=全额退款 partial=不退 */
    private String refundRule;

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

    /** 删除标志：0正常 1删除 */
    @TableLogic
    private Integer delFlag;

    /** 备注 */
    private String remark;
}
