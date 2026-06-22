<template>
  <el-card>
    <div class="toolbar">
      <el-input v-model="search.keyword" placeholder="商品名称" style="width: 200px" clearable />
      <el-select v-model="search.categoryId" placeholder="分类" clearable style="width: 150px">
        <el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
      <el-button type="success" @click="onAdd">新增商品</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="图片" width="100">
        <template #default="{ row }">
          <el-image :src="(row.images || '').split(',')[0] || ''" style="width: 60px; height: 60px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="productName" label="商品名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="minPrice" label="最低价" width="100">
        <template #default="{ row }">¥{{ row.minPrice }}</template>
      </el-table-column>
      <el-table-column prop="maxPrice" label="最高价" width="100">
        <template #default="{ row }">¥{{ row.maxPrice }}</template>
      </el-table-column>
      <el-table-column prop="salesCount" label="销量" width="80" />
      <el-table-column prop="isOnSale" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isOnSale === 1 ? 'success' : 'info'">
            {{ row.isOnSale === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="onEditSpecs(row)">规格</el-button>
          <el-button link type="primary" @click="onToggleSale(row)">
            {{ row.isOnSale === 1 ? '下架' : '上架' }}
          </el-button>
          <el-button link type="primary" @click="onEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="search.pageNum" v-model:page-size="search.pageSize"
                   :total="total" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next"
                   @size-change="loadData" @current-change="loadData" style="margin-top: 20px; text-align: right" />

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '新增商品'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称"><el-input v-model="form.productName" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 100%">
            <el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="最低价"><el-input-number v-model="form.minPrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="最高价"><el-input-number v-model="form.maxPrice" :min="0" :precision="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 规格对话框 -->
    <el-dialog v-model="specDialogVisible" title="商品规格" width="700px">
      <el-table :data="specs" border>
        <el-table-column prop="specName" label="规格名称" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="specSort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, addProduct, updateProduct, deleteProduct, updateSaleStatus, getProductSpecs } from '@/api/product'
import { getCategoryTree } from '@/api/category'

const list = ref([])
const categories = ref([])
const total = ref(0)
const loading = ref(false)

const search = reactive({ pageNum: 1, pageSize: 10, keyword: '', categoryId: null })

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, productName: '', categoryId: null, description: '', minPrice: 0, maxPrice: 0 })

const specDialogVisible = ref(false)
const specs = ref([])

async function loadData() {
  loading.value = true
  try {
    const data = await getProductList(search)
    list.value = data.rows || []
    total.value = data.total || 0
  } finally { loading.value = false }
}

async function loadCategories() {
  const data = await getCategoryTree()
  categories.value = data || []
}

function onAdd() {
  isEdit.value = false
  Object.assign(form, { id: null, productName: '', categoryId: null, description: '', minPrice: 0, maxPrice: 0 })
  dialogVisible.value = true
}

function onEdit(row) {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

async function onSubmit() {
  if (isEdit.value) {
    await updateProduct(form.id, form)
  } else {
    await addProduct(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.productName}」？`, '提示')
  await deleteProduct(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function onToggleSale(row) {
  const status = row.isOnSale === 1 ? 0 : 1
  await updateSaleStatus(row.id, status)
  ElMessage.success('操作成功')
  loadData()
}

async function onEditSpecs(row) {
  const data = await getProductSpecs(row.id)
  specs.value = data || []
  specDialogVisible.value = true
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style lang="scss" scoped>
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; }
</style>