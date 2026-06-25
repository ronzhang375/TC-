<template>
  <view class="confirm-page">
    <view class="address-section" @click="chooseAddress">
      <view v-if="address" class="address-info">
        <view class="consignee-row"><text class="consignee">{{ address.consigneeName }}</text><text class="phone">{{ address.consigneePhone }}</text></view>
        <view class="address">{{ address.provinceName }}{{ address.cityName }}{{ address.districtName }}{{ address.detailAddress }}</view>
      </view>
      <view v-else class="no-address"><text>请选择收货地址</text></view>
    </view>
    <view class="goods-section">
      <view class="section-title">商品信息</view>
      <view v-for="item in orderItems" :key="item.specId" class="goods-item">
        <image class="goods-image" :src="item.image || '/static/images/placeholder.png'" />
        <view class="goods-info">
          <view class="goods-name">{{ item.productName }}</view>
          <view class="goods-spec">{{ item.specName }}</view>
          <view class="goods-bottom"><text class="price">¥{{ item.price }}</text><text class="quantity">x{{ item.quantity }}</text></view>
        </view>
      </view>
    </view>
    <view class="bottom-bar">
      <view class="total-info">实付：<text class="total-price">¥{{ totalAmount }}</text></view>
      <view class="submit-btn" @click="submitOrder">提交订单</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getDefaultAddress } from '../../api/user'
import { createOrder } from '../../api/order'
import { getProductDetail } from '../../api/product'
import { useCartStore } from '../../store/cart'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const cartStore = useCartStore()
const address = ref(null)
const orderItems = ref([])
const remark = ref('')
const deliveryType = ref(1)
const isDirect = ref(false)
const directItem = ref(null)
const cartIds = ref([])

onLoad(async (options) => {
  if (options.direct === 'true') {
    isDirect.value = true
    directItem.value = { productId: Number(options.productId), specId: Number(options.specId), quantity: Number(options.quantity) }
    const res = await getProductDetail(directItem.value.productId)
    if (res.code === 200) {
      const spec = res.data.specs.find(s => s.id === directItem.value.specId)
      orderItems.value = [{ productId: res.data.id, specId: spec.id, productName: res.data.productName, specName: spec.specName, image: res.data.images?.[0], price: spec.price, quantity: directItem.value.quantity }]
    }
  } else {
    cartIds.value = options.cartIds?.split(',').map(Number) || []
    orderItems.value = cartStore.items.filter(item => cartIds.value.includes(item.cartId))
  }
  loadAddress()
})

async function loadAddress() {
  const res = await getDefaultAddress()
  if (res.code === 200 && res.data) address.value = res.data
}

const totalAmount = computed(() => orderItems.value.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2))

async function submitOrder() {
  if (!address.value) return uni.showToast({ title: '请选择收货地址', icon: 'none' })
  uni.showLoading({ title: '提交中...' })
  try {
    const res = await createOrder({
      addressId: address.value.id,
      regionId: 1,
      deliveryType: deliveryType.value,
      remark: remark.value,
      cartItemIds: isDirect.value ? null : cartIds.value,
      directItem: isDirect.value ? directItem.value : null
    })
    uni.hideLoading()
    if (res.code === 200) {
      if (!isDirect.value) cartStore.clearSelected()
      uni.redirectTo({ url: `/pages/pay/index?orderId=${res.data.orderId}` })
    }
  } catch (e) { uni.hideLoading() }
}

function chooseAddress() { uni.navigateTo({ url: '/pages/address/list?select=1' }) }
</script>

<style lang="scss" scoped>
.confirm-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 120rpx; }
.address-section { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.consignee-row { font-size: 30rpx; font-weight: bold; margin-bottom: 10rpx; }
.consignee { margin-right: 20rpx; }
.address { font-size: 26rpx; color: #666; }
.no-address { padding: 40rpx 0; text-align: center; color: #999; }
.goods-section { background-color: #ffffff; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.section-title { font-size: 28rpx; font-weight: bold; margin-bottom: 20rpx; }
.goods-item { display: flex; margin-bottom: 20rpx; }
.goods-image { width: 160rpx; height: 160rpx; border-radius: 8rpx; margin-right: 20rpx; }
.goods-info { flex: 1; }
.goods-name { font-size: 28rpx; }
.goods-spec { font-size: 22rpx; color: #999; margin-top: 10rpx; }
.goods-bottom { display: flex; justify-content: space-between; margin-top: 20rpx; }
.price { color: #e54d42; font-size: 30rpx; font-weight: bold; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; height: 100rpx; background-color: #ffffff; display: flex; align-items: center; padding: 0 20rpx; border-top: 1rpx solid #eee; }
.total-info { flex: 1; font-size: 28rpx; }
.total-price { color: #e54d42; font-size: 36rpx; font-weight: bold; }
.submit-btn { background-color: #e54d42; color: #ffffff; padding: 0 50rpx; height: 80rpx; line-height: 80rpx; border-radius: 40rpx; font-size: 30rpx; }
</style>