<template>
  <view class="user-page">
    <view class="user-info">
      <image class="avatar" :src="userStore.userInfo.avatar || '/static/images/default-avatar.png'" />
      <view v-if="userStore.isLoggedIn" class="user-detail">
        <view class="nickname">{{ userStore.userInfo.nickName || '用户' }}</view>
      </view>
      <view v-else class="login-btn" @click="goLogin">点击登录</view>
    </view>
    <view class="order-section">
      <view class="section-header">
        <text class="section-title">我的订单</text>
        <view class="all-link" @click="goOrderList(null)">查看全部 ></view>
      </view>
      <view class="order-types">
        <view class="order-type" @click="goOrderList(0)"><text>待支付</text></view>
        <view class="order-type" @click="goOrderList(1)"><text>待接单</text></view>
        <view class="order-type" @click="goOrderList(2)"><text>待发货</text></view>
        <view class="order-type" @click="goOrderList(3)"><text>配送中</text></view>
      </view>
    </view>
    <view class="feature-list">
      <view class="feature-item" @click="goAddress"><text>收货地址管理</text></view>
    </view>
    <view v-if="userStore.isLoggedIn" class="logout-btn" @click="logout">退出登录</view>
  </view>
</template>

<script setup>
import { useUserStore } from '../../store/user'
const userStore = useUserStore()
function goLogin() { uni.navigateTo({ url: '/pages/user/login' }) }
function goOrderList(status) { uni.switchTab({ url: '/pages/order/list' }) }
function goAddress() { uni.navigateTo({ url: '/pages/address/list' }) }
function logout() {
  uni.showModal({ title: '提示', content: '确定退出登录？', success: (res) => { if (res.confirm) userStore.logout() } })
}
</script>

<style lang="scss" scoped>
.user-page { min-height: 100vh; background-color: #f5f5f5; }
.user-info { background: linear-gradient(135deg, #e54d42, #ff7e7e); padding: 60rpx 30rpx; display: flex; align-items: center; }
.avatar { width: 130rpx; height: 130rpx; border-radius: 50%; border: 4rpx solid #ffffff; background-color: #ffffff; }
.user-detail { margin-left: 30rpx; color: #ffffff; }
.nickname { font-size: 36rpx; font-weight: bold; }
.login-btn { margin-left: 30rpx; padding: 16rpx 40rpx; background-color: rgba(255, 255, 255, 0.2); color: #ffffff; border-radius: 40rpx; font-size: 28rpx; }
.order-section { background-color: #ffffff; margin: 20rpx; border-radius: 12rpx; padding: 30rpx 20rpx; }
.section-header { display: flex; justify-content: space-between; padding-bottom: 20rpx; }
.section-title { font-size: 30rpx; font-weight: bold; }
.all-link { font-size: 24rpx; color: #999; }
.order-types { display: flex; justify-content: space-between; }
.order-type { display: flex; flex-direction: column; align-items: center; font-size: 24rpx; padding: 10rpx 0; color: #666; }
.feature-list { background-color: #ffffff; margin: 20rpx; border-radius: 12rpx; }
.feature-item { padding: 30rpx 20rpx; border-bottom: 1rpx solid #f5f5f5; font-size: 28rpx; }
.logout-btn { margin: 40rpx 30rpx; padding: 24rpx 0; text-align: center; background-color: #ffffff; border-radius: 12rpx; font-size: 30rpx; color: #e54d42; }
</style>