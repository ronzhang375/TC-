<template>
  <el-card>
    <div class="toolbar">
      <el-button type="success" @click="onAdd">新增地区</el-button>
    </div>

    <el-table :data="list" border v-loading="loading" row-key="id">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="regionName" label="地区名称" min-width="200" />
      <el-table-column prop="regionCode" label="编码" width="150" />
      <el-table-column prop="orderNum" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'info'">{{ row.status === 0 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑地区' : '新增地区'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="地区名称"><el-input v-model="form.regionName" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="form.regionCode" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRegionList, addRegion, updateRegion, deleteRegion } from '@/api/region'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, regionName: '', regionCode: '', parentId: 0, orderNum: 0, status: 0 })

async function loadData() {
  loading.value = true
  try { list.value = await getRegionList() || [] }
  finally { loading.value = false }
}

function onAdd() { isEdit.value = false; Object.assign(form, { id: null, regionName: '', regionCode: '', parentId: 0, orderNum: 0, status: 0 }); dialogVisible.value = true }
function onEdit(row) { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true }
async function onSubmit() { if (isEdit.value) await updateRegion(form.id, form); else await addRegion(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
async function onDelete(row) { await ElMessageBox.confirm(`确定删除「${row.regionName}」？`, '提示'); await deleteRegion(row.id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>