# 体彩衍生品商城小程序 - 后端服务

## 项目简介

体彩衍生品商城小程序后端服务，基于 Spring Boot 3.2 构建。

## 技术栈

- **框架**：Spring Boot 3.2
- **ORM**：MyBatis Plus 3.5
- **数据库**：MySQL 8.0
- **缓存**：Redis 7
- **API文档**：Knife4j (Swagger)
- **构建工具**：Maven

## 项目结构

```
lottery-mall/
├── src/main/java/com/lottery/mall/
│   ├── config/           # 配置类
│   ├── common/           # 公共模块
│   │   ├── core/        # 核心类
│   │   ├── exception/    # 异常处理
│   │   ├── result/       # 统一响应
│   │   └── util/         # 工具类
│   └── module/           # 业务模块
│       ├── user/         # 用户模块
│       ├── product/      # 商品模块
│       ├── order/        # 订单模块
│       ├── channel/      # 渠道商模块
│       ├── supplier/     # 供货商模块
│       ├── commission/   # 佣金模块
│       ├── address/      # 收货地址模块
│       ├── region/       # 地区模块
│       ├── logistics/     # 物流模块
│       ├── finance/      # 资金模块
│       └── marketing/    # 营销模块
├── src/main/resources/
│   ├── mapper/           # MyBatis XML
│   ├── application.yml   # 应用配置
│   ├── application-dev.yml  # 开发环境
│   └── application-prod.yml # 生产环境
├── docker/
│   ├── Dockerfile
│   └── docker-compose.yml
└── pom.xml
```

## 快速开始

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

2. **修改配置**
修改 `src/main/resources/application-dev.yml` 中的数据库连接信息

3. **启动服务**
```bash
mvn spring-boot:run
```

4. **访问 API 文档**
- Knife4j：http://localhost:8080/api/doc.html
- Swagger：http://localhost:8080/api/swagger-ui.html

### Docker 部署

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## API 文档

启动服务后访问：
- 开发环境：http://localhost:8080/api/doc.html
- 生产环境：关闭

## 模块说明

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

## 开发规范

### 代码规范

- 命名：遵循 Java 命名规范
- 分层：Controller → Service → Mapper
- 异常：使用 BusinessException 抛出业务异常

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

## 许可证

Private - 仅供内部使用
