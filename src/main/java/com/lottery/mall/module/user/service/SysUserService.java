package com.lottery.mall.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.SecurityUtils;
import com.lottery.mall.module.user.domain.SysUser;
import com.lottery.mall.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户 Service
 */
@Service
@RequiredArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 根据用户名查询用户
     */
    public SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    /**
     * 根据手机号查询用户
     */
    public SysUser getByPhone(String phone) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
    }

    /**
     * 根据微信OpenID查询用户
     */
    public SysUser getByWxOpenid(String openid) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getWxOpenid, openid));
    }

    /**
     * 注册用户
     */
    public void register(SysUser user) {
        // 检查用户名是否存在
        if (getByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        // 加密密码
        String salt = SecurityUtils.uniqueId().substring(0, 8);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword(), salt));
        // 设置默认值
        user.setUserType("customer");
        user.setStatus(0);
        save(user);
    }

    /**
     * 验证用户名密码
     */
    public SysUser login(String username, String password) {
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND.getCode(), "用户名或密码错误");
        }
        if (user.getStatus() == 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        // TODO: 验证密码（需要获取盐值）
        return user;
    }
}
