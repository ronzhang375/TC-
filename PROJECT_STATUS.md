# 体彩衍生品商城小程序 - 项目进度记录

**最后更新**：2026-06-18
**项目状态**：核心功能开发完成，待联调测试

---

## 📁 项目仓库

**GitHub**：https://github.com/ronzhang375/TC-
**本地目录**：`D:\AI claude\彩票文创商城\lottery-mall-dev`

---

## 📋 已完成的功能模块

### Phase 1：基础模块（C端）
| 模块 | 文件 | 说明 |
|------|------|------|
| 用户 | `AppUserController`, `AppUserService` | 微信登录、JWT Token |
| 商品 | `AppProductController`, `ProductService` | 商品列表、详情、规格 |
| 分类 | `CategoryController`, `CategoryService` | 树形分类 |
| 收货地址 | `AppAddressController`, `AddressService` | CRUD、默认地址 |

### Phase 2：交易模块
| 模块 | 文件 | 说明 |
|------|------|------|
| 购物车 | `AppCartController`, `CartService` | 加购、合并下单 |
| 订单 | `AppOrderController`, `OrderService` | 创建、支付、取消、确认收货 |
| 订单状态 | — | 待支付→待接单→待发货→配送中→已完成 |

### Phase 3：履约模块（供货商）
| 模块 | 文件 | 说明 |
|------|------|------|
| 供货商 | `SupplierController`, `SupplierService` | 登录、接单、发货 |
| 物流 | `LogisticsService` | 创建物流、发货、签收 |

### Phase 4：渠道佣金模块
| 模块 | 文件 | 说明 |
|------|------|------|
| 渠道商 | `ChannelController`, `ChannelCommissionService` | 订单查询、佣金汇总 |
| 佣金 | `ChannelCommissionService` | 佣金计算（订单完成后计算） |

### Phase 5：运营后台
| 模块 | 文件 | 说明 |
|------|------|------|
| 商品管理 | `AdminProductController` | CRUD、上下架、规格管理 |
| 分类管理 | `AdminCategoryController` | CRUD |
| 订单管理 | `AdminOrderController` | 分页查询、详情 |
| 渠道商管理 | `AdminChannelController` | CRUD、启用禁用、二维码 |
| 供货商管理 | `AdminSupplierController` | CRUD、启用禁用 |
| 地区管理 | `AdminRegionController` | CRUD |
| Banner管理 | `AdminBannerController` | CRUD、启用禁用 |

---

## 📂 项目结构

```
lottery-mall-dev/
├── src/main/java/com/lottery/mall/
│   ├── LotteryMallApplication.java          # 启动类
│   ├── config/                             # 配置类
│   │   ├── WebConfig.java                 # CORS、静态资源
│   │   ├── RedisConfig.java              # Redis序列化
│   │   ├── JacksonConfig.java            # JSON配置
│   │   └── MybatisPlusConfig.java       # 分页、自动填充
│   ├── common/                           # 公共模块
│   │   ├── core/PageRequest.java         # 分页请求
│   │   ├── exception/                    # 异常处理
│   │   ├── result/                      # 统一响应
│   │   └── util/                        # 工具类（JwtUtils, DateUtils, SecurityUtils）
│   ├── module/
│   │   ├── admin/controller/            # 运营后台（7个Controller）
│   │   ├── address/                    # 收货地址
│   │   ├── cart/                       # 购物车
│   │   ├── channel/                    # 渠道商
│   │   ├── commission/                  # 佣金
│   │   ├── logistics/                   # 物流
│   │   ├── marketing/                   # Banner
│   │   ├── order/                      # 订单
│   │   ├── product/                    # 商品
│   │   ├── region/                     # 地区
│   │   ├── supplier/                   # 供货商
│   │   └── user/                       # 用户
│   └── generator/                      # 代码生成基础
├── sql/
│   └── init.sql                        # 数据库初始化脚本（32张表）
├── docker/
│   ├── Dockerfile                      # Docker构建
│   └── docker-compose.yml              # MySQL+Redis+App
├── pom.xml                             # Maven配置
└── README.md                          # 项目说明
```

---

## 🗄️ 数据库

**脚本位置**：`sql/init.sql`
**表数量**：32张
**初始化内容**：
- 系统用户（admin/admin123）
- 角色（超管、运营、供货商、渠道商）
- 字典数据（订单状态、支付状态、渠道类型等）
- 默认地区（福州、仓山、莆田）
- 默认分类（鲜花、玫瑰、康乃馨）

---

## 🔌 API 接口

**文档地址**：启动服务后访问 `http://localhost:8080/api/doc.html`

### C端接口（/app/）
| 接口 | 说明 |
|------|------|
| POST /app/user/wx-login | 微信登录 |
| GET /app/user/info | 获取用户信息 |
| GET /app/product/list | 商品列表 |
| GET /app/product/{id} | 商品详情 |
| GET /app/category/tree | 分类树 |
| GET /app/address/list | 地址列表 |
| POST /app/address | 新增地址 |
| POST /app/cart/add | 加入购物车 |
| GET /app/cart/list | 购物车列表 |
| POST /app/order/create | 创建订单 |
| GET /app/order/list | 订单列表 |
| PUT /app/order/{id}/cancel | 取消订单 |
| PUT /app/order/{id}/receive | 确认收货 |

### 供货商接口（/supplier/）
| 接口 | 说明 |
|------|------|
| POST /supplier/login | 供货商登录 |
| GET /supplier/order/pending | 待接单列表 |
| PUT /supplier/order/{id}/accept | 接单 |
| PUT /supplier/order/{id}/ship | 发货 |

### 渠道商接口（/channel/）
| 接口 | 说明 |
|------|------|
| GET /channel/order/list | 我的订单 |
| GET /channel/commission/summary | 佣金汇总 |

### 运营后台接口（/admin/）
| 接口 | 说明 |
|------|------|
| GET /admin/product/list | 商品管理 |
| GET /admin/order/list | 订单管理 |
| GET /admin/channel/list | 渠道商管理 |
| GET /admin/supplier/list | 供货商管理 |
| GET /admin/banner/list | Banner管理 |

---

## ⚠️ 待完善内容

### 高优先级
1. **微信支付对接** - `WxPayController` 需实现 JSAPI 统一下单和回调
2. **JWT Token 中间件** - 接口鉴权拦截器
3. **数据库表** - `cart_item` 购物车表缺失建表SQL
4. **商品表补充字段** - `product_info` 缺少部分字段

### 中优先级
5. **退款功能** - 退款申请、审核、退款流程
6. **佣金结算** - 账单生成、定时任务、周结/月结合
7. **微信公众号对接** - 模板消息通知
8. **二维码生成** - 渠道商二维码生成逻辑

### 低优先级
9. **报表功能** - 销售报表、渠道推广报表
10. **活动管理** - 优惠券、满减活动
11. **会员体系** - 等级、积分

---

## 🚀 本地启动

```bash
# 1. 克隆项目
git clone https://github.com/ronzhang375/TC-.git
cd lottery-mall-dev

# 2. 初始化数据库
mysql -u root -p < sql/init.sql

# 3. 修改配置
# 编辑 src/main/resources/application-dev.yml
# 修改数据库连接、Redis配置

# 4. 启动服务
mvn spring-boot:run

# 5. 访问 API 文档
http://localhost:8080/api/doc.html
```

---

## 🐳 Docker 启动

```bash
cd docker
docker-compose up -d
```

---

## 📝 Git 提交记录

```
commit 7345b61 - feat: Phase 5 运营后台模块
commit 7ead260 - feat: Phase 4 渠道商佣金模块
commit a3808cc - feat: Phase 3 供货商履约模块
commit b1546af - feat: Phase 2 购物车和订单模块
commit 8746a14 - feat: Phase 1 基础模块开发
commit acbed55 - feat: 添加数据库初始化脚本
commit dbc3c02 - feat: 初始化项目骨架
```

---

## 🔄 继续开发命令

```bash
# 拉取最新代码
git clone https://github.com/ronzhang375/TC-.git lottery-mall-dev
cd lottery-mall-dev

# 查看当前进度
cat PROJECT_STATUS.md

# 查看待办
cat TODO.md
```

---

## 📞 文档位置

| 文档 | 路径 |
|------|------|
| 本进度记录 | `lottery-mall-dev/PROJECT_STATUS.md` |
| 产品需求文档 | `..\体彩衍生品商城小程序产品需求文档_V2.0.docx` |
| 开发规划 | `..\体彩衍生品商城小程序-开发规划.docx` |
| 前端开发提示词 | `C:\Users\ron37\.claude\plans\体彩衍生品商城小程序-前端开发提示词.md` |
| 数据库设计 | `..\数据库设计文档.md` |
