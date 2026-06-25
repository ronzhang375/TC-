<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="sidebar">
      <div class="logo">体彩衍生品商城</div>
      <el-menu :default-active="activeMenu" router background-color="#001529" text-color="#fff">
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>工作台</span>
        </el-menu-item>
        <el-sub-menu index="product">
          <template #title>
            <el-icon><Goods /></el-icon>
            <span>商品管理</span>
          </template>
          <el-menu-item index="/product/list">商品列表</el-menu-item>
          <el-menu-item index="/product/category">分类管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="order">
          <template #title>
            <el-icon><List /></el-icon>
            <span>订单管理</span>
          </template>
          <el-menu-item index="/order/list">订单列表</el-menu-item>
          <el-menu-item index="/order/refund">退款审核</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/channel/list">
          <el-icon><Connection /></el-icon>
          <span>渠道商管理</span>
        </el-menu-item>
        <el-menu-item index="/supplier/list">
          <el-icon><Avatar /></el-icon>
          <span>供货商管理</span>
        </el-menu-item>
        <el-menu-item index="/bill/list">
          <el-icon><Money /></el-icon>
          <span>佣金账单</span>
        </el-menu-item>
        <el-menu-item index="/region/list">
          <el-icon><Location /></el-icon>
          <span>地区管理</span>
        </el-menu-item>
        <el-menu-item index="/banner/list">
          <el-icon><Picture /></el-icon>
          <span>Banner管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部 -->
      <el-header class="header">
        <div class="breadcrumb">{{ currentTitle }}</div>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" :icon="UserFilled" />
            {{ userStore.userInfo.username || '管理员' }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!-- 内容 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta?.title || '')

function handleCommand(command) {
  if (command === 'logout') {
    ElMessageBox.confirm('确定退出登录？', '提示').then(() => {
      userStore.logout()
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style lang="scss" scoped>
.layout-container { height: 100vh; }
.sidebar {
  background-color: #001529;
  .logo {
    color: #fff;
    text-align: center;
    padding: 20px;
    font-size: 16px;
    font-weight: bold;
    border-bottom: 1px solid #1f2d3d;
  }
}
.header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  .breadcrumb { font-size: 16px; font-weight: bold; }
  .user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
}
.main { background-color: #f0f2f5; padding: 20px; }
</style>