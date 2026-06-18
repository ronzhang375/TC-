package com.lottery.mall.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    ERROR(400, "操作失败"),
    UNAUTHORIZED(401, "未授权，请登录"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    // 服务端错误 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // 业务错误 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    USER_TOKEN_EXPIRED(1004, "token已过期"),
    USER_TOKEN_INVALID(1005, "token无效"),

    // 商品模块 2xxx
    PRODUCT_NOT_FOUND(2001, "商品不存在"),
    PRODUCT_OFF_SALE(2002, "商品已下架"),
    PRODUCT_STOCK_NOT_ENOUGH(2003, "商品库存不足"),
    PRODUCT_SPEC_NOT_FOUND(2004, "商品规格不存在"),

    // 订单模块 3xxx
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态异常"),
    ORDER_PRICE_CHANGED(3003, "商品价格已变动"),
    ORDER_PAY_TIMEOUT(3004, "订单支付超时"),
    ORDER_REFUND_ERROR(3005, "退款申请异常"),

    // 收货地址模块 4xxx
    ADDRESS_NOT_FOUND(4001, "收货地址不存在"),
    ADDRESS_DEFAULT_ERROR(4002, "默认地址设置失败"),

    // 支付模块 5xxx
    PAY_PARAM_ERROR(5001, "支付参数错误"),
    PAY_FAILED(5002, "支付失败"),
    PAY_CANCEL(5003, "用户取消支付"),
    PAY_REFUND_FAILED(5004, "退款失败"),

    // 渠道商模块 6xxx
    CHANNEL_NOT_FOUND(6001, "渠道商不存在"),
    CHANNEL_DISABLED(6002, "渠道商已被禁用"),

    // 供货商模块 7xxx
    SUPPLIER_NOT_FOUND(7001, "供货商不存在"),
    SUPPLIER_DISABLED(7002, "供货商已被禁用"),

    // 佣金模块 8xxx
    COMMISSION_CALCULATE_ERROR(8001, "佣金计算异常"),
    COMMISSION_SETTLEMENT_ERROR(8002, "佣金结算异常"),

    // 参数校验 9xxx
    PARAM_NOT_NULL(9001, "参数不能为空"),
    PARAM_FORMAT_ERROR(9002, "参数格式错误"),
    PARAM_RANGE_ERROR(9003, "参数超出范围");

    private final Integer code;
    private final String msg;
}
