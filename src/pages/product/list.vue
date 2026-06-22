<template>
  <view class="list-page">
    <view class="search-bar"><u-search placeholder="搜索商品" v-model="keyword" @search="onSearch" :show-action="false" /></view>
    <view class="filter-bar">
      <view v-for="item in filters" :key="item.value" class="filter-item" :class="{ active: sortType === item.value }" @click="changeSort(item.value)">{{ item.label }}</view>
    </view>
    <view class="product-list">
      <view v-for="item in products" :key="item.id" class="product-card" @click="goDetail(item)">
        <image class="product-image" :src="item.images[0] || '/static/images/placeholder.png'" />
        <view class="product-info">
          <view class="product-name">{{ item.productName }}</view>
          <view class="product-price"><text class="price">¥{{ item.minPrice }}</text></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onLoad, onReachBottom } from '@dcloudio/uni-app'
import { getProductList } from '../../api/product'

const keyword = ref('')
const products = ref([])
const sortType = ref('default')
const categoryId = ref(null)
const pageNum = ref(1)
const total = ref(0)
const loading = ref(false)
const hasMore = ref(true)
const filters = [{ label: '综合', value: 'default' }, { label: '销量', value: 'sales' }, { label: '价格↑', value: 'price_asc' }, { label: '价格↓', value: 'price_desc' }]

onLoad((options) => {
  if (options.keyword) keyword.value = options.keyword
  if (options.categoryId) categoryId.value = Number(options.categoryId)
  loadProducts()
})
onReachBottom(() => { if (hasMore.value && !loading.value) { pageNum.value++; loadProducts() } })

async function loadProducts() {
  loading.value = true
  try {
    const res = await getProductList({ pageNum: pageNum.value, pageSize: 10, keyword: keyword.value, categoryId: categoryId.value, sortType: sortType.value })
    if (res.code === 200) {
      const list = res.data.rows || []
      if (pageNum.value === 1) products.value = list
      else products.value.push(...list)
      total.value = res.data.total
      hasMore.value = products.value.length < total.value
    }
  } finally { loading.value = false }
}
function onSearch() { pageNum.value = 1; hasMore.value = true; loadProducts() }
function changeSort(type) { sortType.value = type; pageNum.value = 1; hasMore.value = true; loadProducts() }
function goDetail(item) { uni.navigateTo({ url: `/pages/product/detail?id=${item.id}` }) }
</script>

<style lang="scss" scoped>
.list-page { min-height: 100vh; background-color: #f5f5f5; }
.search-bar { background-color: #ffffff; padding: 20rpx; }
.filter-bar { background-color: #ffffff; display: flex; position: sticky; top: 0; z-index: 10; }
.filter-item { flex: 1; padding: 24rpx 0; text-align: center; font-size: 28rpx; color: #666; }
.filter-item.active { color: #e54d42; font-weight: bold; }
.product-list { display: flex; flex-wrap: wrap; justify-content: space-between; padding: 20rpx; }
.product-card { width: 340rpx; background-color: #ffffff; border-radius: 12rpx; margin-bottom: 20rpx; overflow: hidden; }
.product-image { width: 100%; height: 340rpx; background-color: #f5f5f5; }
.product-info { padding: 20rpx; }
.product-name { font-size: 26rpx; line-height: 1.4; height: 70rpx; }
.price { color: #e54d42; font-size: 32rpx; font-weight: bold; }
</style>