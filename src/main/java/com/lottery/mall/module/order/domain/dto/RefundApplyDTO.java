package com.lottery.mall.module.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 退款申请 DTO
 */
@Data
@Schema(description = "退款申请参数")
public class RefundApplyDTO {

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "退款类型：1仅退款 2退货退款")
    @NotNull(message = "退款类型不能为空")
    private Integer refundType;

    @Schema(description = "退款原因")
    @NotBlank(message = "退款原因不能为空")
    private String refundReason;

    @Schema(description = "退款说明")
    private String refundDesc;

    @Schema(description = "退款金额（默认全额）")
    private BigDecimal refundAmount;
}