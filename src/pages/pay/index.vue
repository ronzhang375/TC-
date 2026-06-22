<template>
  <view class="pay-page">
    <view class="pay-info">
      <view class="amount">¥{{ order.payAmount || '0.00' }}</view>
      <view class="order-no">订单号：{{ order.orderNo }}</view>
    </view>
    <view class="pay-methods">
      <view class="method-title">支付方式</view>
      <view class="method-item">
        <text>微信支付</text>
        <u-icon name="checkmark-circle-fill" size="20" color="#e54d42" />
      </view>
    </view>
    <view class="bottom-bar">
      <view class="pay-btn" @click="doPay">确认支付 ¥{{ order.payAmount }}</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getOrderDetail } from '../../api/order'
import { request } from '../../utils/request'

const orderId = ref(null)
const order = ref({})

onLoad((options) => {
  orderId.value = options.orderId
  loadOrder()
})

async function loadOrder() {
  const res = await getOrderDetail(orderId.value)
  if (res.code === 200) order.value = res.data
}

async function doPay() {
  uni.showLoading({ title: '支付中...' })
  try {
    const res = await request({ url: `/app/pay/create?orderId=${orderId.value}`, method: 'POST' })
    uni.hideLoading()
    if (res.code === 200) {
      uni.requestPayment({
        provider: 'wxpay',
        timeStamp: res.data.timeStamp,
        nonceStr: res.data.nonceStr,
        package: res.data.packageValue,
        signType: res.data.signType,
        paySign: res.data.paySign,
        success: () => {
          uni.showToast({ title: '支付成功', icon: 'success' })
          setTimeout(() => uni.redirectTo({ url: '/pages/order/list' }), 1500)
        },
        fail: () => uni.redirectTo({ url: '/pages/order/list' })
      })
    }
  } catch (e) { uni.hideLoading() }
}
</script>

<style lang="scss" scoped>
.pay-page { min-height: 100vh; background-color: #f5f5f5; }
.pay-info { background: linear-gradient(135deg, #e54d42, #ff7e7e); padding: 80rpx 0; text-align: center; color: #ffffff; }
.amount { font-size: 72rpx; font-weight: bold; }
.order-no { font-size: 26rpx; margin-top: 20rpx; opacity: 0.8; }
.pay-methods { background-color: #ffffff; margin: 20rpx; border-radius: 12rpx; padding: 30rpx 20rpx; }
.method-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; }
.method-item { display: flex; justify-content: space-between; align-items: center; padding: 20rpx 0; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx; background-color: #ffffff; border-top: 1rpx solid #eee; }
.pay-btn { background-color: #e54d42; color: #ffffff; height: 90rpx; line-height: 90rpx; text-align: center; border-radius: 45rpx; font-size: 32rpx; font-weight: bold; }
</style>