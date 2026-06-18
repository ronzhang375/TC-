package com.lottery.mall.module.supplier.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.supplier.domain.SupplierInfo;
import com.lottery.mall.module.supplier.domain.vo.SupplierOrderVO;
import com.lottery.mall.module.supplier.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 供货商 Controller
 */
@Tag(name = "供货商")
@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "供货商登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        SupplierInfo supplier = supplierService.login(username, password);
        String token = supplierService.generateToken(supplier);
        return Result.success(Map.of(
                "token", token,
                "supplierId", supplier.getId(),
                "supplierName", supplier.getSupplierName()
        ));
    }

    @Operation(summary = "获取待接单列表")
    @GetMapping("/order/pending")
    public Result<PageResult<SupplierOrderVO>> getPendingOrders(
            @RequestHeader("X-Supplier-Id") Long supplierId,
            PageRequest pageRequest) {
        return Result.success(supplierService.getPendingOrders(supplierId, pageRequest));
    }

    @Operation(summary = "获取所有订单")
    @GetMapping("/order/list")
    public Result<PageResult<SupplierOrderVO>> getAllOrders(
            @RequestHeader("X-Supplier-Id") Long supplierId,
            PageRequest pageRequest,
            @RequestParam(required = false) Integer status) {
        return Result.success(supplierService.getAllOrders(supplierId, pageRequest, status));
    }

    @Operation(summary = "接单")
    @PutMapping("/order/{orderId}/accept")
    public Result<Void> acceptOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-Supplier-Id") Long supplierId) {
        supplierService.acceptOrder(supplierId, orderId);
        return Result.success();
    }

    @Operation(summary = "发货")
    @PutMapping("/order/{orderId}/ship")
    public Result<Void> shipOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-Supplier-Id") Long supplierId,
            @RequestParam(required = false) String logisticsCompany,
            @RequestParam(required = false) String logisticsNo) {
        supplierService.shipOrder(supplierId, orderId, logisticsCompany, logisticsNo);
        return Result.success();
    }
}
