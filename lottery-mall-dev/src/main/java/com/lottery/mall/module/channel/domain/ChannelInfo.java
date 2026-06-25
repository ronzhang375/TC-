package com.lottery.mall.module.channel.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渠道商实体
 */
@Data
@TableName("channel_info")
public class ChannelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 渠道商ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 渠道商名称 */
    private String channelName;

    /** 渠道商编码 */
    private String channelCode;

    /** 渠道类型：store=门店 market=商场 community=社区 */
    private String channelType;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 所属地区ID */
    private Long regionId;

    /** 佣金比例 */
    private BigDecimal commissionRate;

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
