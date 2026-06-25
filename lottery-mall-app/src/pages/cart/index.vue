<template>
  <view class="cart-page">
    <view v-if="cartStore.items.length === 0 && !loading" class="empty">
      <u-empty text="购物车空空如也" />
      <u-button type="primary" @click="goHome">去逛逛</u-button>
    </view>
    <view v-else>
      <view class="cart-list">
        <view v-for="item in cartStore.items" :key="item.cartId" class="cart-item">
          <view class="checkbox" @click="toggleSelect(item.cartId)">
            <u-icon :name="cartStore.selectedIds.includes(item.cartId) ? 'checkbox-mark' : 'checkbox-blank'"
                    :color="cartStore.selectedIds.includes(item.cartId) ? '#e54d42' : '#999'" size="20" />
          </view>
          <image class="item-image" :src="item.image || '/static/images/placeholder.png'" />
          <view class="item-info">
            <view class="item-name">{{ item.productName }}</view>
            <view class="item-spec">{{ item.specName }}</view>
            <view class="item-bottom">
              <text class="price">¥{{ item.price }}</text>
              <view class="quantity-control">
                <view class="qty-btn" @click="updateQty(item, item.quantity - 1)">-</view>
                <view class="qty-value">{{ item.quantity }}</view>
                <view class="qty-btn" @click="updateQty(item, item.quantity + 1)">+</view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <view class="bottom-bar">
        <view class="all-checkbox" @click="toggleAll">
          <u-icon :name="cartStore.isAllSelected ? 'checkbox-mark' : 'checkbox-blank'"
                  :color="cartStore.isAllSelected ? '#e54d42' : '#999'" size="20" />
          <text>全选</text>
        </view>
        <view class="total-info">合计：<text class="total-price">¥{{ cartStore.totalAmount }}</text></view>
        <view class="checkout-btn" :class="{ disabled: cartStore.selectedCount === 0 }" @click="goCheckout">
          结算({{ cartStore.selectedCount }})
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useCartStore } from '../../store/cart'

const cartStore = useCartStore()
const loading = ref(false)

onMounted(() => loadCart())
onShow(() => loadCart())

async function loadCart() {
  loading.value = true
  try { await cartStore.fetchCartList() } finally { loading.value = false }
}

function toggleSelect(cartId) { cartStore.toggleSelect(cartId) }
function toggleAll() { cartStore.toggleAllSelect() }
async function updateQty(item, quantity) { await cartStore.updateQuantity(item.cartId, quantity) }

function goCheckout() {
  if (cartStore.selectedCount === 0) return uni.showToast({ title: '请选择商品', icon: 'none' })
  uni.navigateTo({ url: `/pages/order/confirm?cartIds=${cartStore.selectedIds.join(',')}` })
}
function goHome() { uni.switchTab({ url: '/pages/index/index' }) }
</script>

<style lang="scss" scoped>
.cart-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 100rpx; }
.empty { padding: 200rpx 60rpx; display: flex; flex-direction: column; align-items: center; }
.cart-list { padding: 20rpx; }
.cart-item { display: flex; align-items: center; background-color: #ffffff; border-radius: 12rpx; padding: 20rpx; margin-bottom: 20rpx; }
.checkbox { padding: 10rpx; }
.item-image { width: 180rpx; height: 180rpx; border-radius: 8rpx; background-color: #f5f5f5; margin: 0 20rpx; }
.item-info { flex: 1; }
.item-name { font-size: 28rpx; line-height: 1.4; }
.item-spec { font-size: 22rpx; color: #999; margin-top: 10rpx; }
.item-bottom { margin-top: 20rpx; display: flex; justify-content: space-between; align-items: center; }
.price { color: #e54d42; font-size: 32rpx; font-weight: bold; }
.quantity-control { display: flex; align-items: center; border: 1rpx solid #ddd; border-radius: 6rpx; }
.qty-btn { width: 56rpx; height: 56rpx; display: flex; align-items: center; justify-content: center; background-color: #f5f5f5; }
.qty-value { width: 80rpx; text-align: center; font-size: 26rpx; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; height: 100rpx; background-color: #ffffff; display: flex; align-items: center; padding: 0 20rpx; border-top: 1rpx solid #eee; }
.all-checkbox { display: flex; align-items: center; padding: 10rpx; font-size: 26rpx; }
.total-info { flex: 1; text-align: right; padding-right: 20rpx; font-size: 28rpx; }
.total-price { color: #e54d42; font-size: 36rpx; font-weight: bold; }
.checkout-btn { background-color: #e54d42; color: #ffffff; padding: 0 30rpx; height: 80rpx; line-height: 80rpx; border-radius: 40rpx; font-size: 28rpx; }
.checkout-btn.disabled { background-color: #cccccc; }
</style>