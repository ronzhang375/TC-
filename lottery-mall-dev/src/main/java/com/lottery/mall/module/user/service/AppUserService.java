package com.lottery.mall.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.JwtUtils;
import com.lottery.mall.module.user.domain.SysUser;
import com.lottery.mall.module.user.domain.vo.WxLoginVo;
import com.lottery.mall.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * C端用户 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService extends ServiceImpl<SysUserMapper, SysUser> {

    private final JwtUtils jwtUtils;

    /**
     * 微信登录
     */
    public WxLoginVo wxLogin(String code, String nickName) {
        // TODO: 调用微信接口获取 openid
        // 临时模拟：使用 code 作为 openid
        String openid = "wx_" + code;

        // 查询是否已存在用户
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getWxOpenid, openid));

        boolean isNew = false;
        if (user == null) {
            // 新用户自动注册
            user = new SysUser();
            user.setWxOpenid(openid);
            user.setNickName(nickName != null ? nickName : "用户" + System.currentTimeMillis() % 10000);
            user.setUserType("customer");
            user.setStatus(0);
            save(user);
            isNew = true;
            log.info("新用户注册: openid={}, nickName={}", openid, nickName);
        } else {
            // 更新昵称
            if (nickName != null && !nickName.isEmpty()) {
                user.setNickName(nickName);
                updateById(user);
            }
            log.info("用户登录: openid={}, userId={}", openid, user.getId());
        }

        // 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getUserType());

        return new WxLoginVo(token, user.getId(), isNew);
    }

    /**
     * 获取当前用户信息
     */
    public SysUser getCurrentUser(Long userId) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(Long userId, String nickName, String phone, String avatar) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (nickName != null) {
            user.setNickName(nickName);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        updateById(user);
    }
}
