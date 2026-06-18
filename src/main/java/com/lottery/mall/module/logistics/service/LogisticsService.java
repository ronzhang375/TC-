package com.lottery.mall.module.logistics.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.module.logistics.domain.LogisticsInfo;
import com.lottery.mall.module.logistics.mapper.LogisticsInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 物流 Service
 */
@Service
@RequiredArgsConstructor
public class LogisticsService extends ServiceImpl<LogisticsInfoMapper, LogisticsInfo> {

    /**
     * 获取订单物流
     */
    public LogisticsInfo getByOrderId(Long orderId) {
        return getOne(new LambdaQueryWrapper<LogisticsInfo>()
                .eq(LogisticsInfo::getOrderId, orderId));
    }

    /**
     * 创建物流记录
     */
    public void create(Long orderId, String orderNo) {
        LogisticsInfo logistics = new LogisticsInfo();
        logistics.setOrderId(orderId);
        logistics.setOrderNo(orderNo);
        logistics.setLogisticsStatus("pending");
        logistics.setCurrentLocation("待发货");
        save(logistics);
    }

    /**
     * 发货
     */
    public void ship(Long orderId, String logisticsCompany, String logisticsNo) {
        LogisticsInfo logistics = getByOrderId(orderId);
        if (logistics != null) {
            logistics.setLogisticsCompany(logisticsCompany);
            logistics.setLogisticsNo(logisticsNo);
            logistics.setLogisticsStatus("shipping");
            logistics.setCurrentLocation("已发货，运输中");
            logistics.setShipTime(LocalDateTime.now());
            updateById(logistics);
        }
    }

    /**
     * 签收
     */
    public void sign(Long orderId) {
        LogisticsInfo logistics = getByOrderId(orderId);
        if (logistics != null) {
            logistics.setLogisticsStatus("signed");
            logistics.setCurrentLocation("已签收");
            logistics.setSignTime(LocalDateTime.now());
            updateById(logistics);
        }
    }

    /**
     * 更新物流轨迹
     */
    public void updateTraces(Long orderId, String traces) {
        LogisticsInfo logistics = getByOrderId(orderId);
        if (logistics != null) {
            logistics.setTraces(traces);
            updateById(logistics);
        }
    }
}
