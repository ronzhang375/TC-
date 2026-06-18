package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.product.domain.ProductInfo;
import com.lottery.mall.module.product.domain.ProductSpec;
import com.lottery.mall.module.product.service.ProductService;
import com.lottery.mall.module.product.service.ProductSpecService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 运营后台-商品管理 Controller
 */
@Tag(name = "运营后台-商品管理")
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductSpecService productSpecService;

    @Operation(summary = "分页查询商品")
    @GetMapping("/list")
    public Result<PageResult<ProductInfo>> getList(
            PageRequest pageRequest,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.getPageList(pageRequest, categoryId, regionId, keyword));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public Result<ProductInfo> getDetail(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public Result<Void> add(@RequestBody ProductInfo product) {
        productService.add(product);
        return Result.success();
    }

    @Operation(summary = "更新商品")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ProductInfo product) {
        product.setId(id);
        productService.update(product);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "上架/下架商品")
    @PutMapping("/{id}/sale")
    public Result<Void> updateSaleStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        productService.updateSaleStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "获取商品规格")
    @GetMapping("/{id}/specs")
    public Result<List<ProductSpec>> getSpecs(@PathVariable Long id) {
        return Result.success(productSpecService.getByProductId(id));
    }

    @Operation(summary = "保存商品规格")
    @PostMapping("/{id}/specs")
    public Result<Void> saveSpecs(
            @PathVariable Long id,
            @RequestBody List<ProductSpec> specs) {
        productSpecService.saveBatch(id, specs);
        return Result.success();
    }

    @Operation(summary = "新增规格")
    @PostMapping("/spec")
    public Result<Void> addSpec(@RequestBody ProductSpec spec) {
        productSpecService.add(spec);
        return Result.success();
    }

    @Operation(summary = "更新规格")
    @PutMapping("/spec/{id}")
    public Result<Void> updateSpec(@PathVariable Long id, @RequestBody ProductSpec spec) {
        spec.setId(id);
        productSpecService.update(spec);
        return Result.success();
    }

    @Operation(summary = "删除规格")
    @DeleteMapping("/spec/{id}")
    public Result<Void> deleteSpec(@PathVariable Long id) {
        productSpecService.delete(id);
        return Result.success();
    }
}
