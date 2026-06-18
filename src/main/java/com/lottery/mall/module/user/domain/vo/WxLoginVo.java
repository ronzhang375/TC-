package com.lottery.mall.module.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信登录响应 VO
 */
@Data
@AllArgsConstructor
@Schema(description = "微信登录响应")
public class WxLoginVo {

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "是否新用户")
    private Boolean isNew;
}
