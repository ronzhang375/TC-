<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background: #1890ff"><el-icon><Goods /></el-icon></div>
            <div>
              <div class="stat-label">商品总数</div>
              <div class="stat-value">{{ stats.productCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background: #52c41a"><el-icon><List /></el-icon></div>
            <div>
              <div class="stat-label">订单总数</div>
              <div class="stat-value">{{ stats.orderCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background: #faad14"><el-icon><Money /></el-icon></div>
            <div>
              <div class="stat-label">总销售额</div>
              <div class="stat-value">¥{{ stats.totalAmount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-card">
            <div class="stat-icon" style="background: #f5222d"><el-icon><Connection /></el-icon></div>
            <div>
              <div class="stat-label">渠道商</div>
              <div class="stat-value">{{ stats.channelCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>系统说明</span>
      </template>
      <p>欢迎使用体彩衍生品商城管理后台。本系统提供商品、订单、渠道商、供货商、佣金结算等完整的管理功能。</p>
      <p>技术栈：Vue3 + Element Plus + Vite</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const stats = ref({
  productCount: 0,
  orderCount: 0,
  totalAmount: 0,
  channelCount: 0
})
</script>

<style lang="scss" scoped>
.stat-card { display: flex; align-items: center; gap: 20px; }
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28px;
}
.stat-label { color: #999; font-size: 14px; }
.stat-value { font-size: 24px; font-weight: bold; margin-top: 4px; }
</style>