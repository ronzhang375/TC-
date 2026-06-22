package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.order.domain.OrderRefund;
import com.lottery.mall.module.order.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 运营后台-退款审核 Controller
 */
@Tag(name = "运营后台-退款审核")
@RestController
@RequestMapping("/admin/refund")
@RequiredArgsConstructor
public class AdminRefundController {

    private final RefundService refundService;

    @Operation(summary = "退款申请列表")
    @GetMapping("/list")
    public Result<PageResult<OrderRefund>> getList(
            PageRequest pageRequest,
            @RequestParam(required = false) Integer status) {
        return Result.success(refundService.getAllRefunds(pageRequest, status));
    }

    @Operation(summary = "退款详情")
    @GetMapping("/{id}")
    public Result<OrderRefund> getDetail(@PathVariable Long id) {
        return Result.success(refundService.getDetail(id));
    }

    @Operation(summary = "审核通过")
    @PutMapping("/{id}/approve")
    public Result<Void> approve(
            @PathVariable Long id,
            @RequestHeader("X-Admin-Name") String auditBy) {
        refundService.approve(id, auditBy);
        return Result.success();
    }

    @Operation(summary = "审核拒绝")
    @PutMapping("/{id}/reject")
    public Result<Void> reject(
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestHeader("X-Admin-Name") String auditBy) {
        refundService.reject(id, reason, auditBy);
        return Result.success();
    }

    @Operation(summary = "标记退款完成（微信回调后调用）")
    @PutMapping("/{id}/complete")
    public Result<Void> refundSuccess(@PathVariable Long id) {
        refundService.refundSuccess(id);
        return Result.success();
    }
}