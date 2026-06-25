package com.lottery.mall.module.channel.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.channel.service.ChannelCommissionService;
import com.lottery.mall.module.commission.service.ChannelCommissionService.ChannelCommissionSummary;
import com.lottery.mall.module.commission.domain.vo.ChannelOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 渠道商 Controller（供渠道商查询自己的订单和佣金）
 */
@Tag(name = "渠道商")
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelCommissionService channelCommissionService;

    @Operation(summary = "渠道商登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        // TODO: 验证渠道商账号密码
        String channelCode = params.get("channelCode");
        // 临时实现：通过 channelCode 查询
        return Result.success(Map.of(
                "token", "channel_token_" + channelCode,
                "channelId", 1L,
                "channelName", "渠道商"
        ));
    }

    @Operation(summary = "获取我的订单列表")
    @GetMapping("/order/list")
    public Result<PageResult<ChannelOrderVO>> getMyOrders(
            @RequestHeader("X-Channel-Id") Long channelId,
            PageRequest pageRequest) {
        return Result.success(channelCommissionService.getChannelOrders(channelId, pageRequest));
    }

    @Operation(summary = "获取我的订单列表（按状态筛选）")
    @GetMapping("/order/listByStatus")
    public Result<PageResult<ChannelOrderVO>> getMyOrdersByStatus(
            @RequestHeader("X-Channel-Id") Long channelId,
            PageRequest pageRequest,
            @RequestParam(required = false) Integer orderStatus) {
        return Result.success(channelCommissionService.getChannelOrdersByStatus(channelId, pageRequest, orderStatus));
    }

    @Operation(summary = "获取我的佣金汇总")
    @GetMapping("/commission/summary")
    public Result<ChannelCommissionSummary> getMyCommissionSummary(
            @RequestHeader("X-Channel-Id") Long channelId) {
        return Result.success(channelCommissionService.getCommissionSummary(channelId));
    }
}
