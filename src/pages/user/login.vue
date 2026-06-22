<template>
  <view class="login-page">
    <view class="logo-section">
      <image class="logo" src="/static/images/logo.png" />
      <view class="app-name">体彩衍生品商城</view>
    </view>
    <view class="login-content">
      <view class="welcome">欢迎登录</view>
      <view class="welcome-sub">买即赠彩票，一起分享惊喜</view>
      <view class="login-btn primary" @click="wxLogin">
        <text>微信一键登录</text>
      </view>
      <view class="agreement">
        <view class="checkbox" @click="agreed = !agreed">
          <u-icon :name="agreed ? 'checkbox-mark' : 'checkbox-blank'" :color="agreed ? '#e54d42' : '#999'" size="16" />
        </view>
        <text class="agreement-text">我已阅读并同意《用户协议》《隐私政策》</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../store/user'
import { useCartStore } from '../../store/cart'

const userStore = useUserStore()
const cartStore = useCartStore()
const agreed = ref(false)

async function wxLogin() {
  if (!agreed.value) return uni.showToast({ title: '请先同意协议', icon: 'none' })
  uni.showLoading({ title: '登录中...' })
  try {
    await userStore.wxLoginAction()
    uni.hideLoading()
    await cartStore.fetchCount()
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 1000)
  } catch (e) { uni.hideLoading(); uni.showToast({ title: '登录失败', icon: 'none' }) }
}
</script>

<style lang="scss" scoped>
.login-page { min-height: 100vh; background-color: #ffffff; }
.logo-section { padding: 100rpx 0 60rpx; text-align: center; }
.logo { width: 160rpx; height: 160rpx; }
.app-name { font-size: 36rpx; font-weight: bold; margin-top: 20rpx; }
.login-content { padding: 40rpx; }
.welcome { font-size: 48rpx; font-weight: bold; }
.welcome-sub { font-size: 28rpx; color: #999; margin-top: 20rpx; margin-bottom: 80rpx; }
.login-btn { display: flex; align-items: center; justify-content: center; height: 90rpx; border-radius: 45rpx; font-size: 32rpx; }
.login-btn.primary { background-color: #07c160; color: #ffffff; }
.agreement { display: flex; align-items: center; justify-content: center; font-size: 24rpx; color: #666; margin-top: 40rpx; }
</style>