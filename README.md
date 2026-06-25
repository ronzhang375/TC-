# 💐 鲜花彩票文创商城

本项目是一个完整的**鲜花衍生品文创商城系统**。为了便于原型展示、交互验证与跨端协作，我们在本地将包含"后台 Java Spring Boot 服务"、"Vue 管理后台系统"、"Uni-App C端小程序"的各分支代码，与专为交互联动的**"静态双端联动模拟器"**进行了整合，采用 Mono-Repo（单一主仓库）架构进行整体版本追踪。

---

## 📂 项目整体目录结构与职责

```text
彩票文创商城/
├── C端小程序模拟器.html        # 🟢 本期重点：C端小程序高保真 HTML 模拟器（包含奶油玫瑰粉主题、购物车置底对齐、全站纯色奶油白底色）
├── 运营管理后台.html          # 🔵 本期重点：运营管理后台高保真 HTML 模拟器（包含配置页 Tab 卡片化重构、大 Banner 可视化装修上传）
├── images/                   # 🖼️ 静态图片及 UI 资源包（包含商品的 Base64 及法式奶油壁纸等）
│
├── lottery-mall/             # ☕ 后端 Java Spring Boot 主工程（一期：包含基础架构、数据库配置及基础增删改查）
├── lottery-mall-dev/         # ☕ 后端 Java Spring Boot 核心开发分支（二期：包含退款申请/审核流转、微信支付模拟、佣金结算生成等核心业务）
├── lottery-mall-admin/       # 🎨 管理后台前端系统源码（基于 Vue 3 + Vite + Element Plus）
├── lottery-mall-app/         # 📱 C端小程序客户端源码（基于 Uni-App + Vue 3 跨端框架）
│
├── 数据库设计文档.md           # 📄 系统核心物理表结构及逻辑外键设计
├── 测试用例文档.md             # 📄 针对退款流、佣金账单及上下架的完整 QA 规程
└── 项目进度总览.md             # 📄 项目八大 Phase 开发路线图与实时状态清单
```

---

## 🚀 启动与演示指南

为了直观查看项目的业务流与视觉效果，本工程提供了开箱即用的静态双端联动模拟器，无需配置任何 Java、Database 或 Node 复杂环境即可一秒启动：

1. **双端并排打开**：
   - 用现代浏览器（如 Chrome、Edge）在桌面上并排拖开打开 [C端小程序模拟器.html](C端小程序模拟器.html) 与 [运营管理后台.html](运营管理后台.html)。
2. **重置初始化**：
   - 在运营后台右上角点击 **"🔄 重置演示数据"**，以确保数据管道初始化。


---

## 🛠️ 后端服务（lottery-mall）— 技术栈与启动

`lottery-mall/` 子工程为体彩衍生品商城小程序的后端服务，基于 Spring Boot 3.2 构建。

### 技术栈

- **框架**：Spring Boot 3.2
- **ORM**：MyBatis Plus 3.5
- **数据库**：MySQL 8.0
- **缓存**：Redis 7
- **API 文档**：Knife4j (Swagger)
- **构建工具**：Maven

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7+

### 开发环境启动

1. **创建数据库**
   ```sql
   CREATE DATABASE lottery_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. **修改配置**：修改 `lottery-mall/src/main/resources/application-dev.yml` 中的数据库连接信息。
3. **启动服务**：
   ```bash
   cd lottery-mall
   mvn spring-boot:run
   ```
4. **访问 API 文档**：http://localhost:8080/api/doc.html

### Docker 部署

```bash
cd lottery-mall
docker-compose up -d
```

### 后端模块说明

| 模块 | 说明 |
|------|------|
| user | 用户模块（登录、注册、微信授权） |
| product | 商品模块（商品、分类、规格、库存） |
| order | 订单模块（创建、支付、退款） |
| channel | 渠道商模块（入驻、佣金、二维码） |
| supplier | 供货商模块（接单、发货） |
| commission | 佣金模块（计算、结算） |
| address | 收货地址模块 |
| region | 地区运营模块 |
| logistics | 物流模块 |
| finance | 资金模块 |
| marketing | 营销模块（Banner、活动） |

### Git 提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档变更
style: 代码格式
refactor: 重构
test: 测试
chore: 构建/工具
```

---

## 📜 许可证

Private - 仅供内部使用
