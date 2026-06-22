package com.lottery.mall.module.supplier.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 供货商实体
 */
@Data
@TableName("supplier_info")
public class SupplierInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 供货商ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 供货商名称 */
    private String supplierName;

    /** 供货商编码 */
    private String supplierCode;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 地址 */
    private String address;

    /** 所属地区ID */
    private Long regionId;

    /** 配送方式：1同城快递 2门店自取 */
    private String deliveryTypes;

    /** 状态：0正常 1停用 */
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

    /** 删除标志：0正常 1删除 */
    @TableLogic
    private Integer delFlag;

    /** 备注 */
    private String remark;
}
