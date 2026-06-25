<template>
  <el-card>
    <div class="toolbar">
      <el-select v-model="search.status" placeholder="订单状态" clearable style="width: 150px">
        <el-option label="待支付" :value="0" />
        <el-option label="待接单" :value="1" />
        <el-option label="待发货" :value="2" />
        <el-option label="配送中" :value="3" />
        <el-option label="已完成" :value="4" />
        <el-option label="已取消" :value="5" />
        <el-option label="已退款" :value="6" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="totalAmount" label="金额" width="120">
        <template #default="{ row }">¥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column prop="payAmount" label="实付" width="120">
        <template #default="{ row }">¥{{ row.payAmount }}</template>
      </el-table-column>
      <el-table-column prop="orderStatus" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusType(row.orderStatus)">{{ statusName(row.orderStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="onDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="search.pageNum" v-model:page-size="search.pageSize"
                   :total="total" layout="total, prev, pager, next"
                   @current-change="loadData" style="margin-top: 20px; text-align: right" />

    <el-drawer v-model="detailVisible" title="订单详情" size="50%">
      <div v-if="detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ detail.consigneeName }} {{ detail.consigneePhone }}</el-descriptions-item>
          <el-descriptions-item label="地址">{{ detail.fullAddress }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ detail.payAmount }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ statusName(detail.orderStatus) }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ detail.createTime }}</el-descriptions-item>
        </el-descriptions>
        <h4>商品明细</h4>
        <el-table :data="detail.items || []" border>
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="specName" label="规格" />
          <el-table-column prop="price" label="单价" />
          <el-table-column prop="quantity" label="数量" />
          <el-table-column prop="totalAmount" label="小计" />
        </el-table>
      </div>
    </el-drawer>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getOrderList, getOrderDetail } from '@/api/order'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const search = reactive({ pageNum: 1, pageSize: 10, status: null })

const detailVisible = ref(false)
const detail = ref(null)

function statusName(s) { return ['待支付', '待接单', '待发货', '配送中', '已完成', '已取消', '已退款'][s] || '' }
function statusType(s) { return ['warning', 'primary', 'primary', 'primary', 'success', 'info', 'info'][s] || '' }

async function loadData() {
  loading.value = true
  try {
    const data = await getOrderList(search)
    list.value = data.rows || []
    total.value = data.total || 0
  } finally { loading.value = false }
}

async function onDetail(row) {
  detail.value = await getOrderDetail(row.id)
  detailVisible.value = true
}

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>