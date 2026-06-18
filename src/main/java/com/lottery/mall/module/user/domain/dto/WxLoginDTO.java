package com.lottery.mall.module.user.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 微信登录 DTO
 */
@Data
@Schema(description = "微信登录参数")
public class WxLoginDTO {

    @Schema(description = "微信授权code")
    @NotBlank(message = "code不能为空")
    private String code;

    @Schema(description = "昵称（可选）")
    private String nickName;

    @Schema(description = "头像（可选）")
    private String avatar;
}
