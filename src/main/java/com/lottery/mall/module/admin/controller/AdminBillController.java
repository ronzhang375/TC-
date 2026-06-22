package com.lottery.mall.module.admin.controller;

import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.commission.domain.CommissionBill;
import com.lottery.mall.module.commission.domain.CommissionDetail;
import com.lottery.mall.module.commission.service.CommissionSettleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运营后台-佣金结算 Controller
 */
@Tag(name = "运营后台-佣金结算")
@RestController
@RequestMapping("/admin/bill")
@RequiredArgsConstructor
public class AdminBillController {

    private final CommissionSettleService commissionSettleService;

    @Operation(summary = "账单列表")
    @GetMapping("/list")
    public Result<PageResult<CommissionBill>> getList(
            PageRequest pageRequest,
            @RequestParam(required = false) Long channelId,
            @RequestParam(required = false) Integer status) {
        return Result.success(commissionSettleService.getBillList(pageRequest, channelId, status));
    }

    @Operation(summary = "账单详情")
    @GetMapping("/{id}")
    public Result<CommissionBill> getDetail(@PathVariable Long id) {
        return Result.success(commissionSettleService.getDetail(id));
    }

    @Operation(summary = "账单明细")
    @GetMapping("/{id}/details")
    public Result<List<CommissionDetail>> getBillDetails(@PathVariable Long id) {
        return Result.success(commissionSettleService.getBillDetails(id));
    }

    @Operation(summary = "确认账单")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        commissionSettleService.confirmBill(id);
        return Result.success();
    }

    @Operation(summary = "结算账单（打款）")
    @PutMapping("/{id}/settle")
    public Result<Void> settle(@PathVariable Long id) {
        commissionSettleService.settleBill(id);
        return Result.success();
    }

    @Operation(summary = "手动触发周结账单生成")
    @PostMapping("/generate/weekly")
    public Result<Void> generateWeekly() {
        commissionSettleService.generateWeeklyBills();
        return Result.success();
    }

    @Operation(summary = "手动触发月结账单生成")
    @PostMapping("/generate/monthly")
    public Result<Void> generateMonthly() {
        commissionSettleService.generateMonthlyBills();
        return Result.success();
    }
}