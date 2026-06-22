package com.lottery.mall.module.commission.controller;

import com.lottery.mall.common.annotation.LoginRequired;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.common.util.UserContext;
import com.lottery.mall.module.commission.domain.CommissionBill;
import com.lottery.mall.module.commission.domain.CommissionDetail;
import com.lottery.mall.module.commission.service.CommissionSettleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 渠道商账单 Controller
 */
@Tag(name = "渠道商账单")
@RestController
@RequestMapping("/channel/bill")
@RequiredArgsConstructor
public class ChannelBillController {

    private final CommissionSettleService commissionSettleService;

    @Operation(summary = "我的账单列表")
    @GetMapping("/list")
    @LoginRequired("channel")
    public Result<PageResult<CommissionBill>> getMyBills(PageRequest pageRequest) {
        Long channelId = UserContext.getUserId();
        return Result.success(commissionSettleService.getChannelBills(channelId, pageRequest));
    }

    @Operation(summary = "账单详情")
    @GetMapping("/{id}")
    @LoginRequired("channel")
    public Result<CommissionBill> getDetail(@PathVariable Long id) {
        return Result.success(commissionSettleService.getDetail(id));
    }

    @Operation(summary = "账单明细")
    @GetMapping("/{id}/details")
    @LoginRequired("channel")
    public Result<List<CommissionDetail>> getBillDetails(@PathVariable Long id) {
        return Result.success(commissionSettleService.getBillDetails(id));
    }
}