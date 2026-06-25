package com.lottery.mall.module.marketing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.module.marketing.domain.BannerConfig;
import com.lottery.mall.module.marketing.mapper.BannerConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Banner Service
 */
@Service
@RequiredArgsConstructor
public class BannerService extends ServiceImpl<BannerConfigMapper, BannerConfig> {

    /**
     * 获取有效Banner列表
     */
    public List<BannerConfig> getActiveBanners(Long regionId) {
        LambdaQueryWrapper<BannerConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BannerConfig::getStatus, 1)
                .le(BannerConfig::getStartTime, LocalDateTime.now())
                .ge(BannerConfig::getEndTime, LocalDateTime.now())
                .orderByAsc(BannerConfig::getOrderNum);

        if (regionId != null) {
            // 优先查地区专属的，其次查全局的
            queryWrapper.and(w -> w.eq(BannerConfig::getRegionId, regionId).or().eq(BannerConfig::getRegionId, 0));
        }

        return list(queryWrapper);
    }

    /**
     * 获取所有Banner
     */
    public List<BannerConfig> getAll(Long regionId) {
        LambdaQueryWrapper<BannerConfig> queryWrapper = new LambdaQueryWrapper<>();
        if (regionId != null) {
            queryWrapper.eq(BannerConfig::getRegionId, regionId);
        }
        queryWrapper.orderByAsc(BannerConfig::getOrderNum);
        return list(queryWrapper);
    }
}
