package com.lottery.mall.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.mall.module.user.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
