package com.lottery.mall.module.order.controller;

import com.lottery.mall.common.annotation.LoginRequired;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.Result;
import com.lottery.mall.common.util.UserContext;
import com.lottery.mall.module.order.domain.OrderRefund;
import com.lottery.mall.module.order.domain.dto.RefundApplyDTO;
import com.lottery.mall.module.order.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 退款 Controller
 */
@Tag(name = "退款管理")
@RestController
@RequestMapping("/app/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @Operation(summary = "申请退款")
    @PostMapping("/apply")
    @LoginRequired("customer")
    public Result<OrderRefund> apply(@RequestBody @Valid RefundApplyDTO dto) {
        Long userId = UserContext.getUserId();
        return Result.success(refundService.apply(userId, dto));
    }

    @Operation(summary = "我的退款申请列表")
    @GetMapping("/list")
    @LoginRequired("customer")
    public Result<PageResult<OrderRefund>> getMyRefunds(PageRequest pageRequest) {
        Long userId = UserContext.getUserId();
        return Result.success(refundService.getUserRefunds(userId, pageRequest));
    }

    @Operation(summary = "退款详情")
    @GetMapping("/{id}")
    @LoginRequired("customer")
    public Result<OrderRefund> getDetail(@PathVariable Long id) {
        return Result.success(refundService.getDetail(id));
    }
}