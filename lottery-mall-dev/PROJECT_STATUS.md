# 体彩衍生品商城小程序 - 项目进度记录

**最后更新**：2026-06-22
**项目状态**：✅ 三个端全部完成，已推送到 GitHub

---

## 📁 GitHub 仓库

**仓库地址**：https://github.com/ronzhang375/TC-

### 分支说明

| 分支 | 内容 | 说明 |
|------|------|------|
| `main` | 后端 SpringBoot 代码 | Java + SpringBoot 3 + MyBatis Plus |
| `lottery-app` | C 端小程序 | UniApp + Vue3 + uView Plus |
| `admin` | 管理后台 | Vue3 + Element Plus + Vite |

---

## ✅ 已完成的功能模块

### 🔧 后端（main 分支）— 8 个 Phase

| Phase | 模块 | 说明 |
|-------|------|------|
| Phase 1 | 用户、商品、收货地址 | 微信登录、JWT、商品规格 |
| Phase 2 | 购物车、订单 | 创建订单、状态机、合并下单 |
| Phase 3 | 供货商履约、物流 | 接单、发货、物流跟踪 |
| Phase 4 | 渠道商、佣金 | 订单查询、佣金汇总 |
| Phase 5 | 运营后台 | 7 个管理 Controller |
| Phase 6 | JWT 鉴权、微信支付 | 拦截器、支付对接 |
| Phase 7 | 退款功能 | 申请、审核、退款完成 |
| Phase 8 | 佣金结算 | 周结/月结账单、结算打款 |

**后端代码量**：约 70+ Java 文件，32 张数据库表

---

### 📱 C 端小程序（lottery-app 分支）— 13 个页面

| 页面 | 功能 |
|------|------|
| pages/index/index | 首页（Banner/分类/推荐） |
| pages/product/list | 商品列表（搜索/筛选/排序） |
| pages/product/detail | 商品详情（含规格选择） |
| pages/cart/index | 购物车 |
| pages/order/confirm | 确认订单 |
| pages/order/list | 订单列表 |
| pages/order/detail | 订单详情 |
| pages/order/refund | 退款申请 |
| pages/pay/index | 微信支付 |
| pages/address/list | 收货地址 |
| pages/address/edit | 地址编辑（省市区） |
| pages/user/index | 个人中心 |
| pages/user/login | 微信授权登录 |

**技术栈**：UniApp + Vue3 + Pinia + uView Plus

---

### 🎨 管理后台（admin 分支）— 11 个页面

| 页面 | 功能 |
|------|------|
| Login | 管理员登录 |
| Dashboard | 工作台（数据统计） |
| Product/List | 商品管理（CRUD、规格、上下架） |
| Product/Category | 分类管理（树形） |
| Order/List | 订单管理 |
| Order/Refund | 退款审核 |
| Channel/List | 渠道商管理（含二维码生成） |
| Supplier/List | 供货商管理 |
| Bill/List | 佣金账单（含周结/月结生成） |
| Region/List | 地区管理 |
| Banner/List | Banner 管理 |

**技术栈**：Vue3 + Element Plus + Vite + Pinia

---

## 📂 本地目录结构

```
D:\AI claude\彩票文创商城\
├── lottery-mall-dev/         # 后端代码（已推送 main）
├── lottery-mall-app/         # 小程序代码（已推送 lottery-app）
├── lottery-mall-admin/       # 管理后台（已推送 admin）
├── node_modules/
├── generate_*.js              # Word 文档生成脚本
├── generate_*.py              # 页面生成脚本
├── package-lock.json
├── 体彩衍生品商城小程序产品需求文档_V2.0.docx
├── 体彩衍生品商城小程序-开发规划.docx
└── 数据库设计文档.md
```

---

## 🔌 后端 API 接口汇总

### C端（/app/）
- 用户：登录、地址管理
- 商品：列表、详情、分类
- 购物车：增删改查
- 订单：创建、支付、取消、确认收货、退款
- 支付：统一下单、回调

### 供货商（/supplier/）
- 登录、待接单、接单、发货

### 渠道商（/channel/）
- 登录、订单查询、佣金汇总、账单

### 管理后台（/admin/）
- 商品、分类、订单、退款、渠道商、供货商、账单、地区、Banner 管理

---

## ⚠️ 待完善内容

### 高优先级（部署前）
1. 微信支付真实接入（替换 mock 数据）
2. 微信小程序账号配置
3. 微信支付商户号配置
4. 阿里云 OSS 配置（图片存储）
5. 域名 + HTTPS 证书

### 中优先级（功能完善）
6. 管理员真实登录接口（当前是 mock）
7. 数据权限控制（按地区隔离）
8. 操作日志记录
9. 报表系统（销售报表、推广报表）

### 低优先级（二期）
10. 优惠券/活动管理
11. 会员体系（等级、积分）
12. 电子刮刮乐体验
13. 公众号模板消息

---

## 🚀 部署清单

### 后端
- JDK 17
- MySQL 8.0（执行 `sql/init.sql`）
- Redis 7
- Maven 构建：`mvn clean package`
- 启动：`java -jar target/lottery-mall-1.0.0-SNAPSHOT.jar`
- Docker：`docker-compose up -d`（包含 MySQL + Redis）

### 小程序
- HBuilderX 打开 `lottery-mall-app/`
- 运行 → 运行到小程序模拟器 → 微信开发者工具
- 修改 `src/utils/request.js` 中的 BASE_URL

### 管理后台
- 进入 `lottery-mall-admin/`
- `npm install`
- `npm run dev` → 访问 `http://localhost:8081`
- 后端代理已配置（`/api → http://localhost:8080`）

---

## 📊 项目统计

| 项目 | 数量 |
|------|------|
| Java 后端类文件 | 70+ |
| 数据库表 | 32 张 |
| 小程序 Vue 页面 | 13 个 |
| 管理后台 Vue 页面 | 11 个 |
| API 接口模块（前后端） | 30+ |
| Git 提交次数（后端） | 10+ |

---

## 📝 后续开发命令

```bash
# 克隆项目
git clone https://github.com/ronzhang375/TC-.git
cd TC-

# 查看后端代码
git checkout main

# 查看小程序
git checkout lottery-app

# 查看管理后台
git checkout admin
```

---

## 🎉 项目总结

```
需求阶段 ✅  PRD V2.0
  ↓
设计阶段 ✅  数据库设计（32表） + 技术选型
  ↓
后端开发 ✅  8 个 Phase，70+ 文件
  ↓
小程序 ✅  13 个页面，完整交易闭环
  ↓
管理后台 ✅  11 个页面，完整运营管理
  ↓
待部署 ⏳  微信支付、域名、备案
```

整个项目从需求梳理到三个端代码实现，**全部完成并推送到 GitHub**！

---

**下次继续任务时，直接告诉我「继续开发」即可，我会从最新进度开始！**