<template>
  <el-card>
    <div class="toolbar">
      <el-button type="success" @click="onAdd">新增供货商</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="supplierName" label="供货商名称" min-width="200" />
      <el-table-column prop="supplierCode" label="编码" width="150" />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="contactPhone" label="电话" width="120" />
      <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="regionId" label="地区ID" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'info'">{{ row.status === 0 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="onToggle(row)">{{ row.status === 0 ? '禁用' : '启用' }}</el-button>
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSupplierList, addSupplier, updateSupplier, updateSupplierStatus } from '@/api/supplier'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, supplierName: '', supplierCode: '', contactName: '', contactPhone: '', address: '', regionId: 1, status: 0 })

async function loadData() {
  loading.value = true
  try { list.value = await getSupplierList() || [] }
  finally { loading.value = false }
}

function onAdd() { isEdit.value = false; Object.assign(form, { id: null, supplierName: '', supplierCode: '', contactName: '', contactPhone: '', address: '', regionId: 1, status: 0 }); dialogVisible.value = true }
function onEdit(row) { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true }
async function onSubmit() { if (isEdit.value) await updateSupplier(form.id, form); else await addSupplier(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
async function onToggle(row) { await updateSupplierStatus(row.id, row.status === 0 ? 1 : 0); ElMessage.success('操作成功'); loadData() }

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>