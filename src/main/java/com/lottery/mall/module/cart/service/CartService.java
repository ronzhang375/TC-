package com.lottery.mall.module.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.cart.domain.CartItem;
import com.lottery.mall.module.cart.domain.vo.CartItemVO;
import com.lottery.mall.module.cart.mapper.CartItemMapper;
import com.lottery.mall.module.product.domain.ProductInfo;
import com.lottery.mall.module.product.domain.ProductSpec;
import com.lottery.mall.module.product.service.ProductService;
import com.lottery.mall.module.product.service.ProductSpecService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车 Service
 */
@Service
@RequiredArgsConstructor
public class CartService extends ServiceImpl<CartItemMapper, CartItem> {

    private final ProductService productService;
    private final ProductSpecService productSpecService;

    /**
     * 获取购物车列表
     */
    public List<CartItemVO> getList(Long userId) {
        List<CartItem> items = list(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId));

        return items.stream().map(item -> {
            CartItemVO vo = new CartItemVO();
            vo.setCartId(item.getId());
            vo.setUserId(item.getUserId());
            vo.setProductId(item.getProductId());
            vo.setSpecId(item.getSpecId());
            vo.setQuantity(item.getQuantity());

            ProductSpec spec = productSpecService.getById(item.getSpecId());
            if (spec != null) {
                vo.setSpecName(spec.getSpecName());
                vo.setPrice(spec.getPrice());
                vo.setStock(spec.getStock());
                vo.setTotalAmount(spec.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            ProductInfo product = productService.getById(item.getProductId());
            if (product != null) {
                vo.setProductName(product.getProductName());
                String[] images = product.getImages() != null ? product.getImages().split(",") : new String[0];
                vo.setImage(images.length > 0 ? images[0] : "");
            }

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取购物车数量
     */
    public Integer getCount(Long userId) {
        return Math.toIntExact(count(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)));
    }

    /**
     * 加入购物车
     */
    @Transactional
    public void add(Long userId, Long productId, Long specId, Integer quantity) {
        // 检查商品和规格
        ProductInfo product = productService.getById(productId);
        if (product == null || product.getIsOnSale() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_OFF_SALE);
        }

        ProductSpec spec = productSpecService.getById(specId);
        if (spec == null || spec.getStatus() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_SPEC_NOT_FOUND);
        }
        if (spec.getStock() < quantity) {
            throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
        }

        // 检查是否已存在
        CartItem exist = getOne(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, productId)
                .eq(CartItem::getSpecId, specId));

        if (exist != null) {
            // 累加数量
            exist.setQuantity(exist.getQuantity() + quantity);
            updateById(exist);
        } else {
            // 新增
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setSpecId(specId);
            item.setQuantity(quantity);
            save(item);
        }
    }

    /**
     * 更新数量
     */
    @Transactional
    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        CartItem item = getById(cartId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ERROR, "购物车项不存在");
        }

        if (quantity <= 0) {
            removeById(cartId);
        } else {
            // 检查库存
            ProductSpec spec = productSpecService.getById(item.getSpecId());
            if (spec.getStock() < quantity) {
                throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
            }
            item.setQuantity(quantity);
            updateById(item);
        }
    }

    /**
     * 删除购物车项
     */
    public void delete(Long userId, Long cartId) {
        CartItem item = getById(cartId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ERROR, "购物车项不存在");
        }
        removeById(cartId);
    }

    /**
     * 清空购物车
     */
    public void clear(Long userId) {
        remove(new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId));
    }
}
