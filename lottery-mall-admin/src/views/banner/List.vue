<template>
  <el-card>
    <div class="toolbar">
      <el-button type="success" @click="onAdd">新增Banner</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image :src="row.bannerImage" style="width: 100px; height: 50px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="bannerTitle" label="标题" min-width="200" />
      <el-table-column prop="regionId" label="地区ID" width="100" />
      <el-table-column prop="orderNum" label="排序" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑Banner' : '新增Banner'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题"><el-input v-model="form.bannerTitle" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="form.bannerImage" /></el-form-item>
        <el-form-item label="跳转链接"><el-input v-model="form.bannerLink" /></el-form-item>
        <el-form-item label="地区ID"><el-input-number v-model="form.regionId" :min="0" /></el-form-item>
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
import { getBannerList, addBanner, updateBanner, deleteBanner } from '@/api/banner'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, bannerTitle: '', bannerImage: '', bannerLink: '', regionId: 0, orderNum: 0, status: 1 })

async function loadData() {
  loading.value = true
  try { list.value = await getBannerList() || [] }
  finally { loading.value = false }
}

function onAdd() { isEdit.value = false; Object.assign(form, { id: null, bannerTitle: '', bannerImage: '', bannerLink: '', regionId: 0, orderNum: 0, status: 1 }); dialogVisible.value = true }
function onEdit(row) { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true }
async function onSubmit() { if (isEdit.value) await updateBanner(form.id, form); else await addBanner(form); ElMessage.success('保存成功'); dialogVisible.value = false; loadData() }
async function onDelete(row) { await ElMessageBox.confirm('确定删除？', '提示'); await deleteBanner(row.id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>