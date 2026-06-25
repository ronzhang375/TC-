package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.channel.domain.ChannelInfo;
import com.lottery.mall.module.channel.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-渠道商管理 Controller
 */
@Tag(name = "运营后台-渠道商管理")
@RestController
@RequestMapping("/admin/channel")
@RequiredArgsConstructor
public class AdminChannelController {

    private final ChannelService channelService;

    @Operation(summary = "获取渠道商列表")
    @GetMapping("/list")
    public Result<List<ChannelInfo>> getList(
            @RequestParam(required = false) Long regionId) {
        if (regionId != null) {
            return Result.success(channelService.getByRegionId(regionId));
        }
        return Result.success(channelService.getAllEnabled());
    }

    @Operation(summary = "获取渠道商详情")
    @GetMapping("/{id}")
    public Result<ChannelInfo> getDetail(@PathVariable Long id) {
        return Result.success(channelService.getById(id));
    }

    @Operation(summary = "新增渠道商")
    @PostMapping
    public Result<Long> add(@RequestBody ChannelInfo channel) {
        // TODO: 保存并返回ID
        return Result.success(1L);
    }

    @Operation(summary = "更新渠道商")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ChannelInfo channel) {
        channel.setId(id);
        // TODO: 更新
        return Result.success();
    }

    @Operation(summary = "启用/禁用渠道商")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        // TODO: 更新状态
        return Result.success();
    }

    @Operation(summary = "生成渠道二维码")
    @PostMapping("/{id}/qrcode")
    public Result<String> generateQrCode(@PathVariable Long id) {
        // TODO: 生成带渠道ID的二维码URL
        return Result.success("https://example.com/qrcode?channel=" + id);
    }
}
