package com.lottery.mall.module.address.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.address.domain.AddressInfo;
import com.lottery.mall.module.address.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端收货地址 Controller
 */
@Tag(name = "C端收货地址")
@RestController
@RequestMapping("/app/address")
@RequiredArgsConstructor
public class AppAddressController {

    private final AddressService addressService;

    @Operation(summary = "获取地址列表")
    @GetMapping("/list")
    public Result<List<AddressInfo>> getList(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(addressService.getByUserId(userId));
    }

    @Operation(summary = "获取默认地址")
    @GetMapping("/default")
    public Result<AddressInfo> getDefault(@RequestHeader("X-User-Id") Long userId) {
        return Result.success(addressService.getDefault(userId));
    }

    @Operation(summary = "获取地址详情")
    @GetMapping("/{id}")
    public Result<AddressInfo> getDetail(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        return Result.success(addressService.getDetail(userId, id));
    }

    @Operation(summary = "新增地址")
    @PostMapping
    public Result<Void> add(@RequestBody AddressInfo address,
                            @RequestHeader("X-User-Id") Long userId) {
        address.setUserId(userId);
        addressService.add(address);
        return Result.success();
    }

    @Operation(summary = "更新地址")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody AddressInfo address,
                               @RequestHeader("X-User-Id") Long userId) {
        address.setId(id);
        address.setUserId(userId);
        addressService.update(address);
        return Result.success();
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id,
                               @RequestHeader("X-User-Id") Long userId) {
        addressService.delete(userId, id);
        return Result.success();
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id,
                                   @RequestHeader("X-User-Id") Long userId) {
        addressService.setDefault(userId, id);
        return Result.success();
    }
}
