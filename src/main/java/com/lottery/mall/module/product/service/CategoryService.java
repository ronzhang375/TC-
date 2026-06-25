package com.lottery.mall.module.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.product.domain.Category;
import com.lottery.mall.module.product.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类 Service
 */
@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    /**
     * 获取分类列表（树形）
     */
    public List<Category> getTreeList() {
        List<Category> all = list(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 0)
                .orderByAsc(Category::getOrderNum));
        return buildTree(all);
    }

    /**
     * 获取子分类
     */
    public List<Category> getChildren(Long parentId) {
        return list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId)
                .eq(Category::getStatus, 0)
                .orderByAsc(Category::getOrderNum));
    }

    /**
     * 获取所有启用的分类
     */
    public List<Category> getAllEnabled() {
        return list(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 0)
                .orderByAsc(Category::getOrderNum));
    }

    /**
     * 新增分类
     */
    public void add(Category category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        save(category);
    }

    /**
     * 更新分类
     */
    public void update(Category category) {
        updateById(category);
    }

    /**
     * 删除分类
     */
    public void delete(Long id) {
        // 检查是否有子分类
        long count = count(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id));
        if (count > 0) {
            throw new BusinessException(ResultCode.ERROR, "存在子分类，无法删除");
        }
        removeById(id);
    }

    /**
     * 构建树形结构
     */
    private List<Category> buildTree(List<Category> list) {
        List<Category> tree = new java.util.ArrayList<>();
        for (Category category : list) {
            if (category.getParentId() == 0 || category.getParentId() == null) {
                tree.add(category);
                buildChildren(category, list);
            }
        }
        return tree;
    }

    private void buildChildren(Category parent, List<Category> all) {
        for (Category category : all) {
            if (category.getParentId() != null && category.getParentId().equals(parent.getId())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new java.util.ArrayList<>());
                }
                parent.getChildren().add(category);
                buildChildren(category, all);
            }
        }
    }
}
