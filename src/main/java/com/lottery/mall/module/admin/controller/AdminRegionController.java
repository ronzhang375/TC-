package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.region.domain.RegionInfo;
import com.lottery.mall.module.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-地区管理 Controller
 */
@Tag(name = "运营后台-地区管理")
@RestController
@RequestMapping("/admin/region")
@RequiredArgsConstructor
public class AdminRegionController {

    private final RegionService regionService;

    @Operation(summary = "获取地区列表")
    @GetMapping("/list")
    public Result<List<RegionInfo>> getList() {
        return Result.success(regionService.getAllEnabled());
    }

    @Operation(summary = "新增地区")
    @PostMapping
    public Result<Long> add(@RequestBody RegionInfo region) {
        // TODO: 保存
        return Result.success(1L);
    }

    @Operation(summary = "更新地区")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody RegionInfo region) {
        region.setId(id);
        // TODO: 更新
        return Result.success();
    }

    @Operation(summary = "删除地区")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        // TODO: 删除
        return Result.success();
    }
}
