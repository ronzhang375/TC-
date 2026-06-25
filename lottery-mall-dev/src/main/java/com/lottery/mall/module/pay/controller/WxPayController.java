package com.lottery.mall.module.pay.controller;

import com.lottery.mall.common.annotation.LoginRequired;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.common.util.UserContext;
import com.lottery.mall.module.pay.domain.vo.WxPayParamsVO;
import com.lottery.mall.module.pay.service.WxPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 微信支付 Controller
 */
@Tag(name = "微信支付")
@RestController
@RequestMapping("/app/pay")
@RequiredArgsConstructor
public class WxPayController {

    private final WxPayService wxPayService;

    @Operation(summary = "获取支付参数")
    @PostMapping("/create")
    @LoginRequired("customer")
    public Result<WxPayParamsVO> createPayOrder(@RequestParam Long orderId) {
        Long userId = UserContext.getUserId();
        return Result.success(wxPayService.createPayOrder(userId, orderId));
    }

    @Operation(summary = "微信支付回调")
    @PostMapping("/callback")
    public Result<Void> payCallback(@RequestBody Map<String, String> params) {
        wxPayService.payCallback(params);
        return Result.success();
    }

    @Operation(summary = "申请退款")
    @PostMapping("/refund")
    @LoginRequired("customer")
    public Result<Void> refund(
            @RequestParam Long orderId,
            @RequestParam java.math.BigDecimal refundAmount,
            @RequestParam(required = false, defaultValue = "用户申请") String reason) {
        wxPayService.refund(orderId, refundAmount, reason);
        return Result.success();
    }
}