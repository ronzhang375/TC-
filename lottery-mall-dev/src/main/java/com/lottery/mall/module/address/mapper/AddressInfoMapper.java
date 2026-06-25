package com.lottery.mall.module.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.mall.module.address.domain.AddressInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址 Mapper
 */
@Mapper
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {
}
