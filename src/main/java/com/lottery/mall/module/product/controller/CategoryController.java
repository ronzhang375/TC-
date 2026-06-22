package com.lottery.mall.module.product.controller;

import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.product.domain.Category;
import com.lottery.mall.module.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类 Controller
 */
@Tag(name = "商品分类")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "获取分类列表（树形）")
    @GetMapping("/tree")
    public Result<List<Category>> getTreeList() {
        return Result.success(categoryService.getTreeList());
    }

    @Operation(summary = "获取所有启用的分类")
    @GetMapping("/all")
    public Result<List<Category>> getAll() {
        return Result.success(categoryService.getAllEnabled());
    }

    @Operation(summary = "获取子分类")
    @GetMapping("/children/{parentId}")
    public Result<List<Category>> getChildren(@PathVariable Long parentId) {
        return Result.success(categoryService.getChildren(parentId));
    }
}
