import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/layouts/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/Index.vue'), meta: { title: '工作台' } },
      { path: 'product/list', name: 'ProductList', component: () => import('@/views/product/List.vue'), meta: { title: '商品管理' } },
      { path: 'product/category', name: 'CategoryList', component: () => import('@/views/product/Category.vue'), meta: { title: '分类管理' } },
      { path: 'order/list', name: 'OrderList', component: () => import('@/views/order/List.vue'), meta: { title: '订单管理' } },
      { path: 'order/refund', name: 'RefundList', component: () => import('@/views/order/Refund.vue'), meta: { title: '退款审核' } },
      { path: 'channel/list', name: 'ChannelList', component: () => import('@/views/channel/List.vue'), meta: { title: '渠道商管理' } },
      { path: 'supplier/list', name: 'SupplierList', component: () => import('@/views/supplier/List.vue'), meta: { title: '供货商管理' } },
      { path: 'bill/list', name: 'BillList', component: () => import('@/views/bill/List.vue'), meta: { title: '佣金账单' } },
      { path: 'region/list', name: 'RegionList', component: () => import('@/views/region/List.vue'), meta: { title: '地区管理' } },
      { path: 'banner/list', name: 'BannerList', component: () => import('@/views/banner/List.vue'), meta: { title: 'Banner管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('admin_token')
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router