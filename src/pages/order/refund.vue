<template>
  <view class="refund-page">
    <view class="form-section">
      <view class="section-title">退款原因</view>
      <view class="reason-list">
        <view v-for="item in reasons" :key="item.value" class="reason-item" :class="{ active: form.refundReason === item.value }" @click="form.refundReason = item.value">{{ item.label }}</view>
      </view>
    </view>
    <view class="form-section">
      <view class="section-title">退款说明</view>
      <textarea v-model="form.refundDesc" class="textarea" placeholder="请详细描述退款原因" maxlength="200" />
    </view>
    <view class="bottom-bar"><view class="submit-btn" @click="submit">提交申请</view></view>
  </view>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { applyRefund, getOrderDetail } from '../../api/order'

const orderId = ref(null)
const order = ref({})
const reasons = [{ label: '商品损坏', value: 'damaged' }, { label: '商品与描述不符', value: 'not_match' }, { label: '物流太慢', value: 'delivery_slow' }, { label: '其他', value: 'other' }]
const form = reactive({ refundType: 1, refundReason: '', refundDesc: '' })

onLoad(async (options) => {
  orderId.value = options.orderId
  const res = await getOrderDetail(orderId.value)
  if (res.code === 200) order.value = res.data
})

async function submit() {
  if (!form.refundReason) return uni.showToast({ title: '请选择退款原因', icon: 'none' })
  uni.showLoading({ title: '提交中...' })
  try {
    const res = await applyRefund({ orderId: orderId.value, refundType: form.refundType, refundReason: form.refundReason, refundDesc: form.refundDesc, refundAmount: order.value.payAmount })
    uni.hideLoading()
    if (res.code === 200) {
      uni.showToast({ title: '申请已提交', icon: 'success' })
      setTimeout(() => uni.navigateBack(), 1500)
    }
  } catch (e) { uni.hideLoading() }
}
</script>

<style lang="scss" scoped>
.refund-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 120rpx; }
.form-section { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.section-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; }
.reason-list { display: flex; flex-wrap: wrap; gap: 16rpx; }
.reason-item { padding: 16rpx 30rpx; background-color: #f5f5f5; border-radius: 8rpx; font-size: 26rpx; }
.reason-item.active { background-color: #fff5f5; color: #e54d42; border: 1rpx solid #e54d42; }
.textarea { width: 100%; height: 200rpx; background-color: #f5f5f5; border-radius: 8rpx; padding: 20rpx; font-size: 28rpx; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; padding: 20rpx; background-color: #ffffff; }
.submit-btn { background-color: #e54d42; color: #ffffff; height: 90rpx; line-height: 90rpx; text-align: center; border-radius: 45rpx; font-size: 32rpx; font-weight: bold; }
</style>