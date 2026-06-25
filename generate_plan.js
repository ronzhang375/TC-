const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell,
        Header, Footer, AlignmentType, LevelFormat, HeadingLevel,
        BorderStyle, WidthType, ShadingType, PageNumber } = require('docx');
const fs = require('fs');

const border = { style: BorderStyle.SINGLE, size: 1, color: "CCCCCC" };
const borders = { top: border, bottom: border, left: border, right: border };

function createCell(text, width, isHeader = false) {
    return new TableCell({
        borders,
        width: { size: width, type: WidthType.DXA },
        shading: isHeader ? { fill: "D5E8F0", type: ShadingType.CLEAR } : undefined,
        margins: { top: 80, bottom: 80, left: 120, right: 120 },
        children: [new Paragraph({ children: [new TextRun({ text, bold: isHeader })] })]
    });
}

function createTable(headers, rows, columnWidths) {
    const headerCells = headers.map((h, i) => createCell(h, columnWidths[i], true));
    const dataRows = rows.map(row => new TableRow({
        children: row.map((cell, i) => createCell(String(cell), columnWidths[i]))
    }));
    return new Table({
        width: { size: columnWidths.reduce((a, b) => a + b, 0), type: WidthType.DXA },
        columnWidths: columnWidths,
        rows: [new TableRow({ children: headerCells }), ...dataRows]
    });
}

function heading1(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun(text)], spacing: { before: 400, after: 200 } });
}

function heading2(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun(text)], spacing: { before: 300, after: 150 } });
}

function heading3(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_3, children: [new TextRun(text)], spacing: { before: 200, after: 100 } });
}

function paragraph(text) {
    return new Paragraph({ children: [new TextRun(text)], spacing: { after: 120 } });
}

function bullet(text) {
    return new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun(text)]
    });
}

function checkItem(text) {
    return new Paragraph({
        numbering: { reference: "checkboxes", level: 0 },
        children: [new TextRun(text)]
    });
}

const doc = new Document({
    styles: {
        default: { document: { run: { font: "Arial", size: 24 } } },
        paragraphStyles: [
            { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 32, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 400, after: 200 }, outlineLevel: 0 } },
            { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 28, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 300, after: 150 }, outlineLevel: 1 } },
            { id: "Heading3", name: "Heading 3", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 24, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 200, after: 100 }, outlineLevel: 2 } },
        ]
    },
    numbering: {
        config: [
            { reference: "bullets", levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT,
              style: { paragraph: { indent: { left: 720, hanging: 360 } } } }] },
            { reference: "checkboxes", levels: [{ level: 0, format: LevelFormat.BULLET, text: "☐", alignment: AlignmentType.LEFT,
              style: { paragraph: { indent: { left: 720, hanging: 360 } } } }] }
        ]
    },
    sections: [{
        properties: {
            page: {
                size: { width: 11906, height: 16838 },
                margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 }
            }
        },
        headers: {
            default: new Header({ children: [new Paragraph({
                alignment: AlignmentType.RIGHT,
                children: [new TextRun({ text: "体彩衍生品商城小程序 开发规划", color: "666666", size: 20 })]
            })] })
        },
        footers: {
            default: new Footer({ children: [new Paragraph({
                alignment: AlignmentType.CENTER,
                children: [new TextRun({ text: "第 ", size: 20 }), new TextRun({ children: [PageNumber.CURRENT], size: 20 }), new TextRun({ text: " 页", size: 20 })]
            })] })
        },
        children: [
            // 标题
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 480 },
                children: [new TextRun({ text: "体彩衍生品商城小程序", size: 48, bold: true })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 480 },
                children: [new TextRun({ text: "开发规划", size: 40 })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 120 },
                children: [new TextRun({ text: "基于 PRD V2.0", size: 28, color: "666666" })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 480 },
                children: [new TextRun({ text: "编制日期：2026年6月18日", size: 24 })] }),

            // 核心逻辑链
            heading1("一、开发顺序核心逻辑"),
            paragraph("基于功能模块依赖关系和数据流，优化后的开发顺序："),
            createTable(
                ["顺序", "模块", "理由"],
                [
                    ["1", "项目脚手架 + 权限体系", "所有模块的基础"],
                    ["2", "商品基础数据（分类+商品+SKU）", "没有商品，后续都无法测试"],
                    ["3", "C端小程序基础（登录+浏览+购物车）", "用户触达的第一步"],
                    ["4", "收货地址 + 订单核心流程", "交易闭环的关键"],
                    ["5", "微信支付", "订单必须能支付才算完成"],
                    ["6", "供货商履约（接单+发货）", "订单需要被履约"],
                    ["7", "渠道商管理 + 归属逻辑", "入口和佣金的基础"],
                    ["8", "佣金系统（计算+结算+打款）", "需要订单数据积累"],
                    ["9", "运营后台（Banner+活动+配置）", "辅助运营"],
                    ["10", "报表系统", "需要历史数据支撑"],
                    ["11", "消息通知", "体验优化"]
                ],
                [1500, 4000, 5000]
            ),

            // 项目概述
            heading1("二、项目概述"),

            heading2("2.1 技术架构"),
            createTable(
                ["层级", "技术方案", "说明"],
                [
                    ["C端小程序", "UniApp + uView UI", "跨端，一期聚焦微信小程序"],
                    ["管理后台", "Vue3 + Element Plus", "RBAC权限，统一管理端"],
                    ["后端服务", "Java 17 + SpringBoot 3", "微服务架构"],
                    ["数据库", "MySQL 8 + Redis", "主从读写分离规划"],
                    ["文件存储", "阿里云 OSS", "图片、素材存储"],
                    ["消息推送", "微信模板消息", "订单节点通知"],
                    ["部署", "Docker + 云平台", "弹性扩容"]
                ],
                [3000, 3500, 4000]
            ),

            heading2("2.2 角色体系"),
            createTable(
                ["角色", "说明"],
                [
                    ["平台超管", "总运营方，最高权限"],
                    ["地区运营（分平台）", "平台超管子账号，负责区域运营"],
                    ["供货商", "供货、备货、发货"],
                    ["渠道商", "入口铺设，看订单+佣金（极简单页面）"]
                ],
                [3500, 7000]
            ),

            // 开发阶段规划
            heading1("三、开发阶段规划"),

            heading2("Phase 0: 基础设施（1-1.5周）"),
            paragraph("目标：搭建项目骨架，支撑后续所有模块开发"),
            bullet("SpringBoot 项目结构搭建（分层架构：controller/service/mapper/entity）"),
            bullet("MySQL 数据库初始化脚本（建库、建表基础SQL）"),
            bullet("Redis 配置（缓存、会话）"),
            bullet("OSS 文件服务封装（图片上传/下载）"),
            bullet("Swagger/API 文档集成"),
            bullet("日志框架配置（Logback）+ 全局异常处理"),
            bullet("RBAC 权限架构设计（用户/角色/菜单/权限）"),
            bullet("运营后台 Vue3 + Element Plus 框架搭建"),
            bullet("C端小程序 UniApp 项目初始化"),

            heading2("Phase 1: 商品与 C端基础（2-2.5周）"),
            paragraph("目标：用户可浏览商品、加购下单"),
            heading3("1.1 商品基础数据（1周）"),
            checkItem("商品分类管理（CRUD）"),
            checkItem("产品管理（花束款式 × 刮刮乐面额矩阵 SKU）"),
            checkItem("SKU 定价（地区独立）"),
            checkItem("库存管理（地区运营配置）"),
            checkItem("商品上下架控制"),
            checkItem("供货商绑定品类"),
            heading3("1.2 C端小程序基础（1.5周）"),
            checkItem("微信授权登录 + 用户信息获取"),
            checkItem("首页（Banner + 推荐商品 + 活动位）"),
            checkItem("商品分类 + 列表 + 搜索筛选"),
            checkItem("商品详情（SKU 矩阵选择：花束款式 × 刮刮乐面额）"),
            checkItem("购物车（加购、修改数量、删除、合并结算）"),
            checkItem("直接购买（快速下单入口）"),

            heading2("Phase 2: 交易闭环（2周）"),
            paragraph("目标：完成下单到支付的完整链路"),
            heading3("2.1 收货地址（0.3周）"),
            checkItem("收货地址新增/编辑/删除/设为默认"),
            checkItem("地址列表选择"),
            heading3("2.2 订单核心（0.7周）"),
            checkItem("订单确认页（地址+商品+价格明细）"),
            checkItem("订单创建（扣减库存、锁定 SKU）"),
            checkItem("订单状态机：待支付→待接单→待发货→配送中→已完成/已取消"),
            checkItem("订单列表 + 订单详情"),
            checkItem("退款/退货申请（按商品配置规则）"),
            heading3("2.3 微信支付（0.5周）"),
            checkItem("微信支付 JSAPI 统一下单"),
            checkItem("支付回调通知处理"),
            checkItem("支付成功/失败页面"),
            checkItem("退款接口对接"),
            heading3("2.4 C端订单完善（0.5周）"),
            checkItem("物流信息查看"),
            checkItem("收货确认"),
            checkItem("订单消息通知"),

            heading2("Phase 3: 供货商履约（1.5周）"),
            paragraph("目标：订单被供货商接单并发货"),
            heading3("3.1 供货商后台框架（0.2周）"),
            checkItem("供货商账号密码登录"),
            checkItem("供货商工作台首页"),
            heading3("3.2 接单流程（0.5周）"),
            checkItem("待接单列表（新订单提醒）"),
            checkItem("接单确认"),
            checkItem("接单后状态更新同步"),
            heading3("3.3 发货流程（0.5周）"),
            checkItem("发货操作（物流公司+运单号录入）"),
            checkItem("发货状态同步（推送给平台和用户）"),
            checkItem("门店自取订单处理"),
            heading3("3.4 供货商数据（0.3周）"),
            checkItem("库存查看（运营方管理的库存）"),
            checkItem("结算账单查看（集采结算）"),

            heading2("Phase 4: 渠道与归属（1.5周）"),
            paragraph("目标：渠道商入驻，扫码锁定归属关系"),
            heading3("4.1 渠道商管理（0.5周）"),
            checkItem("渠道商入驻（运营方手动添加、分配账号）"),
            checkItem("渠道商信息管理"),
            checkItem("渠道商状态管理（启用/禁用）"),
            checkItem("平台自营渠道配置"),
            heading3("4.2 二维码与归属（0.5周）"),
            checkItem("二维码生成（携带渠道ID + 地区ID）"),
            checkItem("扫码进入 → 锁定地区 + 渠道归属"),
            checkItem("分享小程序 → 继承原归属"),
            checkItem("扫新码 → 切换归属"),
            checkItem("自选地区 → 归属平台自营渠道"),
            heading3("4.3 C端归属逻辑（0.5周）"),
            checkItem("用户渠道归属存储（Cookie/Session）"),
            checkItem("订单与渠道商关联"),
            checkItem("渠道来源标识展示（可配置）"),

            heading2("Phase 5: 佣金系统（1.5周）"),
            paragraph("目标：渠道商可查看佣金，运营方可结算"),
            heading3("5.1 佣金规则配置（0.3周）"),
            checkItem("佣金比例配置（按渠道商维度）"),
            checkItem("支持固定比例模式"),
            checkItem("结算周期配置（周结/月结）"),
            heading3("5.2 佣金计算（0.4周）"),
            checkItem("佣金计算引擎（订单完成后计算）"),
            checkItem("佣金账单生成（按结算周期）"),
            checkItem("佣金数据报表（各方佣金汇总）"),
            heading3("5.3 结算打款（0.4周）"),
            checkItem("佣金账单导出"),
            checkItem("结算打款记录"),
            checkItem("运营方打款操作"),
            heading3("5.4 渠道商后台（0.4周）"),
            checkItem("登录认证"),
            checkItem("本渠道订单查询（商品、数量、金额）"),
            checkItem("佣金收益查看"),
            checkItem("佣金账单查看"),
            checkItem("结算记录查看"),
            checkItem("推广二维码查看/下载"),

            heading2("Phase 6: 运营能力（1.5周）"),
            paragraph("目标：完善运营配置和数据报表"),
            heading3("6.1 运营配置（0.5周）"),
            checkItem("首页 Banner 管理"),
            checkItem("推荐商品管理"),
            checkItem("活动管理（营销活动配置）"),
            checkItem("系统参数配置（年龄校验开关、渠道来源展示开关、退款规则）"),
            heading3("6.2 报表系统（0.7周）"),
            checkItem("订单数据报表（按时间/渠道）"),
            checkItem("销售数据报表（销售额、销量趋势）"),
            checkItem("渠道推广数据报表（扫码量、转化率）"),
            checkItem("供货商履约数据报表（接单时效、发货时效）"),
            heading3("6.3 资金管理（0.3周）"),
            checkItem("平台收款账户管理"),
            checkItem("资金流水记录"),
            checkItem("退款资金处理"),

            heading2("Phase 7: 地区运营扩展（0.5周）"),
            paragraph("目标：支持多地区独立运营"),
            checkItem("地区运营账号管理（子账号模式）"),
            checkItem("地区切换（平台超管切换视角）"),
            checkItem("地区基础信息配置"),
            checkItem("地区独立数据隔离实现"),

            heading2("Phase 8: 集成上线（1.5周）"),
            paragraph("目标：微信生态集成 + 生产部署"),
            heading3("8.1 微信生态集成（0.5周）"),
            checkItem("微信小程序发布审核"),
            checkItem("微信支付商户配置 + 签名验证"),
            checkItem("微信模板消息行业配置"),
            heading3("8.2 运营配置fine-tune（0.3周）"),
            checkItem("基础数据初始化（分类数据、字典数据）"),
            checkItem("测试数据准备"),
            checkItem("运营人员培训"),
            heading3("8.3 部署上线（0.7周）"),
            checkItem("生产环境部署（Docker 镜像）"),
            checkItem("域名配置 + HTTPS 证书"),
            checkItem("监控告警配置"),
            checkItem("数据备份策略"),
            checkItem("上线前全链路测试"),

            // 数据库设计
            heading1("四、数据库设计要点"),

            heading2("4.1 核心表结构"),
            createTable(
                ["模块", "表名", "说明"],
                [
                    ["用户", "sys_user", "用户表（平台用户）"],
                    ["用户", "sys_role", "角色表"],
                    ["用户", "sys_menu", "菜单权限表"],
                    ["用户", "sys_dict", "字典表"],
                    ["地区", "region_info", "地区信息"],
                    ["地区", "region_settings", "地区配置"],
                    ["供货商", "supplier_info", "供货商信息"],
                    ["供货商", "supplier_product", "供货商品类绑定"],
                    ["渠道商", "channel_info", "渠道商信息"],
                    ["渠道商", "channel_qrcode", "渠道二维码记录"],
                    ["商品", "category", "商品分类"],
                    ["商品", "product_info", "商品信息"],
                    ["商品", "product_sku", "SKU表（花束×刮刮乐矩阵）"],
                    ["商品", "product_stock", "库存表"],
                    ["订单", "order_main", "订单主表"],
                    ["订单", "order_item", "订单明细"],
                    ["订单", "order_refund", "退款申请"],
                    ["佣金", "commission_rule", "佣金规则"],
                    ["佣金", "commission_bill", "佣金账单"],
                    ["佣金", "commission_settlement", "结算记录"],
                    ["物流", "logistics_info", "物流信息"],
                    ["资金", "account_balance", "账户余额"],
                    ["资金", "flow_record", "资金流水"]
                ],
                [2000, 3500, 5000]
            ),

            heading2("4.2 关键关系"),
            createTable(
                ["关系", "说明"],
                [
                    ["订单 → 渠道商", "锁定佣金归属"],
                    ["订单 → 供货商", "锁定履约方"],
                    ["SKU → 地区", "地区独立定价和库存"],
                    ["佣金账单 → 渠道商", "按结算周期生成"]
                ],
                [3500, 7000]
            ),

            // 第三方集成
            heading1("五、第三方集成清单"),
            createTable(
                ["系统", "集成内容", "优先级"],
                [
                    ["微信小程序", "授权登录、用户信息", "P0"],
                    ["微信支付", "JSAPI 支付、退款", "P0"],
                    ["微信模板消息", "订单通知", "P1"],
                    ["阿里云 OSS", "图片存储", "P0"],
                    ["快递100", "物流轨迹查询（可选）", "P2"]
                ],
                [3000, 5000, 2000]
            ),

            // 开发里程碑
            heading1("六、开发里程碑"),
            createTable(
                ["阶段", "里程碑", "交付物"],
                [
                    ["Phase 0-1", "商品可下单", "商品基础 + C端浏览 + 加购"],
                    ["Phase 2", "支付完成", "微信支付 + 交易闭环"],
                    ["Phase 3", "履约上线", "供货商接单发货完整"],
                    ["Phase 4-5", "渠道佣金跑通", "渠道商入驻 + 佣金结算"],
                    ["Phase 6-7", "运营完善", "报表 + 多地区 + 活动配置"],
                    ["Phase 8", "生产上线", "正式运营版本"]
                ],
                [2500, 3500, 4500]
            ),

            // 关键路径
            heading1("七、关键路径图"),
            paragraph("Week 1-2:    Phase 0 —— 脚手架 + 权限 + 登录"),
            paragraph("                  │"),
            paragraph("Week 3-4:    Phase 1 —— 商品管理 + 小程序浏览 + 购物车"),
            paragraph("                                │"),
            paragraph("Week 5-6:    Phase 2 —— 地址 + 订单 + 支付 + 订单列表"),
            paragraph("                                                    │"),
            paragraph("Week 7-8:    Phase 3 —— 供货商接单 + 发货 + 状态同步"),
            paragraph("                                                    │"),
            paragraph("Week 9-10:   Phase 4-5 —— 渠道商 + 归属 + 佣金计算 + 结算"),
            paragraph("                                                    │"),
            paragraph("Week 11:      Phase 6-7 —— 运营配置 + 报表 + 多地区"),
            paragraph("                                                    │"),
            paragraph("Week 12:      Phase 8 —— 集成 + 测试 + 上线"),

            // 开发团队
            heading1("八、开发团队建议"),
            createTable(
                ["角色", "人数", "职责"],
                [
                    ["后端开发", "2-3人", "SpringBoot 服务、API、数据库"],
                    ["前端小程序", "1-2人", "UniApp C端小程序"],
                    ["前端管理后台", "1人", "Vue3 + Element Plus"],
                    ["测试", "1人", "全链路测试"],
                    ["项目经理", "1人", "协调、进度把控"]
                ],
                [2500, 1500, 6500]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("总工期估算：12 周（含测试和缓冲）"),

            // 下一步行动
            heading1("九、下一步行动"),
            bullet("确认开发规划 → 确定团队规模和时间线"),
            bullet("技术方案设计 → 细化数据库表结构、API 接口定义"),
            bullet("开发环境准备 → 代码仓库（Git）、测试服务器、域名"),
            bullet("Phase 0 启动 → 项目脚手架和基础设施搭建")
        ]
    }]
});

Packer.toBuffer(doc).then(buffer => {
    fs.writeFileSync("D:\\AI claude\\彩票文创商城\\体彩衍生品商城小程序-开发规划.docx", buffer);
    console.log("开发规划文档生成成功！");
}).catch(err => {
    console.error("生成失败:", err);
});
