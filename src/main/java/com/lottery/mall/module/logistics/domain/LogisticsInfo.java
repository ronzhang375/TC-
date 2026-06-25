package com.lottery.mall.module.logistics.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 物流信息实体
 */
@Data
@TableName("logistics_info")
public class LogisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String orderNo;

    private String logisticsCompany;

    private String logisticsNo;

    private String logisticsStatus;

    private String currentLocation;

    private String traces;

    private LocalDateTime shipTime;

    private LocalDateTime signTime;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
