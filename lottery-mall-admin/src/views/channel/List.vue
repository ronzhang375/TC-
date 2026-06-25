<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="search.regionId" placeholder="地区ID" style="width: 150px" clearable />
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="success" @click="onAdd">新增渠道商</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="channelName" label="渠道商名称" min-width="200" />
      <el-table-column prop="channelCode" label="编码" width="150" />
      <el-table-column prop="channelType" label="类型" width="100" />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="contactPhone" label="电话" width="120" />
      <el-table-column prop="commissionRate" label="佣金比例" width="120">
        <template #default="{ row }">{{ (row.commissionRate * 100).toFixed(2) }}%</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'info'">
            {{ row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="onQrcode(row)">生成二维码</el-button>
          <el-button link type="primary" @click="onToggle(row)">
            {{ row.status === 0 ? '禁用' : '启用' }}
          </el-button>
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="search.pageNum" v-model:page-size="search.pageSize"
                   :total="total" layout="total, prev, pager, next"
                   @current-change="loadData" style="margin-top: 20px; text-align: right" />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑渠道商' : '新增渠道商'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.channelName" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="form.channelCode" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.channelType">
            <el-option label="门店" value="store" />
            <el-option label="商场" value="market" />
            <el-option label="社区" value="community" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contactName" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.contactPhone" /></el-form-item>
        <el-form-item label="地区ID"><el-input-number v-model="form.regionId" :min="1" /></el-form-item>
        <el-form-item label="佣金比例">
          <el-input-number v-model="form.commissionRate" :min="0" :max="1" :step="0.01" :precision="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getChannelList, addChannel, updateChannel, updateChannelStatus, generateQrCode } from '@/api/channel'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const search = reactive({ pageNum: 1, pageSize: 10, regionId: null })

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, channelName: '', channelCode: '', channelType: 'store', contactName: '', contactPhone: '', regionId: 1, commissionRate: 0.05 })

async function loadData() {
  loading.value = true
  try {
    const data = await getChannelList(search)
    list.value = data || []
    total.value = list.value.length
  } finally { loading.value = false }
}

function onAdd() {
  isEdit.value = false
  Object.assign(form, { id: null, channelName: '', channelCode: '', channelType: 'store', contactName: '', contactPhone: '', regionId: 1, commissionRate: 0.05 })
  dialogVisible.value = true
}

function onEdit(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function onSubmit() {
  if (isEdit.value) await updateChannel(form.id, form)
  else await addChannel(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function onToggle(row) {
  const status = row.status === 0 ? 1 : 0
  await updateChannelStatus(row.id, status)
  ElMessage.success('操作成功')
  loadData()
}

async function onQrcode(row) {
  const data = await generateQrCode(row.id)
  ElMessage.success('二维码已生成：' + data)
}

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>