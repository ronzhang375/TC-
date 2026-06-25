package com.lottery.mall.module.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.product.domain.ProductSpec;
import com.lottery.mall.module.product.mapper.ProductSpecMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品规格 Service
 */
@Service
@RequiredArgsConstructor
public class ProductSpecService extends ServiceImpl<ProductSpecMapper, ProductSpec> {

    /**
     * 获取商品的所有规格
     */
    public List<ProductSpec> getByProductId(Long productId) {
        return list(new LambdaQueryWrapper<ProductSpec>()
                .eq(ProductSpec::getProductId, productId)
                .eq(ProductSpec::getStatus, 1) // 只查启用的
                .orderByAsc(ProductSpec::getSpecSort));
    }

    /**
     * 获取规格详情
     */
    public ProductSpec getSpec(Long specId) {
        ProductSpec spec = getById(specId);
        if (spec == null) {
            throw new BusinessException(ResultCode.PRODUCT_SPEC_NOT_FOUND);
        }
        return spec;
    }

    /**
     * 新增规格
     */
    public void add(ProductSpec spec) {
        save(spec);
    }

    /**
     * 更新规格
     */
    public void update(ProductSpec spec) {
        updateById(spec);
    }

    /**
     * 删除规格
     */
    public void delete(Long id) {
        removeById(id);
    }

    /**
     * 批量保存规格
     */
    public void saveBatch(Long productId, List<ProductSpec> specs) {
        // 先删除原有规格
        remove(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getProductId, productId));
        // 保存新规格
        for (ProductSpec spec : specs) {
            spec.setProductId(productId);
        }
        saveBatch(specs);
    }

    /**
     * 扣减库存
     */
    public boolean reduceStock(Long specId, Integer quantity) {
        ProductSpec spec = getById(specId);
        if (spec == null) {
            throw new BusinessException(ResultCode.PRODUCT_SPEC_NOT_FOUND);
        }
        if (spec.getStock() < quantity) {
            throw new BusinessException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
        }
        spec.setStock(spec.getStock() - quantity);
        updateById(spec);
        return true;
    }

    /**
     * 回滚库存
     */
    public void rollbackStock(Long specId, Integer quantity) {
        ProductSpec spec = getById(specId);
        if (spec != null) {
            spec.setStock(spec.getStock() + quantity);
            updateById(spec);
        }
    }
}
