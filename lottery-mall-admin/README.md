# 体彩衍生品商城 - 管理后台

## 技术栈

- **框架**：Vue 3
- **UI 库**：Element Plus
- **构建工具**：Vite
- **状态管理**：Pinia
- **路由**：Vue Router 4

## 功能模块

| 模块 | 路径 | 功能 |
|------|------|------|
| 登录 | /login | 管理员登录 |
| 工作台 | /dashboard | 数据统计概览 |
| 商品管理 | /product/list | 商品CRUD、上下架、规格管理 |
| 分类管理 | /product/category | 分类CRUD（树形） |
| 订单管理 | /order/list | 订单查询、详情查看 |
| 退款审核 | /order/refund | 退款申请审核 |
| 渠道商管理 | /channel/list | 渠道商CRUD、二维码生成 |
| 供货商管理 | /supplier/list | 供货商CRUD |
| 佣金账单 | /bill/list | 账单确认、结算、自动生成 |
| 地区管理 | /region/list | 地区CRUD |
| Banner管理 | /banner/list | BannerCRUD |

## 本地启动

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问 http://localhost:8081
```

## 目录结构

```
src/
├── api/           # API 接口（10个模块）
├── views/         # 页面（11个）
├── layouts/       # 布局
├── router/        # 路由配置
├── store/         # Pinia
├── utils/         # 工具类（请求封装）
├── App.vue
└── main.js
```

## 后端 API

开发环境通过 Vite 代理转发到 `http://localhost:8080/api`

## 默认账号

- 用户名：admin
- 密码：admin123