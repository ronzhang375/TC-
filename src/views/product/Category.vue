<template>
  <el-card>
    <div class="toolbar">
      <el-button type="success" @click="onAdd(null)">新增分类</el-button>
    </div>

    <el-table :data="categories" row-key="id" :tree-props="{ children: 'children' }" border v-loading="loading">
      <el-table-column prop="categoryName" label="分类名称" min-width="200" />
      <el-table-column prop="orderNum" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'info'">
            {{ row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" @click="onAdd(row.id)">新增子分类</el-button>
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="分类名称"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.orderNum" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">启用</el-radio>
            <el-radio :value="1">禁用</el-radio>
          </el-radio-group>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryTree, addCategory, updateCategory, deleteCategory } from '@/api/category'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, categoryName: '', parentId: 0, orderNum: 0, status: 0 })

async function loadData() {
  loading.value = true
  try {
    categories.value = await getCategoryTree() || []
  } finally { loading.value = false }
}

function onAdd(parentId) {
  isEdit.value = false
  Object.assign(form, { id: null, categoryName: '', parentId: parentId || 0, orderNum: 0, status: 0 })
  dialogVisible.value = true
}

function onEdit(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function onSubmit() {
  if (isEdit.value) await updateCategory(form.id, form)
  else await addCategory(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.categoryName}」？`, '提示')
  await deleteCategory(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>