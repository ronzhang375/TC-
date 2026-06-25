package com.lottery.mall.module.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.product.domain.ProductInfo;
import com.lottery.mall.module.product.domain.ProductSpec;
import com.lottery.mall.module.product.domain.vo.ProductDetailVO;
import com.lottery.mall.module.product.domain.vo.ProductListVO;
import com.lottery.mall.module.product.mapper.ProductInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品 Service
 */
@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductInfoMapper, ProductInfo> {

    private final ProductSpecService productSpecService;

    /**
     * 分页查询商品列表
     */
    public PageResult<ProductListVO> getPageList(PageRequest request, Long categoryId, Long regionId, String keyword) {
        Page<ProductInfo> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<ProductInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductInfo::getIsOnSale, 1); // 只查上架的

        if (categoryId != null) {
            queryWrapper.eq(ProductInfo::getCategoryId, categoryId);
        }
        if (regionId != null) {
            queryWrapper.eq(ProductInfo::getRegionId, regionId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(ProductInfo::getProductName, keyword);
        }

        queryWrapper.orderByDesc(ProductInfo::getSalesCount, ProductInfo::getCreateTime);

        Page<ProductInfo> result = page(page, queryWrapper);

        List<ProductListVO> voList = result.getRecords().stream()
                .map(this::toListVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取商品详情
     */
    public ProductDetailVO getDetail(Long id) {
        ProductInfo product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        if (product.getIsOnSale() == 0) {
            throw new BusinessException(ResultCode.PRODUCT_OFF_SALE);
        }

        ProductDetailVO vo = toDetailVO(product);

        // 查询规格列表
        List<ProductSpec> specs = productSpecService.getByProductId(id);
        vo.setSpecs(specs);

        return vo;
    }

    /**
     * 新增商品
     */
    @Transactional
    public void add(ProductInfo product) {
        // 设置价格区间
        if (product.getMinPrice() == null) {
            product.setMinPrice(BigDecimal.ZERO);
        }
        if (product.getMaxPrice() == null) {
            product.setMaxPrice(product.getMinPrice());
        }
        save(product);
    }

    /**
     * 更新商品
     */
    @Transactional
    public void update(ProductInfo product) {
        ProductInfo exist = getById(product.getId());
        if (exist == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        updateById(product);
    }

    /**
     * 上架/下架
     */
    public void updateSaleStatus(Long id, Integer status) {
        ProductInfo product = getById(id);
        if (product == null) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        product.setIsOnSale(status);
        updateById(product);
    }

    private ProductListVO toListVO(ProductInfo product) {
        ProductListVO vo = new ProductListVO();
        vo.setId(product.getId());
        vo.setProductName(product.getProductName());
        vo.setDescription(product.getDescription());
        vo.setImages(product.getImages() != null ? product.getImages().split(",") : new String[0]);
        vo.setMinPrice(product.getMinPrice());
        vo.setMaxPrice(product.getMaxPrice());
        vo.setSalesCount(product.getSalesCount());
        return vo;
    }

    private ProductDetailVO toDetailVO(ProductInfo product) {
        ProductDetailVO vo = new ProductDetailVO();
        vo.setId(product.getId());
        vo.setProductName(product.getProductName());
        vo.setCategoryId(product.getCategoryId());
        vo.setRegionId(product.getRegionId());
        vo.setDescription(product.getDescription());
        vo.setImages(product.getImages() != null ? product.getImages().split(",") : new String[0]);
        vo.setMinPrice(product.getMinPrice());
        vo.setMaxPrice(product.getMaxPrice());
        vo.setSalesCount(product.getSalesCount());
        vo.setRefundRule(product.getRefundRule());
        return vo;
    }
}
