<template>
  <el-card>
    <div class="toolbar">
      <el-select v-model="search.status" placeholder="退款状态" clearable style="width: 150px">
        <el-option label="待审核" :value="0" />
        <el-option label="审核通过" :value="1" />
        <el-option label="审核拒绝" :value="2" />
        <el-option label="退款中" :value="3" />
        <el-option label="已完成" :value="4" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="refundNo" label="退款单号" width="180" />
      <el-table-column prop="orderNo" label="原订单号" width="180" />
      <el-table-column prop="refundAmount" label="退款金额" width="120">
        <template #default="{ row }">¥{{ row.refundAmount }}</template>
      </el-table-column>
      <el-table-column prop="refundReason" label="退款原因" width="150" />
      <el-table-column prop="refundStatus" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="refundType(row.refundStatus)">{{ refundName(row.refundStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="180" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <template v-if="row.refundStatus === 0">
            <el-button link type="success" @click="onApprove(row)">通过</el-button>
            <el-button link type="danger" @click="onReject(row)">拒绝</el-button>
          </template>
          <template v-if="row.refundStatus === 1">
            <el-button link type="primary" @click="onComplete(row)">标记完成</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="search.pageNum" v-model:page-size="search.pageSize"
                   :total="total" layout="total, prev, pager, next"
                   @current-change="loadData" style="margin-top: 20px; text-align: right" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRefundList, approveRefund, rejectRefund, completeRefund } from '@/api/refund'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const search = reactive({ pageNum: 1, pageSize: 10, status: null })

function refundName(s) { return ['待审核', '审核通过', '审核拒绝', '退款中', '已完成'][s] || '' }
function refundType(s) { return ['warning', 'success', 'danger', 'primary', 'info'][s] || '' }

async function loadData() {
  loading.value = true
  try {
    const data = await getRefundList(search)
    list.value = data.rows || []
    total.value = data.total || 0
  } finally { loading.value = false }
}

async function onApprove(row) {
  await ElMessageBox.confirm('确定审核通过？', '提示')
  await approveRefund(row.id)
  ElMessage.success('已通过')
  loadData()
}

async function onReject(row) {
  const { value } = await ElMessageBox.prompt('请输入拒绝原因', '审核拒绝', { inputPattern: /.{1,200}/, inputErrorMessage: '请输入1-200字' })
  await rejectRefund(row.id, value)
  ElMessage.success('已拒绝')
  loadData()
}

async function onComplete(row) {
  await ElMessageBox.confirm('确定标记为退款完成？', '提示', { type: 'warning' })
  await completeRefund(row.id)
  ElMessage.success('操作成功')
  loadData()
}

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>