package com.lottery.mall.module.product.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.product.domain.vo.ProductDetailVO;
import com.lottery.mall.module.product.domain.vo.ProductListVO;
import com.lottery.mall.module.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * C端商品 Controller
 */
@Tag(name = "C端商品")
@RestController
@RequestMapping("/app/product")
@RequiredArgsConstructor
public class AppProductController {

    private final ProductService productService;

    @Operation(summary = "分页查询商品列表")
    @GetMapping("/list")
    public Result<PageResult<ProductListVO>> getList(
            PageRequest pageRequest,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.getPageList(pageRequest, categoryId, regionId, keyword));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public Result<ProductDetailVO> getDetail(@PathVariable Long id) {
        return Result.success(productService.getDetail(id));
    }
}
