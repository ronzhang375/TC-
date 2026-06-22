package com.lottery.mall.module.pay.service;

import cn.hutool.core.util.RandomUtil;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.service.OrderService;
import com.lottery.mall.module.pay.domain.vo.WxPayParamsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信支付 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxPayService {

    private final OrderService orderService;

    @Value("${wx.pay.app-id:wx_appid_placeholder}")
    private String appId;

    @Value("${wx.pay.mch-id:mch_id_placeholder}")
    private String mchId;

    @Value("${wx.pay.api-key:api_key_placeholder}")
    private String apiKey;

    /**
     * 统一下单（获取支付参数）
     */
    public WxPayParamsVO createPayOrder(Long userId, Long orderId) {
        OrderMain order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getPayStatus() != 0) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单已支付");
        }

        // TODO: 调用微信统一下单API
        // https://api.mch.weixin.qq.com/pay/unifiedorder
        // 实际项目中需要使用 WXPay SDK 或自己实现

        // 返回支付参数（临时模拟数据）
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtil.randomString(32);
        String packageValue = "prepay_id=prepay_" + RandomUtil.randomString(32);

        // TODO: 生成真实签名（MD5 或 HMAC-SHA256）
        String paySign = "SIGN_" + RandomUtil.randomString(32);

        log.info("创建微信支付订单: orderId={}, payAmount={}", orderId, order.getPayAmount());

        return new WxPayParamsVO(timeStamp, nonceStr, packageValue, "MD5", paySign, order.getOrderNo());
    }

    /**
     * 支付回调
     */
    public void payCallback(Map<String, String> params) {
        // TODO: 验证签名
        // 1. 解密回调数据
        // 2. 验证微信签名
        // 3. 验证订单状态

        String orderNo = params.get("out_trade_no");
        String transactionId = params.get("transaction_id");

        log.info("微信支付回调: orderNo={}, transactionId={}", orderNo, transactionId);

        // 根据订单号查询订单并更新状态
        OrderMain order = orderService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderMain>()
                        .eq(OrderMain::getOrderNo, orderNo));

        if (order != null) {
            orderService.paySuccess(order.getId(), transactionId);
        }
    }

    /**
     * 申请退款
     */
    public void refund(Long orderId, BigDecimal refundAmount, String reason) {
        OrderMain order = orderService.getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }

        // TODO: 调用微信退款API
        // https://api.mch.weixin.qq.com/secapi/pay/refund

        log.info("申请退款: orderId={}, amount={}, reason={}", orderId, refundAmount, reason);
    }
}