<template>
  <view class="order-list-page">
    <view class="tab-bar">
      <view v-for="tab in tabs" :key="tab.value" class="tab-item" :class="{ active: currentTab === tab.value }" @click="switchTab(tab.value)">{{ tab.label }}</view>
    </view>
    <view class="order-list">
      <view v-if="orders.length === 0 && !loading" class="empty"><u-empty text="暂无订单" /></view>
      <view v-for="order in orders" :key="order.id" class="order-card" @click="goDetail(order)">
        <view class="order-header">
          <text class="order-no">订单号：{{ order.orderNo }}</text>
          <text class="order-status" :class="`status-${order.orderStatus}`">{{ getStatusName(order.orderStatus) }}</text>
        </view>
        <view class="order-time">{{ order.createTime }}</view>
        <view class="order-actions">
          <view v-if="order.orderStatus === 0" class="action-btn" @click.stop="cancelOrder(order)">取消订单</view>
          <view v-if="order.orderStatus === 0" class="action-btn primary" @click.stop="payOrder(order)">去支付</view>
          <view v-if="order.orderStatus === 3" class="action-btn primary" @click.stop="confirmReceive(order)">确认收货</view>
          <view v-if="order.orderStatus >= 2 && order.orderStatus < 5" class="action-btn" @click.stop="refundOrder(order)">申请退款</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getOrderList, cancelOrder as cancelOrderApi, confirmReceive as confirmReceiveApi } from '../../api/order'

const tabs = [
  { label: '全部', value: null },
  { label: '待支付', value: 0 },
  { label: '待接单', value: 1 },
  { label: '待发货', value: 2 },
  { label: '配送中', value: 3 },
  { label: '已完成', value: 4 }
]

const currentTab = ref(null)
const orders = ref([])
const loading = ref(false)

onMounted(() => loadOrders())
onShow(() => loadOrders())

async function loadOrders() {
  loading.value = true
  try {
    const res = await getOrderList(currentTab.value, 1, 20)
    if (res.code === 200) orders.value = res.data.rows || []
  } finally { loading.value = false }
}

function switchTab(value) { currentTab.value = value; loadOrders() }
function getStatusName(status) { return ['待支付', '待接单', '待发货', '配送中', '已完成', '已取消', '已退款'][status] || '未知' }
function goDetail(order) { uni.navigateTo({ url: `/pages/order/detail?id=${order.id}` }) }
async function cancelOrder(order) {
  uni.showModal({ title: '提示', content: '确定取消订单？', success: async (res) => {
    if (res.confirm) { await cancelOrderApi(order.id); loadOrders() }
  }})
}
function payOrder(order) { uni.navigateTo({ url: `/pages/pay/index?orderId=${order.id}` }) }
async function confirmReceive(order) {
  uni.showModal({ title: '提示', content: '确认收货？', success: async (res) => {
    if (res.confirm) { await confirmReceiveApi(order.id); loadOrders() }
  }})
}
function refundOrder(order) { uni.navigateTo({ url: `/pages/order/refund?orderId=${order.id}` }) }
</script>

<style lang="scss" scoped>
.order-list-page { min-height: 100vh; background-color: #f5f5f5; }
.tab-bar { background-color: #ffffff; display: flex; position: sticky; top: 0; z-index: 10; }
.tab-item { flex: 1; padding: 24rpx 0; text-align: center; font-size: 28rpx; color: #666; }
.tab-item.active { color: #e54d42; font-weight: bold; border-bottom: 4rpx solid #e54d42; }
.order-list { padding: 20rpx; }
.order-card { background-color: #ffffff; border-radius: 12rpx; padding: 30rpx 20rpx; margin-bottom: 20rpx; }
.order-header { display: flex; justify-content: space-between; padding-bottom: 20rpx; border-bottom: 1rpx solid #f5f5f5; }
.order-no { font-size: 26rpx; color: #666; }
.order-status { font-size: 28rpx; font-weight: bold; }
.status-0 { color: #ff9900; }
.status-1, .status-2, .status-3 { color: #1890ff; }
.status-4 { color: #52c41a; }
.status-5, .status-6 { color: #999; }
.order-time { font-size: 24rpx; color: #999; padding: 20rpx 0; }
.order-actions { display: flex; justify-content: flex-end; gap: 16rpx; }
.action-btn { padding: 12rpx 30rpx; border: 1rpx solid #ddd; border-radius: 32rpx; font-size: 26rpx; }
.action-btn.primary { background-color: #e54d42; color: #ffffff; border-color: #e54d42; }
.empty { padding: 200rpx 0; text-align: center; }
</style>