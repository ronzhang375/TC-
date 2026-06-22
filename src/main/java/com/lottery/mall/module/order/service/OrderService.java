package com.lottery.mall.module.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lottery.mall.common.core.PageRequest;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.PageResult;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.DateUtils;
import com.lottery.mall.module.address.domain.AddressInfo;
import com.lottery.mall.module.address.service.AddressService;
import com.lottery.mall.module.cart.domain.CartItem;
import com.lottery.mall.module.cart.service.CartService;
import com.lottery.mall.module.cart.domain.vo.CartItemVO;
import com.lottery.mall.module.channel.domain.ChannelInfo;
import com.lottery.mall.module.channel.service.ChannelService;
import com.lottery.mall.module.order.domain.OrderItem;
import com.lottery.mall.module.order.domain.OrderMain;
import com.lottery.mall.module.order.domain.dto.CreateOrderDTO;
import com.lottery.mall.module.order.domain.vo.OrderDetailVO;
import com.lottery.mall.module.order.domain.vo.OrderItemVO;
import com.lottery.mall.module.order.mapper.OrderItemMapper;
import com.lottery.mall.module.order.mapper.OrderMainMapper;
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
 * 订单 Service
 */
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMainMapper, OrderMain> {

    private final OrderItemMapper orderItemMapper;
    private final CartService cartService;
    private final AddressService addressService;
    private final ProductService productService;
    private final ProductSpecService productSpecService;
    private final ChannelService channelService;

    /**
     * 创建订单
     */
    @Transactional
    public OrderMain createOrder(Long userId, CreateOrderDTO dto) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 1. 处理商品信息
        if (dto.getCartItemIds() != null && !dto.getCartItemIds().isEmpty()) {
            // 购物车合并下单
            List<CartItemVO> cartItems = cartService.getList(userId);
            for (CartItemVO cartItem : cartItems) {
                if (dto.getCartItemIds().contains(cartItem.getCartId())) {
                    OrderItem item = createOrderItem(cartItem);
                    orderItems.add(item);
                    totalAmount = totalAmount.add(item.getTotalAmount());
                }
            }
        } else if (dto.getDirectItem() != null) {
            // 直接购买
            ProductSpec spec = productSpecService.getById(dto.getDirectItem().getSpecId());
            ProductInfo product = productService.getById(dto.getDirectItem().getProductId());

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setSpecId(spec.getId());
            item.setProductName(product.getProductName());
            item.setSpecName(spec.getSpecName());
            item.setPrice(spec.getPrice());
            item.setQuantity(dto.getDirectItem().getQuantity());
            item.setTotalAmount(spec.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItems.add(item);
            totalAmount = totalAmount.add(item.getTotalAmount());
        }

        if (orderItems.isEmpty()) {
            throw new BusinessException(ResultCode.ERROR, "订单商品不能为空");
        }

        // 2. 校验地址
        AddressInfo address = addressService.getById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ADDRESS_NOT_FOUND);
        }

        // 3. 计算佣金
        BigDecimal commissionAmount = BigDecimal.ZERO;
        if (dto.getChannelId() != null) {
            ChannelInfo channel = channelService.getById(dto.getChannelId());
            if (channel != null) {
                commissionAmount = totalAmount.multiply(channel.getCommissionRate());
            }
        }

        // 4. 创建订单
        OrderMain order = new OrderMain();
        order.setOrderNo(DateUtils.generateOrderNo());
        order.setUserId(userId);
        order.setRegionId(dto.getRegionId());
        order.setChannelId(dto.getChannelId());
        order.setAddressId(dto.getAddressId());
        order.setTotalAmount(totalAmount);
        order.setDeliveryFee(BigDecimal.ZERO); // 配送费暂为0
        order.setPayAmount(totalAmount); // 实付金额 = 商品总额
        order.setCommissionAmount(commissionAmount);
        order.setDeliveryType(dto.getDeliveryType());
        order.setOrderStatus(0); // 待支付
        order.setPayStatus(0); // 未支付
        order.setRemark(dto.getRemark() != null ? dto.getRemark() : "");
        save(order);

        // 5. 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
            // 扣减库存
            productSpecService.reduceStock(item.getSpecId(), item.getQuantity());
        }

        // 6. 清空购物车
        if (dto.getCartItemIds() != null) {
            for (Long cartId : dto.getCartItemIds()) {
                cartService.delete(userId, cartId);
            }
        }

        return order;
    }

    private OrderItem createOrderItem(CartItemVO cartItem) {
        OrderItem item = new OrderItem();
        item.setProductId(cartItem.getProductId());
        item.setSpecId(cartItem.getSpecId());
        item.setProductName(cartItem.getProductName());
        item.setSpecName(cartItem.getSpecName());
        item.setPrice(cartItem.getPrice());
        item.setQuantity(cartItem.getQuantity());
        item.setTotalAmount(cartItem.getTotalAmount());
        return item;
    }

    /**
     * 获取用户订单分页列表
     */
    public PageResult<OrderMain> getUserOrderPage(Long userId, PageRequest pageRequest, Integer status) {
        Page<OrderMain> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        LambdaQueryWrapper<OrderMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderMain::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(OrderMain::getOrderStatus, status);
        }
        queryWrapper.orderByDesc(OrderMain::getCreateTime);

        Page<OrderMain> result = page(page, queryWrapper);
        return PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取订单详情
     */
    public OrderDetailVO getDetail(Long userId, Long orderId) {
        OrderMain order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        return toDetailVO(order);
    }

    /**
     * 支付成功回调
     */
    @Transactional
    public void paySuccess(Long orderId, String transactionId) {
        OrderMain order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getPayStatus() != 0) {
            return; // 避免重复处理
        }

        order.setPayStatus(1);
        order.setPayTime(java.time.LocalDateTime.now());
        order.setTransactionId(transactionId);
        order.setOrderStatus(1); // 变为待接单
        updateById(order);
    }

    /**
     * 取消订单
     */
    @Transactional
    public void cancel(Long userId, Long orderId) {
        OrderMain order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getOrderStatus() != 0) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只有待支付的订单可以取消");
        }

        order.setOrderStatus(5); // 已取消
        order.setPayStatus(2); // 已退款（如果已支付需要退款）
        updateById(order);

        // 回滚库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            productSpecService.rollbackStock(item.getSpecId(), item.getQuantity());
        }
    }

    /**
     * 确认收货
     */
    @Transactional
    public void confirmReceive(Long userId, Long orderId) {
        OrderMain order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getOrderStatus() != 3) { // 配送中
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "订单状态异常");
        }

        order.setOrderStatus(4); // 已完成
        updateById(order);
    }

    private OrderDetailVO toDetailVO(OrderMain order) {
        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setOrderStatusName(getStatusName(order.getOrderStatus()));
        vo.setPayStatus(order.getPayStatus());
        vo.setPayType(order.getPayType());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setDeliveryFee(order.getDeliveryFee());
        vo.setPayAmount(order.getPayAmount());
        vo.setCommissionAmount(order.getCommissionAmount());
        vo.setRemark(order.getRemark());
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());

        // 地址信息
        AddressInfo address = addressService.getById(order.getAddressId());
        if (address != null) {
            vo.setConsigneeName(address.getConsigneeName());
            vo.setConsigneePhone(address.getConsigneePhone());
            vo.setFullAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getDetailAddress());
        }

        // 配送方式
        vo.setDeliveryTypeName(order.getDeliveryType() == 1 ? "同城快递" : "门店自取");

        // 订单项
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        vo.setItems(items.stream().map(this::toItemVO).collect(Collectors.toList()));

        return vo;
    }

    private OrderItemVO toItemVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        vo.setProductId(item.getProductId());
        vo.setSpecId(item.getSpecId());
        vo.setProductName(item.getProductName());
        vo.setSpecName(item.getSpecName());
        vo.setPrice(item.getPrice());
        vo.setQuantity(item.getQuantity());
        vo.setTotalAmount(item.getTotalAmount());

        ProductInfo product = productService.getById(item.getProductId());
        if (product != null) {
            String[] images = product.getImages() != null ? product.getImages().split(",") : new String[0];
            vo.setImage(images.length > 0 ? images[0] : "");
        }
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
