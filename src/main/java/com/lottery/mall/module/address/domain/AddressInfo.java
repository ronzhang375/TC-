package com.lottery.mall.module.address.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址实体
 */
@Data
@TableName("address_info")
public class AddressInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地址ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 收货人姓名 */
    private String consigneeName;

    /** 收货人电话 */
    private String consigneePhone;

    /** 省份编码 */
    private String provinceCode;

    /** 省份名称 */
    private String provinceName;

    /** 城市编码 */
    private String cityCode;

    /** 城市名称 */
    private String cityName;

    /** 区县编码 */
    private String districtCode;

    /** 区县名称 */
    private String districtName;

    /** 详细地址 */
    private String detailAddress;

    /** 是否默认：0否 1是 */
    private Integer isDefault;

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
}
