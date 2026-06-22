<template>
  <view class="address-page">
    <view v-if="addresses.length === 0" class="empty"><u-empty text="暂无收货地址" /></view>
    <view v-for="item in addresses" :key="item.id" class="address-item">
      <view class="address-info">
        <view class="address-top">
          <text class="name">{{ item.consigneeName }}</text>
          <text class="phone">{{ item.consigneePhone }}</text>
          <text v-if="item.isDefault === 1" class="default-tag">默认</text>
        </view>
        <view class="address-detail">{{ item.provinceName }}{{ item.cityName }}{{ item.districtName }}{{ item.detailAddress }}</view>
      </view>
      <view class="address-actions">
        <view class="action-btn" @click.stop="editAddress(item)">编辑</view>
        <view class="action-btn" @click.stop="deleteAddress(item)">删除</view>
      </view>
    </view>
    <view class="add-btn" @click="addAddress">+ 新增地址</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getAddressList, deleteAddress } from '../../api/user'

const addresses = ref([])
onShow(() => loadAddresses())
async function loadAddresses() {
  const res = await getAddressList()
  if (res.code === 200) addresses.value = res.data || []
}
function addAddress() { uni.navigateTo({ url: '/pages/address/edit' }) }
function editAddress(item) { uni.navigateTo({ url: `/pages/address/edit?id=${item.id}` }) }
async function deleteAddress(item) {
  uni.showModal({ title: '提示', content: '确定删除？', success: async (res) => {
    if (res.confirm) { await deleteAddress(item.id); loadAddresses() }
  }})
}
</script>

<style lang="scss" scoped>
.address-page { min-height: 100vh; background-color: #f5f5f5; padding-bottom: 120rpx; }
.empty { padding: 200rpx 0; text-align: center; }
.address-item { background-color: #ffffff; margin: 20rpx; border-radius: 12rpx; padding: 30rpx 20rpx; }
.address-info { border-bottom: 1rpx solid #f5f5f5; padding-bottom: 20rpx; }
.address-top { display: flex; align-items: center; margin-bottom: 10rpx; }
.name { font-size: 32rpx; font-weight: bold; margin-right: 20rpx; }
.phone { font-size: 28rpx; color: #666; }
.default-tag { margin-left: 20rpx; padding: 4rpx 16rpx; background-color: #e54d42; color: #ffffff; border-radius: 6rpx; font-size: 22rpx; }
.address-detail { font-size: 26rpx; color: #666; }
.address-actions { display: flex; justify-content: flex-end; gap: 16rpx; padding-top: 20rpx; }
.action-btn { padding: 10rpx 24rpx; border: 1rpx solid #ddd; border-radius: 30rpx; font-size: 24rpx; color: #666; }
.add-btn { position: fixed; bottom: 30rpx; left: 30rpx; right: 30rpx; height: 90rpx; line-height: 90rpx; text-align: center; background-color: #e54d42; color: #ffffff; border-radius: 45rpx; font-size: 32rpx; }
</style>