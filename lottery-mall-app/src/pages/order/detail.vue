<template>
  <view class="order-detail-page">
    <view class="status-bar" :class="`status-${order.orderStatus}`">
      <text class="status-text">{{ order.orderStatusName }}</text>
    </view>
    <view class="address-section">
      <view class="consignee-row"><text class="consignee">{{ order.consigneeName }}</text><text class="phone">{{ order.consigneePhone }}</text></view>
      <view class="address">{{ order.fullAddress }}</view>
    </view>
    <view class="goods-section">
      <view v-for="item in order.items || []" :key="item.specId" class="goods-item">
        <image class="goods-image" :src="item.image || '/static/images/placeholder.png'" />
        <view class="goods-info">
          <view class="goods-name">{{ item.productName }}</view>
          <view class="goods-spec">{{ item.specName }}</view>
          <view class="goods-bottom"><text class="price">¥{{ item.price }}</text><text>x{{ item.quantity }}</text></view>
        </view>
      </view>
    </view>
    <view class="info-card">
      <view class="info-row"><text>订单编号</text><text>{{ order.orderNo }}</text></view>
      <view class="info-row"><text>下单时间</text><text>{{ order.createTime }}</text></view>
      <view class="info-row total"><text>实付金额</text><text class="price-total">¥{{ order.payAmount }}</text></view>
    </view>
    <view class="bottom-bar">
      <view v-if="order.orderStatus === 0" class="action-btn" @click="cancelOrder">取消订单</view>
      <view v-if="order.orderStatus === 0" class="action-btn primary" @click="payOrder">去支付</view>
      <view v-if="order.orderStatus === 3" class="action-btn primary" @click="confirmReceive">确认收货</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getOrderDetail, cancelOrder as cancelOrderApi, confirmReceive as confirmReceiveApi } from '../../api/order'

const orderId = ref(null)
const order = ref({})

onLoad((options) => { orderId.value = options.id; loadOrder() })

async function loadOrder() {
  const res = await getOrderDetail(orderId.value)
  if (res.code === 200) order.value = res.data
}

async function cancelOrder() {
  await cancelOrderApi(orderId.value)
  uni.showToast({ title: '已取消', icon: 'success' })
  setTimeout(() => uni.navigateBack(), 1000)
}
function payOrder() { uni.redirectTo({ url: `/pages/pay/index?orderId=${orderId.value}` }) }
async function confirmReceive() {
  await confirmReceiveApi(orderId.value)
  uni.showToast({ title: '已确认收货', icon: 'success' })
  loadOrder()
}
</script>

<style lang="scss" scoped>
.order-detail-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 120rpx; }
.status-bar { padding: 60rpx 30rpx; color: #ffffff; }
.status-0 { background-color: #ff9900; }
.status-1, .status-2, .status-3 { background-color: #1890ff; }
.status-4 { background-color: #52c41a; }
.status-text { font-size: 36rpx; font-weight: bold; }
.address-section { background-color: #ffffff; padding: 30rpx 20rpx; }
.consignee-row { font-size: 30rpx; font-weight: bold; margin-bottom: 10rpx; }
.consignee { margin-right: 20rpx; }
.address { font-size: 26rpx; color: #666; }
.goods-section { background-color: #ffffff; padding: 30rpx 20rpx; margin: 20rpx 0; }
.goods-item { display: flex; margin-bottom: 20rpx; }
.goods-image { width: 180rpx; height: 180rpx; border-radius: 8rpx; margin-right: 20rpx; }
.goods-info { flex: 1; }
.goods-name { font-size: 28rpx; }
.goods-spec { font-size: 22rpx; color: #999; margin-top: 10rpx; }
.goods-bottom { display: flex; justify-content: space-between; margin-top: 20rpx; }
.price { color: #e54d42; font-size: 30rpx; font-weight: bold; }
.info-card { background-color: #ffffff; margin: 20rpx 0; padding: 20rpx; }
.info-row { display: flex; justify-content: space-between; padding: 20rpx 0; font-size: 28rpx; }
.info-row.total { font-weight: bold; border-top: 1rpx solid #eee; padding-top: 20rpx; }
.price-total { color: #e54d42; font-size: 32rpx; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx; background-color: #ffffff; display: flex; justify-content: flex-end; gap: 20rpx; }
.action-btn { padding: 16rpx 40rpx; border: 1rpx solid #ddd; border-radius: 32rpx; font-size: 28rpx; }
.action-btn.primary { background-color: #e54d42; color: #ffffff; border-color: #e54d42; }
</style>