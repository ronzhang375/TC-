<template>
  <view class="home-page">
    <view class="search-bar">
      <u-search placeholder="搜索商品" v-model="keyword" @click="goSearch" />
    </view>
    <view class="banner">
      <u-swiper :list="banners" :autoplay="true" :interval="3000" :duration="500" />
    </view>
    <view class="categories">
      <view class="category-item" v-for="item in categories" :key="item.id" @click="goCategory(item)">
        <image class="category-icon" src="/static/icons/flower.png" />
        <text class="category-name">{{ item.categoryName }}</text>
      </view>
    </view>
    <view class="section-title">推荐商品</view>
    <view class="product-list">
      <view class="product-card" v-for="item in products" :key="item.id" @click="goDetail(item)">
        <image class="product-image" :src="item.images[0] || '/static/images/placeholder.png'" />
        <view class="product-info">
          <view class="product-name">{{ item.productName }}</view>
          <view class="product-price"><text class="price">¥{{ item.minPrice }}</text></view>
          <view class="product-sales">已售 {{ item.salesCount }}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onPullDownRefresh, onReachBottom } from '@dcloudio/uni-app'
import { getProductList, getCategoryTree } from '../../api/product'

const keyword = ref('')
const banners = ref([{ image: '/static/images/banner1.png' }, { image: '/static/images/banner2.png' }])
const categories = ref([])
const products = ref([])
const pageNum = ref(1)
const total = ref(0)
const loading = ref(false)
const hasMore = ref(true)

onMounted(() => {
  loadCategories()
  loadProducts()
})

onPullDownRefresh(async () => {
  pageNum.value = 1
  hasMore.value = true
  await loadCategories()
  await loadProducts()
  uni.stopPullDownRefresh()
})

onReachBottom(() => {
  if (hasMore.value && !loading.value) {
    pageNum.value++
    loadProducts()
  }
})

async function loadCategories() {
  const res = await getCategoryTree()
  if (res.code === 200) categories.value = res.data || []
}

async function loadProducts() {
  loading.value = true
  try {
    const res = await getProductList({ pageNum: pageNum.value, pageSize: 10 })
    if (res.code === 200) {
      const list = res.data.rows || []
      if (pageNum.value === 1) products.value = list
      else products.value.push(...list)
      total.value = res.data.total
      hasMore.value = products.value.length < total.value
    }
  } finally {
    loading.value = false
  }
}

function goSearch() { uni.navigateTo({ url: '/pages/product/list' }) }
function goCategory(item) { uni.navigateTo({ url: `/pages/product/list?categoryId=${item.id}` }) }
function goDetail(item) { uni.navigateTo({ url: `/pages/product/detail?id=${item.id}` }) }
</script>

<style lang="scss" scoped>
.home-page { min-height: 100vh; padding-bottom: 40rpx; }
.search-bar { padding: 20rpx; background-color: #ffffff; }
.banner { margin: 20rpx; border-radius: 16rpx; overflow: hidden; }
.categories { background-color: #ffffff; padding: 30rpx 20rpx; margin: 20rpx; border-radius: 16rpx; display: flex; flex-wrap: wrap; }
.category-item { width: 25%; display: flex; flex-direction: column; align-items: center; padding: 20rpx 0; }
.category-icon { width: 80rpx; height: 80rpx; }
.category-name { font-size: 24rpx; margin-top: 10rpx; }
.section-title { padding: 30rpx 20rpx 20rpx; font-size: 32rpx; font-weight: bold; }
.product-list { display: flex; flex-wrap: wrap; justify-content: space-between; padding: 0 20rpx; }
.product-card { width: 340rpx; background-color: #ffffff; border-radius: 16rpx; margin-bottom: 20rpx; overflow: hidden; }
.product-image { width: 100%; height: 340rpx; background-color: #f5f5f5; }
.product-info { padding: 20rpx; }
.product-name { font-size: 28rpx; line-height: 1.4; height: 80rpx; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden; }
.price { color: #e54d42; font-size: 32rpx; font-weight: bold; }
.product-sales { font-size: 22rpx; color: #999; margin-top: 10rpx; }
</style>