package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.supplier.domain.SupplierInfo;
import com.lottery.mall.module.supplier.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-供货商管理 Controller
 */
@Tag(name = "运营后台-供货商管理")
@RestController
@RequestMapping("/admin/supplier")
@RequiredArgsConstructor
public class AdminSupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "获取供货商列表")
    @GetMapping("/list")
    public Result<List<SupplierInfo>> getList(@RequestParam(required = false) Long regionId) {
        // TODO: 按地区筛选
        return Result.success(supplierService.list());
    }

    @Operation(summary = "获取供货商详情")
    @GetMapping("/{id}")
    public Result<SupplierInfo> getDetail(@PathVariable Long id) {
        return Result.success(supplierService.getById(id));
    }

    @Operation(summary = "新增供货商")
    @PostMapping
    public Result<Long> add(@RequestBody SupplierInfo supplier) {
        // TODO: 保存并返回ID
        return Result.success(1L);
    }

    @Operation(summary = "更新供货商")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SupplierInfo supplier) {
        supplier.setId(id);
        // TODO: 更新
        return Result.success();
    }

    @Operation(summary = "启用/禁用供货商")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        // TODO: 更新状态
        return Result.success();
    }
}
