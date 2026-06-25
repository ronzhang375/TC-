package com.lottery.mall.module.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信支付参数 VO
 */
@Data
@AllArgsConstructor
@Schema(description = "微信支付参数")
public class WxPayParamsVO {

    @Schema(description = "时间戳")
    private String timeStamp;

    @Schema(description = "随机字符串")
    private String nonceStr;

    @Schema(description = "预支付交易会话标识")
    @com.fasterxml.jackson.annotation.JsonProperty("package")
    private String packageValue;

    @Schema(description = "签名方式")
    private String signType;

    @Schema(description = "签名")
    private String paySign;

    @Schema(description = "订单号")
    private String orderNo;
}