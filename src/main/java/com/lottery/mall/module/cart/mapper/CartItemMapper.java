package com.lottery.mall.module.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.mall.module.cart.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车 Mapper
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}
