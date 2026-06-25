const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell,
        Header, Footer, AlignmentType, LevelFormat, HeadingLevel,
        BorderStyle, WidthType, ShadingType, PageNumber } = require('docx');
const fs = require('fs');

// 辅助函数：创建表格
const border = { style: BorderStyle.SINGLE, size: 1, color: "CCCCCC" };
const borders = { top: border, bottom: border, left: border, right: border };

function createTable(headers, rows, columnWidths) {
    const headerCells = headers.map((h, i) => new TableCell({
        borders,
        width: { size: columnWidths[i], type: WidthType.DXA },
        shading: { fill: "D5E8F0", type: ShadingType.CLEAR },
        margins: { top: 80, bottom: 80, left: 120, right: 120 },
        children: [new Paragraph({ children: [new TextRun({ text: h, bold: true })] })]
    }));

    const dataRows = rows.map(row => new TableRow({
        children: row.map((cell, i) => new TableCell({
            borders,
            width: { size: columnWidths[i], type: WidthType.DXA },
            margins: { top: 80, bottom: 80, left: 120, right: 120 },
            children: [new Paragraph({ children: [new TextRun(cell)] })]
        }))
    }));

    return new Table({
        width: { size: columnWidths.reduce((a, b) => a + b, 0), type: WidthType.DXA },
        columnWidths: columnWidths,
        rows: [new TableRow({ children: headerCells }), ...dataRows]
    });
}

function createCell(text, width, isHeader = false) {
    return new TableCell({
        borders,
        width: { size: width, type: WidthType.DXA },
        shading: isHeader ? { fill: "D5E8F0", type: ShadingType.CLEAR } : undefined,
        margins: { top: 80, bottom: 80, left: 120, right: 120 },
        children: [new Paragraph({ children: [new TextRun({ text, bold: isHeader })] })]
    });
}

function heading1(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_1, children: [new TextRun(text)] });
}

function heading2(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_2, children: [new TextRun(text)] });
}

function heading3(text) {
    return new Paragraph({ heading: HeadingLevel.HEADING_3, children: [new TextRun(text)] });
}

function paragraph(text) {
    return new Paragraph({ children: [new TextRun(text)] });
}

function bullet(text) {
    return new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun(text)]
    });
}

function tableRow(cells) {
    return new TableRow({ children: cells });
}

// ============ 文档内容 ============

const doc = new Document({
    styles: {
        default: { document: { run: { font: "Arial", size: 24 } } },
        paragraphStyles: [
            { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 32, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 240, after: 240 }, outlineLevel: 0 } },
            { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 28, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 180, after: 180 }, outlineLevel: 1 } },
            { id: "Heading3", name: "Heading 3", basedOn: "Normal", next: "Normal", quickFormat: true,
              run: { size: 24, bold: true, font: "Arial" },
              paragraph: { spacing: { before: 120, after: 120 }, outlineLevel: 2 } },
        ]
    },
    numbering: {
        config: [{
            reference: "bullets",
            levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT,
                      style: { paragraph: { indent: { left: 720, hanging: 360 } } } }]
        }, {
            reference: "numbers",
            levels: [{ level: 0, format: LevelFormat.DECIMAL, text: "%1.", alignment: AlignmentType.LEFT,
                      style: { paragraph: { indent: { left: 720, hanging: 360 } } } }]
        }]
    },
    sections: [{
        properties: {
            page: {
                size: { width: 11906, height: 16838 }, // A4
                margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 }
            }
        },
        headers: {
            default: new Header({ children: [new Paragraph({
                alignment: AlignmentType.RIGHT,
                children: [new TextRun({ text: "体彩衍生品商城小程序 产品需求文档 V2.0", color: "666666", size: 20 })]
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
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 240 },
                children: [new TextRun({ text: "产品需求文档 (PRD)", size: 36 })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 120 },
                children: [new TextRun({ text: "文档版本：V2.0", size: 28 })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 120 },
                children: [new TextRun({ text: "文档状态：需求梳理阶段", size: 28 })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 120 },
                children: [new TextRun({ text: "编制日期：2026年6月16日", size: 28 })] }),
            new Paragraph({ alignment: AlignmentType.CENTER, spacing: { after: 480 },
                children: [new TextRun({ text: "密级：内部资料", size: 28 })] }),

            // 一、修订记录
            heading1("一、修订记录"),
            createTable(
                ["版本号", "修订日期", "修订内容", "修订人"],
                [
                    ["V1.0", "2026-06-16", "初始版本，完成需求梳理", "---"],
                    ["V2.0", "2026-06-17", "角色体系调整（5类→3类）；分润模型简化为两方佣金体系；供货商（供应商+体彩门店）合并；渠道商极简化", "---"]
                ],
                [2000, 2400, 5000, 2000]
            ),

            // 二、项目概况
            heading1("二、项目概况"),

            heading2("2.1 项目背景"),
            paragraph("为拓展体育彩票销售渠道、提升品牌影响力，拟打造体彩衍生品商城小程序。项目以「买商品赠彩票」为核心模式，将刮刮乐即开型彩票与具象商品（鲜花等）组合销售，通过线下多渠道铺设二维码的方式引流至小程序，实现线上交易、线下履约的O2O闭环。"),

            heading2("2.2 基本信息"),
            createTable(
                ["维度", "说明"],
                [
                    ["项目定位", "体彩衍生品商城小程序"],
                    ["核心模式", "买商品赠彩票（刮刮乐 + 具象产品组合）"],
                    ["目标用户", "普通消费者（C端）"],
                    ["当前阶段", "从0开始规划MVP"],
                    ["预期规模", "第一期约1000家渠道商"],
                    ["盈利模式", "平台统一收款，渠道商佣金结算为主"],
                    ["角色体系", "平台/运营方 + 供货商 + 渠道商（3类）"],
                    ["技术栈", "Java + SpringBoot（后端）、Vue3（管理端）、UniApp（小程序）"],
                    ["部署方式", "云平台"]
                ],
                [3000, 8000]
            ),

            // 三、产品与SKU模型
            heading1("三、产品与 SKU 模型"),

            heading2("3.1 商品体系"),
            createTable(
                ["维度", "说明"],
                [
                    ["商品品类", "一期以鲜花为核心品类，后续可扩展接入更多品类（如零食、文创周边等）"],
                    ["产品组合形式", "矩阵式 SKU：花束款式 × 刮刮乐面额自由组合"],
                    ["定价权", "运营方统一制定，各地区可独立定价"],
                    ["库存管理", "运营方管理，供货商备货"],
                    ["采购模式", "运营方集采，供货商交付等量商品（买断式）"]
                ],
                [3000, 8000]
            ),

            heading2("3.2 刮刮乐实物票"),
            paragraph("刮刮乐采用实物纸质票形态，随鲜花包裹一起配送给用户。供货商（整合原供应商+体彩门店）负责线下对接取票后打包发货。运营方按集采模式与供货商结算，商品所有权归运营方。"),

            // 四、平台运营架构
            heading1("四、平台运营架构"),

            heading2("4.1 角色体系（3类角色）"),
            createTable(
                ["角色", "职责说明", "系统端"],
                [
                    ["平台超管", "统一管理全局设置（系统配置、权限等）；可直接运营某个地区（兼具双重身份）", "平台超管后台"],
                    ["地区运营（分平台）", "平台超管的子账号，负责区域运营，收益归平台统一支配", "地区运营后台"],
                    ["供货商", "接收订单→线下取票→打包发货→同步发货状态", "供货商后台"],
                    ["渠道商", "铺设二维码引流消费者，看本渠道订单+佣金，不参与运营", "渠道商后台"]
                ],
                [2500, 6500, 2400]
            ),

            heading2("4.2 地区运营模式"),
            paragraph("平台以「地区」为独立运营部署单元，每个地区拥有独立的运营账号（作为平台超管的子账号）。地区并非严格的行政区划层级，而是根据业务覆盖范围划分的运营单元（如「福州市仓山区」、「莆田市」）。"),
            bullet("地区运营账号为平台超管的子账号，统一核算，不单独结算分润"),
            bullet("每个地区独立管理本地区的商品、渠道商、库存和定价"),
            bullet("平台超管可直接运营某个地区，具备地区运营的完整功能权限"),
            bullet("小程序统一入口，渠道二维码决定地区归属和渠道归属"),

            heading2("4.3 小程序入口与归属逻辑"),
            createTable(
                ["入口方式", "地区归属", "渠道归属"],
                [
                    ["扫渠道二维码", "该渠道所在地区", "该渠道商"],
                    ["自选地区（无二维码）", "用户选择的地区", "平台自营渠道"],
                    ["用户分享小程序", "继承原用户的地区和渠道归属", "继承原渠道归属"],
                    ["扫新地区二维码", "切换到新地区", "切换到新渠道"]
                ],
                [3500, 3500, 3500]
            ),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("渠道商入驻后，系统生成带渠道商标识的小程序二维码。用户扫码进入后锁定该渠道商归属，后续该用户的订单归属该渠道商，按运营方配置的比例结算佣金。"),

            heading2("4.4 管理后台架构"),
            paragraph("管理后台采用统一架构：一个Vue3应用，通过RBAC权限控制不同角色登录后可见范围。"),
            createTable(
                ["角色", "登录后可见范围", "说明"],
                [
                    ["平台超管", "全局管理菜单 + 可切换至任意地区运营视角", "兼具全局管理和地区运营双重身份"],
                    ["地区运营", "本地区范围的完整运营菜单和数据", "商品、渠道、订单、佣金等"],
                    ["供货商", "仅与自己相关的接单、发货、结算功能", "供货、备货、发货状态同步"],
                    ["渠道商", "本渠道订单数据 + 佣金收益（极简单页面）", "看数据收佣金，不参与运营"]
                ],
                [2500, 5000, 3500]
            ),

            // 五、交易与履约
            heading1("五、交易与履约"),

            heading2("5.1 订单流转"),
            createTable(
                ["步骤", "说明"],
                [
                    ["1. 用户下单", "用户选购商品，提交订单并完成支付"],
                    ["2. 平台接单", "平台接收订单，路由至本地区供货商"],
                    ["3. 供货商接单", "供货商确认接单，开始备货"],
                    ["4. 线下取票", "供货商线下对接体彩门店，领取刮刮乐实物票"],
                    ["5. 打包发货", "供货商将商品与刮刮乐打包，录入物流信息后发货"],
                    ["6. 物流配送", "平台实时跟踪物流信息，同步给用户"],
                    ["7. 用户收货", "用户确认收货，订单完成"]
                ],
                [3000, 7000]
            ),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("每个订单同时关联两个关键实体：供货商（履约方）和渠道商（佣金归属方）。"),

            heading2("5.2 履约规则"),
            createTable(
                ["维度", "说明"],
                [
                    ["下单方式", "购物车 + 直接购买两种模式"],
                    ["支付方式", "微信支付 / 聚合支付"],
                    ["配送方式", "由运营方配置供货商支持的配送方式（同城快递、门店自取等）"],
                    ["退款规则", "商品上架时可配置退款规则（未发货可退、配送中可退等）"],
                    ["物流对接", "手动填写和系统对接均支持"],
                    ["中奖兑奖", "一期线下兑奖，预留线上兑奖扩展模块"]
                ],
                [3000, 8000]
            ),

            // 六、渠道与佣金体系
            heading1("六、渠道与佣金体系"),

            heading2("6.1 渠道体系"),
            createTable(
                ["维度", "说明"],
                [
                    ["渠道类型", "体彩门店、商场、社区、合作商家等均可作为渠道"],
                    ["渠道层级", "扁平化结构，不分层级，运营方统一管理"],
                    ["二维码机制", "一个渠道一个码，码内携带渠道ID和地区ID"],
                    ["渠道入驻", "由运营方手动添加、分配账号"],
                    ["渠道归属", "扫码锁定渠道归属，分享继承原归属，扫新码可切换"]
                ],
                [3000, 8000]
            ),

            heading2("6.2 佣金体系（两方）"),
            createTable(
                ["参与方", "说明", "佣金来源"],
                [
                    ["平台/运营方", "收款主体，给渠道商结算佣金后，剩余归平台", "订单金额按比例分成"],
                    ["渠道商", "推广引流收益，按配置比例和周期结算", "订单金额按比例分成"]
                ],
                [3000, 5000, 3000]
            ),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("佣金规则要点："),
            bullet("渠道商佣金比例按运营方配置，支持固定比例、阶梯等多种模式"),
            bullet("结算周期支持周结和月结，由运营方后台灵活配置"),
            bullet("自选地区用户（无渠道来源）归属平台自营渠道，平台获得全额收益"),

            heading2("6.3 资金流向"),
            paragraph("用户下单支付 → 资金进入平台账户 → 平台按佣金规则定期结算 → 向渠道商打款。平台对资金拥有完全管控权，便于处理退款和异常情况。"),

            // 七、用户与体验
            heading1("七、用户与体验"),
            createTable(
                ["维度", "说明"],
                [
                    ["平台载体", "微信小程序"],
                    ["用户登录", "微信授权登录"],
                    ["渠道来源展示", "平台可配置是否让用户看到渠道来源信息"],
                    ["未成年人限制", "平台可配置是否开启18岁+年龄校验"],
                    ["用户体系", "一期不做会员/积分，预留扩展能力"],
                    ["彩票资质", "默认合规运营"],
                    ["发票", "暂不考虑"]
                ],
                [3000, 8000]
            ),

            // 八、完整功能清单
            heading1("八、完整功能清单"),

            heading2("8.1 C端小程序（UniApp）"),
            paragraph("核心交易功能："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["1", "微信授权登录", "用户通过微信授权快速登录小程序"],
                    ["2", "首页展示", "Banner轮播、推荐商品、活动位（按地区动态加载）"],
                    ["3", "商品分类浏览", "按品类分类展示商品列表"],
                    ["4", "商品搜索与筛选", "关键词搜索、多维度筛选"],
                    ["5", "商品详情", "花束款式×刮刮乐面额矩阵组合选择"],
                    ["6", "购物车", "多商品加购，合并结算"],
                    ["7", "直接购买", "单品快速下单"],
                    ["8", "收货地址管理", "新增、编辑、删除收货地址"],
                    ["9", "下单支付", "微信支付/聚合支付"],
                    ["10", "地区切换", "自选地区或扫新码切换"],
                    ["11", "订单列表与详情", "查看历史订单及详细状态"],
                    ["12", "订单状态跟踪", "下单→接单→发货→配送→收货"],
                    ["13", "物流信息查看", "实时查看物流轨迹"],
                    ["14", "收货确认", "用户手动确认收货"],
                    ["15", "退款/退货申请", "按商品配置的退款规则申请"],
                    ["16", "渠道来源标识", "可配置是否展示渠道来源信息"]
                ],
                [1500, 2500, 6000]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("用户体验功能："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["17", "消息通知", "微信模板消息：接单、发货、收货等节点通知"],
                    ["18", "用户反馈/投诉", "用户提交反馈或投诉入口"],
                    ["19", "在线客服", "一期简单实现或预留入口"]
                ],
                [1500, 2500, 6000]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("预留扩展功能："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["20", "会员体系", "等级、积分（预留）"],
                    ["21", "优惠券/促销活动", "营销工具（预留）"],
                    ["22", "刮刮乐线上兑奖", "线上兑奖模块（预留）"],
                    ["23", "电子刮刮乐体验", "线上刮奖互动（预留）"]
                ],
                [1500, 2500, 6000]
            ),

            heading2("8.2 平台超管后台（全局管理）"),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["24", "地区运营账号管理", "创建、配置、权限分配（子账号模式）"],
                    ["25", "系统参数配置", "年龄校验开关、渠道来源展示开关、退款规则等"],
                    ["26", "角色权限管理", "RBAC权限体系"],
                    ["27", "操作日志记录", "关键操作留痕"],
                    ["28", "全局数据总览报表", "跨地区数据汇总与趋势分析"],
                    ["29", "小程序配置与发布", "小程序版本管理、发布审核"],
                    ["30", "支付配置", "微信支付/聚合支付参数配置"],
                    ["31", "消息模板管理", "微信模板消息配置"],
                    ["32", "佣金规则配置", "渠道商佣金比例、结算周期等"]
                ],
                [1500, 3000, 5500]
            ),
            paragraph("此外，平台超管可直接运营某个地区，具备地区运营后台的全部功能。"),

            heading2("8.3 地区运营后台（分平台）"),
            paragraph("商品与库存："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["33", "产品管理", "产品定义、花束款式×刮刮乐面额矩阵SKU配置"],
                    ["34", "产品分类管理", "品类分类维护"],
                    ["35", "产品图片/素材管理", "图片上传、排序、管理"],
                    ["36", "产品上架/下架", "控制商品在C端的可见性"],
                    ["37", "产品定价管理", "本地区独立定价"],
                    ["38", "库存管理", "库存数量维护、扣减规则配置"]
                ],
                [1500, 3000, 5500]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("供货商管理："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["39", "供货商入驻", "手动添加、分配账号"],
                    ["40", "供货商信息管理", "名称、联系方式、地址等"],
                    ["41", "供货商品类绑定", "为每个品类绑定供货商"],
                    ["42", "供货商配送方式配置", "门店自取/外送等配送方式"],
                    ["43", "供货商状态管理", "启用/禁用"]
                ],
                [1500, 3000, 5500]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("渠道商管理："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["44", "渠道商入驻", "手动添加、分配账号"],
                    ["45", "渠道商信息管理", "名称、联系方式等"],
                    ["46", "二维码生成与管理", "一个渠道一个码，携带渠道ID"],
                    ["47", "渠道商佣金比例配置", "按渠道配置佣金比例"],
                    ["48", "渠道商状态管理", "启用/禁用"],
                    ["49", "平台自营渠道配置", "自选地区用户的默认渠道归属"]
                ],
                [1500, 3000, 5500]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("订单与佣金管理："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["50", "订单列表与详情", "本地区所有订单查看"],
                    ["51", "订单流转状态管理", "全流程状态跟踪"],
                    ["52", "退款审核与处理", "退款申请审核"],
                    ["53", "订单渠道来源追溯", "溯源渠道佣金归属"],
                    ["54", "佣金规则配置", "按渠道配置佣金比例"],
                    ["55", "佣金计算引擎", "自动计算各渠道商佣金"],
                    ["56", "佣金结算管理", "周结/月结，可配置结算周期"],
                    ["57", "佣金账单生成与导出", "自动生成结算账单"],
                    ["58", "结算打款", "向渠道商打款"]
                ],
                [1500, 3000, 5500]
            ),

            new Paragraph({ spacing: { before: 200 } }),
            paragraph("资金、运营与数据："),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["59", "平台收款账户管理", "收款账户配置"],
                    ["60", "资金流水记录", "收支明细记录"],
                    ["61", "退款资金处理", "退款资金流转处理"],
                    ["62", "首页Banner管理", "本地区Banner配置"],
                    ["63", "推荐商品管理", "首页推荐位配置"],
                    ["64", "活动管理", "营销活动配置"],
                    ["65", "订单数据报表", "按时间/渠道维度"],
                    ["66", "销售数据报表", "销售额、销量趋势"],
                    ["67", "渠道推广数据报表", "扫码量、转化率等"],
                    ["68", "佣金数据报表", "各渠道商佣金汇总"],
                    ["69", "地区基础信息配置", "地区名称、状态等"]
                ],
                [1500, 3000, 5500]
            ),

            heading2("8.4 供货商后台"),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["70", "登录认证", "账号密码登录"],
                    ["71", "待接单列表与接单", "新订单提醒与接单"],
                    ["72", "订单处理", "确认接单→发货全流程"],
                    ["73", "发货操作", "物流信息手动录入/系统对接"],
                    ["74", "门店自取订单管理", "如有配置门店自取方式"],
                    ["75", "结算账单查看", "查看集采结算明细"],
                    ["76", "库存查看", "查看运营方管理的库存数据"],
                    ["77", "发货状态同步", "同步发货状态给运营方"]
                ],
                [1500, 3000, 5500]
            ),

            heading2("8.5 渠道商后台（极简）"),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["78", "登录认证", "账号密码登录"],
                    ["79", "本渠道订单查询", "查看本渠道内C端用户订单（商品、数量、金额）"],
                    ["80", "佣金收益查看", "查看本渠道订单产生的佣金金额"],
                    ["81", "二维码查看/下载", "查看和下载推广二维码"],
                    ["82", "佣金账单查看", "结算周期内的佣金明细"],
                    ["83", "结算记录查看", "历史结算记录"]
                ],
                [1500, 3000, 5500]
            ),
            paragraph("渠道商后台极简化设计——单一页面展示渠道订单数据和佣金收益，不包含任何运营管理功能。"),

            // 九、技术架构与基础设施
            heading1("九、技术架构与基础设施"),

            heading2("9.1 技术架构概览"),
            createTable(
                ["层级", "技术方案", "说明"],
                [
                    ["前端-小程序", "UniApp", "跨端开发框架，一期聚焦微信小程序"],
                    ["前端-管理端", "Vue3 + Element Plus", "统一管理后台前端框架，单应用 + RBAC"],
                    ["后端服务", "Java + SpringBoot", "微服务架构，支撑多地区独立运营"],
                    ["数据存储", "MySQL + Redis", "关系型数据库 + 缓存"],
                    ["文件存储", "对象存储OSS", "图片、素材存储"],
                    ["消息推送", "微信模板消息", "订单节点通知"],
                    ["部署方式", "云平台", "弹性扩容，按地区数据隔离"]
                ],
                [3000, 3500, 4500]
            ),

            heading2("9.2 基础设施功能清单"),
            createTable(
                ["序号", "功能名称", "功能说明"],
                [
                    ["84", "API接口设计与网关", "统一API网关"],
                    ["85", "文件/图片存储服务", "OSS对象存储"],
                    ["86", "微信小程序配置与发布", "小程序管理"],
                    ["87", "微信支付/聚合支付对接", "支付系统对接"],
                    ["88", "消息推送服务", "微信模板消息、短信"],
                    ["89", "数据库设计与多地区数据架构", "多地区数据隔离"],
                    ["90", "日志与监控", "系统监控告警"],
                    ["91", "安全机制", "接口鉴权、数据加密、防刷限流"],
                    ["92", "备份与恢复", "数据备份策略"],
                    ["93", "小程序版本管理与更新", "小程序版本管理"]
                ],
                [1500, 3500, 5000]
            ),

            // 十、系统角色关系图
            heading1("十、系统角色关系图"),
            paragraph("以下为系统角色之间的层级关系概览："),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("平台超管"),
            bullet("管理全局：系统配置、权限、地区运营账号（分平台）"),
            bullet("可自营：直接运营某个地区（等同地区运营）"),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("├── 地区运营（分平台/子运营）"),
            bullet("负责本地区商品、渠道商、订单、佣金等运营管理"),
            bullet("统一核算，收益归平台支配"),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("├── 供货商（供应商+体彩门店合并）"),
            bullet("供货给运营方、库存备货、发货、同步状态"),
            bullet("集采关系，不参与佣金分配"),
            new Paragraph({ spacing: { before: 200 } }),
            paragraph("└── 渠道商（扁平化，无多级结构）"),
            bullet("铺设二维码引流，看本渠道订单+佣金"),
            bullet("一个渠道一个码，扫码锁定归属"),

            // 附录
            heading1("附录"),
            new Paragraph({ spacing: { before: 200 } }),
            heading2("业务逻辑调整说明（V1.0 → V2.0）"),
            bullet("角色体系从5类缩减为3类（平台/运营方、供货商、渠道商）"),
            bullet("分润模型从「四方分润」简化为「两方佣金」（平台/运营方 + 渠道商）"),
            bullet("供货商（供应商+体彩门店合并）改为集采关系，不参与佣金分配"),
            bullet("渠道商取消多级树状结构，极简单页面（看订单+佣金）"),
            bullet("佣金结算周期可配置（周结/月结）"),
            bullet("小程序入口与归属逻辑保留，作为渠道商绑定的核心机制"),
            new Paragraph({ spacing: { before: 400 } }),
            paragraph("本文档为需求梳理阶段产出，后续将进入设计方案阶段，对各功能模块进行详细的交互设计、数据库设计和接口设计。")
        ]
    }]
});

// 生成文档
Packer.toBuffer(doc).then(buffer => {
    fs.writeFileSync("D:\\AI claude\\彩票文创商城\\体彩衍生品商城小程序产品需求文档_V2.0.docx", buffer);
    console.log("PRD V2.0 生成成功！");
}).catch(err => {
    console.error("生成失败:", err);
});
