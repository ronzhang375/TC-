package com.lottery.mall.module.address.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.module.address.domain.AddressInfo;
import com.lottery.mall.module.address.mapper.AddressInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址 Service
 */
@Service
@RequiredArgsConstructor
public class AddressService extends ServiceImpl<AddressInfoMapper, AddressInfo> {

    /**
     * 获取用户的所有地址
     */
    public List<AddressInfo> getByUserId(Long userId) {
        return list(new LambdaQueryWrapper<AddressInfo>()
                .eq(AddressInfo::getUserId, userId)
                .orderByDesc(AddressInfo::getIsDefault)
                .orderByDesc(AddressInfo::getCreateTime));
    }

    /**
     * 获取默认地址
     */
    public AddressInfo getDefault(Long userId) {
        return getOne(new LambdaQueryWrapper<AddressInfo>()
                .eq(AddressInfo::getUserId, userId)
                .eq(AddressInfo::getIsDefault, 1));
    }

    /**
     * 新增收权地址
     */
    @Transactional
    public void add(AddressInfo address) {
        // 如果设置为默认，先取消其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getUserId());
        }
        save(address);
    }

    /**
     * 更新地址
     */
    @Transactional
    public void update(AddressInfo address) {
        AddressInfo exist = getById(address.getId());
        if (exist == null || !exist.getUserId().equals(address.getUserId())) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }
        // 如果设置为默认，先取消其他默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault(address.getUserId());
        }
        updateById(address);
    }

    /**
     * 删除地址
     */
    public void delete(Long userId, Long addressId) {
        AddressInfo address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }
        removeById(addressId);
    }

    /**
     * 设置默认地址
     */
    @Transactional
    public void setDefault(Long userId, Long addressId) {
        // 取消所有默认
        clearDefault(userId);
        // 设置新的默认
        AddressInfo address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }
        address.setIsDefault(1);
        updateById(address);
    }

    /**
     * 获取地址详情
     */
    public AddressInfo getDetail(Long userId, Long addressId) {
        AddressInfo address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }
        return address;
    }

    /**
     * 取消所有默认地址
     */
    private void clearDefault(Long userId) {
        List<AddressInfo> list = list(new LambdaQueryWrapper<AddressInfo>()
                .eq(AddressInfo::getUserId, userId)
                .eq(AddressInfo::getIsDefault, 1));
        for (AddressInfo address : list) {
            address.setIsDefault(0);
        }
        updateBatchById(list);
    }
}
