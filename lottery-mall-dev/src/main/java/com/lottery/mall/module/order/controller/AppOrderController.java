package com.lottery.mall.module.order.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.domain.dto.CreateOrderDTO;
import com.lottery.mall.module.order.domain.vo.OrderDetailVO;
import com.lottery.mall.module.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * C端订单 Controller
 */
@Tag(name = "C端订单")
@RestController
@RequestMapping("/app/order")
@RequiredArgsConstructor
public class AppOrderController {

    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public Result<Map<String, Long>> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CreateOrderDTO dto) {
        OrderMain order = orderService.createOrder(userId, dto);
        return Result.success(Map.of("orderId", order.getId(), "orderNo", order.getOrderNo()));
    }

    @Operation(summary = "获取订单列表")
    @GetMapping("/list")
    public Result<PageResult<OrderMain>> getList(
            @RequestHeader("X-User-Id") Long userId,
            PageRequest pageRequest,
            @RequestParam(required = false) Integer status) {
        return Result.success(orderService.getUserOrderPage(userId, pageRequest, status));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getDetail(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        return Result.success(orderService.getDetail(userId, id));
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        orderService.cancel(userId, id);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @PutMapping("/{id}/receive")
    public Result<Void> confirmReceive(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        orderService.confirmReceive(userId, id);
        return Result.success();
    }

    @Operation(summary = "微信支付回调（内部调用）")
    @PostMapping("/pay/callback")
    public Result<Void> payCallback(
            @RequestParam Long orderId,
            @RequestParam String transactionId) {
        orderService.paySuccess(orderId, transactionId);
        return Result.success();
    }
}
