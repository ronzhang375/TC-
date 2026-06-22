package com.lottery.mall.module.user.controller;

import com.lottery.mall.common.result.Result;
import com.lottery.mall.module.user.domain.voWxLoginVo;
import com.lottery.mall.module.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * C端用户 Controller
 */
@Tag(name = "C端用户")
@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @Operation(summary = "微信登录")
    @PostMapping("/wx-login")
    public Result<WxLoginVo> wxLogin(@RequestBody WxLoginDTO dto) {
        return Result.success(appUserService.wxLogin(dto.getCode(), dto.getNickName()));
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(appUserService.getUserInfo());
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserInfoDTO dto) {
        appUserService.updateUserInfo(dto);
        return Result.success();
    }
}
