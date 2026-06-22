package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.domain.vo.OrderDetailVO;
import com.lottery.mall.module.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 运营后台-订单管理 Controller
 */
@Tag(name = "运营后台-订单管理")
@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "分页查询订单")
    @GetMapping("/list")
    public Result<PageResult<OrderMain>> getList(
            PageRequest pageRequest,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long channelId,
            @RequestParam(required = false) Integer status) {
        // TODO: 添加 regionId 和 channelId 筛选
        return Result.success(orderService.getUserOrderPage(null, pageRequest, status));
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getDetail(@PathVariable Long id) {
        // TODO: 需要管理员权限验证
        return Result.success(orderService.getDetail(null, id));
    }

    @Operation(summary = "关闭订单")
    @PutMapping("/{id}/close")
    public Result<Void> closeOrder(@PathVariable Long id) {
        // TODO: 管理员关闭订单
        return Result.success();
    }
}
