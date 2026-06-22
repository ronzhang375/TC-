package com.lottery.mall.module.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品规格实体
 */
@Data
@TableName("product_spec")
public class ProductSpec implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 规格ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 规格名称（如：红色11朵+10元刮刮乐） */
    private String specName;

    /** 价格 */
    private BigDecimal price;

    /** 库存 */
    private Integer stock;

    /** 排序 */
    private Integer specSort;

    /** 状态：0禁用 1启用 */
    private Integer status;

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

    /** 备注 */
    private String remark;
}
