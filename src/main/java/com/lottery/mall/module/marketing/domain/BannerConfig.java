package com.lottery.mall.module.marketing.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Banner配置实体
 */
@Data
@TableName("banner_config")
public class BannerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long regionId;

    private String bannerTitle;

    private String bannerImage;

    private String bannerLink;

    private String linkType;

    private Integer orderNum;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String remark;
}
