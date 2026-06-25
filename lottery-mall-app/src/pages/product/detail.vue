<template>
  <view class="detail-page">
    <swiper class="product-swiper" :indicator-dots="true" :autoplay="true">
      <swiper-item v-for="(img, idx) in product.images || []" :key="idx">
        <image :src="img" class="product-image" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="product-info">
      <view class="price-row"><text class="price-current">¥{{ currentPrice }}</text></view>
      <view class="product-name">{{ product.productName }}</view>
      <view class="product-desc">{{ product.description }}</view>
      <view class="sales-row"><text>已售 {{ product.salesCount || 0 }}</text></view>
    </view>
    <view class="specs-section">
      <view class="section-title">选择规格</view>
      <view class="spec-list">
        <view v-for="(spec, idx) in product.specs || []" :key="spec.id"
              class="spec-item" :class="{ active: selectedSpecIndex === idx, disabled: spec.stock <= 0 }"
              @click="selectSpec(idx)">
          <text class="spec-name">{{ spec.specName }}</text>
          <text class="spec-price">¥{{ spec.price }}</text>
        </view>
      </view>
    </view>
    <view class="quantity-section">
      <text class="quantity-label">数量</text>
      <view class="quantity-control">
        <view class="qty-btn" @click="decreaseQty">-</view>
        <view class="qty-value">{{ quantity }}</view>
        <view class="qty-btn" @click="increaseQty">+</view>
      </view>
    </view>
    <view class="bottom-bar">
      <view class="bottom-btn cart-btn" @click="goCart">购物车</view>
      <view class="bottom-btn add-btn" @click="addToCartAction">加入购物车</view>
      <view class="bottom-btn buy-btn" @click="buyNow">立即购买</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getProductDetail } from '../../api/product'
import { useCartStore } from '../../store/cart'
import { useUserStore } from '../../store/user'

const product = ref({})
const selectedSpecIndex = ref(0)
const quantity = ref(1)
const productId = ref(null)
const cartStore = useCartStore()
const userStore = useUserStore()

onLoad((options) => {
  productId.value = options.id
  loadDetail()
})

async function loadDetail() {
  const res = await getProductDetail(productId.value)
  if (res.code === 200) product.value = res.data
}

const currentSpec = computed(() => product.value.specs?.[selectedSpecIndex.value])
const currentPrice = computed(() => currentSpec.value?.price || product.value.minPrice || '—')

function selectSpec(index) {
  if (product.value.specs[index].stock <= 0) {
    uni.showToast({ title: '该规格缺货', icon: 'none' })
    return
  }
  selectedSpecIndex.value = index
}
function increaseQty() {
  if (currentSpec.value && quantity.value >= currentSpec.value.stock) {
    uni.showToast({ title: '已达到库存上限', icon: 'none' })
    return
  }
  quantity.value++
}
function decreaseQty() { if (quantity.value > 1) quantity.value-- }

async function addToCartAction() {
  if (!userStore.isLoggedIn) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    return
  }
  await cartStore.addToCartAction(productId.value, currentSpec.value.id, quantity.value)
  uni.showToast({ title: '已加入购物车', icon: 'success' })
}

function buyNow() {
  if (!userStore.isLoggedIn) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    return
  }
  uni.navigateTo({
    url: `/pages/order/confirm?direct=true&productId=${productId.value}&specId=${currentSpec.value.id}&quantity=${quantity.value}`
  })
}
function goCart() { uni.switchTab({ url: '/pages/cart/index' }) }
</script>

<style lang="scss" scoped>
.detail-page { min-height: 100vh; padding-bottom: 120rpx; background-color: #f5f5f5; }
.product-swiper { width: 100%; height: 750rpx; background-color: #ffffff; }
.product-image { width: 100%; height: 100%; }
.product-info { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.price-current { color: #e54d42; font-size: 48rpx; font-weight: bold; }
.product-name { font-size: 32rpx; line-height: 1.4; font-weight: bold; margin-bottom: 20rpx; }
.product-desc { font-size: 26rpx; color: #666; margin-bottom: 20rpx; }
.specs-section { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.section-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; }
.spec-list { display: flex; flex-wrap: wrap; gap: 16rpx; }
.spec-item { padding: 16rpx 24rpx; background-color: #f5f5f5; border-radius: 8rpx; border: 2rpx solid transparent; }
.spec-item.active { border-color: #e54d42; background-color: #fff5f5; }
.spec-item.disabled { opacity: 0.5; }
.spec-name { font-size: 26rpx; }
.spec-price { color: #e54d42; font-size: 22rpx; display: block; margin-top: 4rpx; }
.quantity-section { background-color: #ffffff; padding: 30rpx 20rpx; display: flex; align-items: center; }
.quantity-label { font-size: 28rpx; margin-right: 20rpx; }
.quantity-control { display: flex; align-items: center; border: 1rpx solid #ddd; border-radius: 8rpx; }
.qty-btn { width: 64rpx; height: 64rpx; display: flex; align-items: center; justify-content: center; background-color: #f5f5f5; font-size: 32rpx; }
.qty-value { width: 100rpx; text-align: center; font-size: 28rpx; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; height: 100rpx; background-color: #ffffff; display: flex; border-top: 1rpx solid #eee; }
.bottom-btn { flex: 1; display: flex; align-items: center; justify-content: center; font-size: 26rpx; }
.cart-btn { background-color: #ffffff; }
.add-btn { background-color: #ff9900; color: #ffffff; flex: 2; }
.buy-btn { background-color: #e54d42; color: #ffffff; flex: 2; }
</style>