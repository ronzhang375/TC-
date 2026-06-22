package com.lottery.mall.module.order.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体
 */
@Data
@TableName("order_main")
public class OrderMain implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单编号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 地区ID */
    private Long regionId;

    /** 渠道商ID */
    private Long channelId;

    /** 供货商ID */
    private Long supplierId;

    /** 收货地址ID */
    private Long addressId;

    /** 商品总额 */
    private BigDecimal totalAmount;

    /** 配送费 */
    private BigDecimal deliveryFee;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 佣金金额 */
    private BigDecimal commissionAmount;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 配送方式：1同城快递 2门店自取 */
    private Integer deliveryType;

    /** 订单状态：0待支付 1待接单 2待发货 3配送中 4已完成 5已取消 6已退款 */
    private Integer orderStatus;

    /** 支付状态：0未支付 1已支付 2已退款 */
    private Integer payStatus;

    /** 支付方式：wxpay=微信支付 */
    private String payType;

    /** 微信交易号 */
    private String transactionId;

    /** 订单备注 */
    private String remark;

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
