package com.lottery.mall.module.cart.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.cart.domain.vo.CartItemVO;
import com.lottery.mall.module.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端购物车 Controller
 */
@Tag(name = "C端购物车")
@RestController
@RequestMapping("/app/cart")
@RequiredArgsConstructor
public class AppCartController {

    private final CartService cartService;

    @Operation(summary = "获取购物车列表")
    @GetMapping("/list")
    public Result<List<CartItemVO>> getList(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(cartService.getList(userId));
    }

    @Operation(summary = "获取购物车数量")
    @GetMapping("/count")
    public Result<Integer> getCount(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(cartService.getCount(userId));
    }

    @Operation(summary = "加入购物车")
    @PostMapping("/add")
    public Result<Void> add(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long productId,
            @RequestParam Long specId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        cartService.add(userId, productId, specId, quantity);
        return Result.success();
    }

    @Operation(summary = "更新数量")
    @PutMapping("/quantity/{cartId}")
    public Result<Void> updateQuantity(
            @PathVariable Long cartId,
            @RequestParam Integer quantity,
            @RequestHeader("X-User-Id") Long userId) {
        cartService.updateQuantity(userId, cartId, quantity);
        return Result.success();
    }

    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{cartId}")
    public Result<Void> delete(
            @PathVariable Long cartId,
            @RequestHeader("X-User-Id") Long userId) {
        cartService.delete(userId, cartId);
        return Result.success();
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear")
    public Result<Void> clear(@RequestHeader("X-User-Id") Long userId) {
        cartService.clear(userId);
        return Result.success();
    }
}
