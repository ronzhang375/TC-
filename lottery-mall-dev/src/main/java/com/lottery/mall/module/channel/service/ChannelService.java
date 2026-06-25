package com.lottery.mall.module.channel.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.module.channel.domain.ChannelInfo;
import com.lottery.mall.module.channel.mapper.ChannelInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 渠道商 Service
 */
@Service
@RequiredArgsConstructor
public class ChannelService extends ServiceImpl<ChannelInfoMapper, ChannelInfo> {

    /**
     * 获取渠道商详情
     */
    public ChannelInfo getByCode(String channelCode) {
        return getOne(new LambdaQueryWrapper<ChannelInfo>()
                .eq(ChannelInfo::getChannelCode, channelCode)
                .eq(ChannelInfo::getStatus, 0));
    }

    /**
     * 获取地区的所有渠道商
     */
    public List<ChannelInfo> getByRegionId(Long regionId) {
        return list(new LambdaQueryWrapper<ChannelInfo>()
                .eq(ChannelInfo::getRegionId, regionId)
                .eq(ChannelInfo::getStatus, 0));
    }

    /**
     * 获取启用的渠道商
     */
    public List<ChannelInfo> getAllEnabled() {
        return list(new LambdaQueryWrapper<ChannelInfo>()
                .eq(ChannelInfo::getStatus, 0));
    }
}
