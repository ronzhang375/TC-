package com.lottery.mall.module.region.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.module.region.domain.RegionInfo;
import com.lottery.mall.module.region.mapper.RegionInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区 Service
 */
@Service
@RequiredArgsConstructor
public class RegionService extends ServiceImpl<RegionInfoMapper, RegionInfo> {

    /**
     * 获取所有启用的地区
     */
    public List<RegionInfo> getAllEnabled() {
        return list(new LambdaQueryWrapper<RegionInfo>()
                .eq(RegionInfo::getStatus, 0)
                .orderByAsc(RegionInfo::getOrderNum));
    }

    /**
     * 获取子地区
     */
    public List<RegionInfo> getChildren(Long parentId) {
        return list(new LambdaQueryWrapper<RegionInfo>()
                .eq(RegionInfo::getParentId, parentId)
                .eq(RegionInfo::getStatus, 0)
                .orderByAsc(RegionInfo::getOrderNum));
    }
}
