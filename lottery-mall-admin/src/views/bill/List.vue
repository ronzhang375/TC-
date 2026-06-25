<template>
  <el-card>
    <div class="toolbar">
      <el-select v-model="search.status" placeholder="账单状态" clearable style="width: 150px">
        <el-option label="待确认" :value="0" />
        <el-option label="待结算" :value="1" />
        <el-option label="已打款" :value="3" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="success" @click="onGenerateWeekly">生成周结</el-button>
      <el-button type="success" @click="onGenerateMonthly">生成月结</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="billNo" label="账单编号" width="180" />
      <el-table-column prop="channelName" label="渠道商" min-width="150" />
      <el-table-column prop="settlementType" label="周期" width="100">
        <template #default="{ row }">
          <el-tag :type="row.settlementType === 'week' ? 'primary' : 'success'">
            {{ row.settlementType === 'week' ? '周结' : '月结' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="periodStart" label="开始日期" width="120" />
      <el-table-column prop="periodEnd" label="结束日期" width="120" />
      <el-table-column prop="orderCount" label="订单数" width="100" />
      <el-table-column prop="orderAmount" label="订单金额" width="120">
        <template #default="{ row }">¥{{ row.orderAmount }}</template>
      </el-table-column>
      <el-table-column prop="commissionAmount" label="佣金金额" width="120">
        <template #default="{ row }">¥{{ row.commissionAmount }}</template>
      </el-table-column>
      <el-table-column prop="billStatus" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.billStatus)">{{ statusName(row.billStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="onDetail(row)">详情</el-button>
          <el-button v-if="row.billStatus === 0" link type="success" @click="onConfirm(row)">确认</el-button>
          <el-button v-if="row.billStatus === 1" link type="warning" @click="onSettle(row)">结算</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="search.pageNum" v-model:page-size="search.pageSize"
                   :total="total" layout="total, prev, pager, next"
                   @current-change="loadData" style="margin-top: 20px; text-align: right" />

    <el-drawer v-model="detailVisible" title="账单详情" size="60%">
      <div v-if="detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="账单编号">{{ detail.billNo }}</el-descriptions-item>
          <el-descriptions-item label="渠道商">{{ detail.channelName }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ detail.orderAmount }}</el-descriptions-item>
          <el-descriptions-item label="佣金金额">¥{{ detail.commissionAmount }}</el-descriptions-item>
        </el-descriptions>
        <h4>订单明细</h4>
        <el-table :data="details" border>
          <el-table-column prop="orderNo" label="订单号" />
          <el-table-column prop="orderAmount" label="金额">
            <template #default="{ row }">¥{{ row.orderAmount }}</template>
          </el-table-column>
          <el-table-column prop="commissionAmount" label="佣金">
            <template #default="{ row }">¥{{ row.commissionAmount }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBillList, getBillDetail, getBillDetails, confirmBill, settleBill, generateWeeklyBills, generateMonthlyBills } from '@/api/bill'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const search = reactive({ pageNum: 1, pageSize: 10, status: null })

const detailVisible = ref(false)
const detail = ref(null)
const details = ref([])

function statusName(s) { return ['待确认', '待结算', '已结算', '已打款'][s] || '' }
function statusType(s) { return ['warning', 'primary', 'success', 'success'][s] || '' }

async function loadData() {
  loading.value = true
  try {
    const data = await getBillList(search)
    list.value = data.rows || []
    total.value = data.total || 0
  } finally { loading.value = false }
}

async function onDetail(row) {
  detail.value = await getBillDetail(row.id)
  details.value = await getBillDetails(row.id)
  detailVisible.value = true
}

async function onConfirm(row) {
  await confirmBill(row.id)
  ElMessage.success('已确认')
  loadData()
}

async function onSettle(row) {
  await ElMessageBox.confirm('确定结算该账单？', '提示', { type: 'warning' })
  await settleBill(row.id)
  ElMessage.success('已结算')
  loadData()
}

async function onGenerateWeekly() {
  await generateWeeklyBills()
  ElMessage.success('周结账单生成完成')
  loadData()
}

async function onGenerateMonthly() {
  await generateMonthlyBills()
  ElMessage.success('月结账单生成完成')
  loadData()
}

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>