package com.lottery.mall.module.supplier.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.JwtUtils;
import com.lottery.mall.module.address.domain.AddressInfo;
import com.lottery.mall.module.address.service.AddressService;
import com.lottery.mall.module.logistics.service.LogisticsService;
import com.lottery.mall.module.order.domain.OrderItem;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.mapper.OrderItemMapper;
import com.lottery.mall.module.order.mapper.OrderMainMapper;
import com.lottery.mall.module.product.domain.ProductInfo;
import com.lottery.mall.module.product.service.ProductService;
import com.lottery.mall.module.supplier.domain.SupplierInfo;
import com.lottery.mall.module.supplier.domain.vo.SupplierOrderVO;
import com.lottery.mall.module.supplier.mapper.SupplierInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供货商 Service
 */
@Service
@RequiredArgsConstructor
public class SupplierService extends ServiceImpl<SupplierInfoMapper, SupplierInfo> {

    private final JwtUtils jwtUtils;
    private final OrderMainMapper orderMainMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddressService addressService;
    private final ProductService productService;
    private final LogisticsService logisticsService;

    /**
     * 供货商登录
     */
    public SupplierInfo login(String username, String password) {
        SupplierInfo supplier = getOne(new LambdaQueryWrapper<SupplierInfo>()
                .eq(SupplierInfo::getSupplierCode, username));
        if (supplier == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (supplier.getStatus() != 0) {
            throw new BusinessException(ResultCode.SUPPLIER_DISABLED);
        }
        // TODO: 验证密码
        return supplier;
    }

    /**
     * 生成供货商Token
     */
    public String generateToken(SupplierInfo supplier) {
        return jwtUtils.generateToken(supplier.getId(), "supplier");
    }

    /**
     * 获取待接单列表
     */
    public PageResult<SupplierOrderVO> getPendingOrders(Long supplierId, PageRequest pageRequest) {
        // 查找绑定到该供货商的地区下的待接单订单
        SupplierInfo supplier = getById(supplierId);
        if (supplier == null) {
            throw new BusinessException(ResultCode.SUPPLIER_NOT_FOUND);
        }

        Page<OrderMain> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getRegionId, supplier.getRegionId())
                .eq(OrderMain::getOrderStatus, 1) // 待接单
                .orderByDesc(OrderMain::getCreateTime);

        Page<OrderMain> result = orderMainMapper.selectPage(page, queryWrapper);
        List<SupplierOrderVO> voList = result.getRecords().stream()
                .map(this::toSupplierOrderVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取供货商所有订单
     */
    public PageResult<SupplierOrderVO> getAllOrders(Long supplierId, PageRequest pageRequest, Integer status) {
        SupplierInfo supplier = getById(supplierId);
        if (supplier == null) {
            throw new BusinessException(ResultCode.SUPPLIER_NOT_FOUND);
        }

        Page<OrderMain> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getRegionId, supplier.getRegionId())
                .ge(OrderMain::getOrderStatus, 1); // 已接单及之后

        if (status != null) {
            queryWrapper.eq(OrderMain::getOrderStatus, status);
        }
        queryWrapper.orderByDesc(OrderMain::getCreateTime);

        Page<OrderMain> result = orderMainMapper.selectPage(page, queryWrapper);
        List<SupplierOrderVO> voList = result.getRecords().stream()
                .map(this::toSupplierOrderVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 接单
     */
    @Transactional
    public void acceptOrder(Long supplierId, Long orderId) {
        SupplierInfo supplier = getById(supplierId);
        if (supplier == null) {
            throw new BusinessException(ResultCode.SUPPLIER_NOT_FOUND);
        }

        OrderMain order = orderMainMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (!order.getRegionId().equals(supplier.getRegionId())) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "无权操作此订单");
        }
        if (order.getOrderStatus() != 1) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单状态异常");
        }

        // 更新订单状态为待发货，绑定供货商
        order.setSupplierId(supplierId);
        order.setOrderStatus(2); // 待发货
        order.setUpdateTime(java.time.LocalDateTime.now());
        orderMainMapper.updateById(order);
    }

    /**
     * 发货
     */
    @Transactional
    public void shipOrder(Long supplierId, Long orderId, String logisticsCompany, String logisticsNo) {
        SupplierInfo supplier = getById(supplierId);
        if (supplier == null) {
            throw new BusinessException(ResultCode.SUPPLIER_NOT_FOUND);
        }

        OrderMain order = orderMainMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (!order.getSupplierId().equals(supplierId)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "无权操作此订单");
        }
        if (order.getOrderStatus() != 2) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单状态异常");
        }

        // 更新订单状态为配送中
        order.setOrderStatus(3); // 配送中
        order.setUpdateTime(java.time.LocalDateTime.now());
        orderMainMapper.updateById(order);

        // 创建物流信息
        logisticsService.create(orderId, order.getOrderNo());
        if (logisticsCompany != null && logisticsNo != null) {
            logisticsService.ship(orderId, logisticsCompany, logisticsNo);
        }
    }

    private SupplierOrderVO toSupplierOrderVO(OrderMain order) {
        SupplierOrderVO vo = new SupplierOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setOrderStatusName(getStatusName(order.getOrderStatus()));
        vo.setDeliveryTypeName(order.getDeliveryType() == 1 ? "同城快递" : "门店自取");
        vo.setCreateTime(order.getCreateTime());

        // 地址信息
        AddressInfo address = addressService.getById(order.getAddressId());
        if (address != null) {
            vo.setConsigneeName(address.getConsigneeName());
            vo.setConsigneePhone(address.getConsigneePhone());
            vo.setFullAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getDetailAddress());
        }

        // 商品信息
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        String productInfo = items.stream()
                .map(item -> item.getProductName() + " x" + item.getQuantity())
                .collect(Collectors.joining(", "));
        vo.setProductInfo(productInfo);

        return vo;
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "待接单";
            case 2: return "待发货";
            case 3: return "配送中";
            case 4: return "已完成";
            case 5: return "已取消";
            case 6: return "已退款";
            default: return "未知";
        }
    }
}
