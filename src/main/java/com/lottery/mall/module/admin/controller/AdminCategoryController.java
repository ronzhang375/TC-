package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.product.domain.Category;
import com.lottery.mall.module.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-分类管理 Controller
 */
@Tag(name = "运营后台-分类管理")
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "获取分类列表（树形）")
    @GetMapping("/tree")
    public Result<List<Category>> getTree() {
        return Result.success(categoryService.getTreeList());
    }

    @Operation(summary = "获取所有分类")
    @GetMapping("/all")
    public Result<List<Category>> getAll() {
        return Result.success(categoryService.getAllEnabled());
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Void> add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}
