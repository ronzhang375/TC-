package com.lottery.mall.module.region.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地区实体
 */
@Data
@TableName("region_info")
public class RegionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 地区ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 地区名称 */
    private String regionName;

    /** 地区编码 */
    private String regionCode;

    /** 父地区ID */
    private Long parentId;

    /** 排序 */
    private Integer orderNum;

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
