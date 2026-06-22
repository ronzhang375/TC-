package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.marketing.domain.BannerConfig;
import com.lottery.mall.module.marketing.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-Banner管理 Controller
 */
@Tag(name = "运营后台-Banner管理")
@RestController
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @Operation(summary = "获取Banner列表")
    @GetMapping("/list")
    public Result<List<BannerConfig>> getList(@RequestParam(required = false) Long regionId) {
        return Result.success(bannerService.getAll(regionId));
    }

    @Operation(summary = "新增Banner")
    @PostMapping
    public Result<Void> add(@RequestBody BannerConfig banner) {
        // TODO: 保存
        return Result.success();
    }

    @Operation(summary = "更新Banner")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody BannerConfig banner) {
        banner.setId(id);
        // TODO: 更新
        return Result.success();
    }

    @Operation(summary = "删除Banner")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        // TODO: 删除
        return Result.success();
    }

    @Operation(summary = "启用/禁用Banner")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        // TODO: 更新状态
        return Result.success();
    }
}
