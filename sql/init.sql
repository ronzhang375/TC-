-- =========================================
-- 体彩衍生品商城小程序 - 数据库初始化脚本
-- 版本：V1.0
-- 日期：2026-06-18
-- =========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `lottery_mall` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `lottery_mall`;

-- =========================================
-- 1. 系统模块（sys_）
-- =========================================

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nick_name` VARCHAR(50) COMMENT '昵称',
  `phone` VARCHAR(11) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `wx_openid` VARCHAR(64) COMMENT '微信OpenID',
  `wx_unionid` VARCHAR(64) COMMENT '微信UnionID',
  `user_type` VARCHAR(20) NOT NULL DEFAULT 'customer' COMMENT '用户类型：customer=普通用户',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `login_date` DATE COMMENT '最后登录日期',
  `login_ip` VARCHAR(128) COMMENT '最后登录IP',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_wx_openid` (`wx_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(100) NOT NULL COMMENT '角色标识',
  `role_sort` INT NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `data_scope` VARCHAR(50) DEFAULT '1' COMMENT '数据范围：1全部 2本地区',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单权限表
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `parent_id` BIGINT NOT NULL DEFAULT '0' COMMENT '父菜单ID',
  `order_num` INT DEFAULT '0' COMMENT '显示顺序',
  `path` VARCHAR(200) DEFAULT '' COMMENT '路由地址',
  `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `menu_type` CHAR(1) DEFAULT '' COMMENT '菜单类型：M目录 C菜单 F按钮',
  `visible` TINYINT(1) DEFAULT '0' COMMENT '状态：0显示 1隐藏',
  `status` TINYINT(1) DEFAULT '0' COMMENT '状态：0正常 1停用',
  `perms` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `icon` VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
  `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dict_sort` INT DEFAULT '0' COMMENT '字典排序',
  `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
  `dict_value` VARCHAR(100) NOT NULL COMMENT '字典键值',
  `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
  `css_class` VARCHAR(100) DEFAULT NULL COMMENT '样式属性',
  `list_class` VARCHAR(100) DEFAULT NULL COMMENT '回显样式',
  `is_default` TINYINT(1) DEFAULT '0' COMMENT '是否默认：0否 1是',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键名',
  `config_value` VARCHAR(500) NOT NULL COMMENT '配置值',
  `config_type` VARCHAR(20) DEFAULT 'N' COMMENT '类型：Y系统参数 N普通参数',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `sys_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module` VARCHAR(50) DEFAULT '' COMMENT '模块名称',
  `business_type` VARCHAR(20) DEFAULT '' COMMENT '业务类型',
  `method` VARCHAR(100) DEFAULT '' COMMENT '请求方法',
  `request_method` VARCHAR(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` VARCHAR(20) DEFAULT '' COMMENT '操作类型',
  `oper_name` VARCHAR(50) DEFAULT '' COMMENT '操作人',
  `oper_ip` VARCHAR(128) DEFAULT '' COMMENT '操作IP',
  `oper_location` VARCHAR(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` TEXT COMMENT '返回结果',
  `status` TINYINT(1) DEFAULT '0' COMMENT '状态：0正常 1异常',
  `error_msg` TEXT COMMENT '错误消息',
  `oper_time` DATETIME DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_oper_time` (`oper_time`),
  KEY `idx_oper_name` (`oper_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =========================================
-- 2. 地区运营模块（region_）
-- =========================================

-- 地区信息表
CREATE TABLE IF NOT EXISTS `region_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地区ID',
  `region_name` VARCHAR(100) NOT NULL COMMENT '地区名称',
  `region_code` VARCHAR(50) DEFAULT '' COMMENT '地区编码',
  `parent_id` BIGINT NOT NULL DEFAULT '0' COMMENT '父地区ID',
  `order_num` INT DEFAULT '0' COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_region_code` (`region_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区信息表';

-- 地区配置表
CREATE TABLE IF NOT EXISTS `region_settings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `region_id` BIGINT NOT NULL COMMENT '地区ID',
  `age_check_enabled` TINYINT(1) DEFAULT '0' COMMENT '年龄校验开关：0关 1开',
  `channel_source_visible` TINYINT(1) DEFAULT '1' COMMENT '渠道来源展示：0关 1开',
  `default_channel_id` BIGINT DEFAULT NULL COMMENT '默认渠道ID（自营）',
  `delivery_fee` DECIMAL(10,2) DEFAULT '0.00' COMMENT '默认配送费',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区配置表';

-- =========================================
-- 3. 供货商模块（supplier_）
-- =========================================

-- 供货商信息表
CREATE TABLE IF NOT EXISTS `supplier_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '供货商ID',
  `supplier_name` VARCHAR(100) NOT NULL COMMENT '供货商名称',
  `supplier_code` VARCHAR(50) NOT NULL COMMENT '供货商编码',
  `contact_name` VARCHAR(50) COMMENT '联系人',
  `contact_phone` VARCHAR(20) COMMENT '联系电话',
  `address` VARCHAR(255) COMMENT '地址',
  `region_id` BIGINT NOT NULL COMMENT '所属地区ID',
  `delivery_types` VARCHAR(100) DEFAULT '' COMMENT '配送方式：1同城快递 2门店自取',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`),
  KEY `idx_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供货商信息表';

-- 供货商品类绑定表
CREATE TABLE IF NOT EXISTS `supplier_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `supplier_id` BIGINT NOT NULL COMMENT '供货商ID',
  `category_id` BIGINT NOT NULL COMMENT '商品分类ID',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_category` (`supplier_id`, `category_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供货商品类绑定表';

-- =========================================
-- 4. 渠道商模块（channel_）
-- =========================================

-- 渠道商信息表
CREATE TABLE IF NOT EXISTS `channel_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '渠道商ID',
  `channel_name` VARCHAR(100) NOT NULL COMMENT '渠道商名称',
  `channel_code` VARCHAR(50) NOT NULL COMMENT '渠道商编码',
  `channel_type` VARCHAR(20) DEFAULT '' COMMENT '渠道类型：store=门店 market=商场 community=社区',
  `contact_name` VARCHAR(50) COMMENT '联系人',
  `contact_phone` VARCHAR(20) COMMENT '联系电话',
  `region_id` BIGINT NOT NULL COMMENT '所属地区ID',
  `commission_rate` DECIMAL(5,4) NOT NULL DEFAULT '0.0500' COMMENT '佣金比例',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_channel_code` (`channel_code`),
  KEY `idx_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道商信息表';

-- 渠道二维码表
CREATE TABLE IF NOT EXISTS `channel_qrcode` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `channel_id` BIGINT NOT NULL COMMENT '渠道商ID',
  `qrcode_url` VARCHAR(500) NOT NULL COMMENT '二维码URL',
  `qrcode_scene` VARCHAR(200) NOT NULL COMMENT '二维码参数',
  `scan_count` INT NOT NULL DEFAULT '0' COMMENT '扫码次数',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_channel_id` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道二维码表';

-- =========================================
-- 5. 商品模块（product_）
-- =========================================

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `parent_id` BIGINT NOT NULL DEFAULT '0' COMMENT '父分类ID',
  `order_num` INT DEFAULT '0' COMMENT '排序',
  `icon` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
  `status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常 1停用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品信息表
CREATE TABLE IF NOT EXISTS `product_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `region_id` BIGINT NOT NULL COMMENT '所属地区ID',
  `description` TEXT COMMENT '商品描述',
  `images` VARCHAR(2000) DEFAULT '' COMMENT '商品图片，多个用逗号分隔',
  `min_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '最低价格',
  `max_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '最高价格',
  `sales_count` INT NOT NULL DEFAULT '0' COMMENT '销量',
  `is_on_sale` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '上架状态：0下架 1上架',
  `refund_rule` VARCHAR(20) DEFAULT 'all' COMMENT '退款规则：all=全额退款 partial=不退',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_region_id` (`region_id`),
  KEY `idx_is_on_sale` (`is_on_sale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- 商品规格表
CREATE TABLE IF NOT EXISTS `product_spec` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `spec_name` VARCHAR(200) NOT NULL COMMENT '规格名称（如：红色11朵+10元刮刮乐）',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `stock` INT NOT NULL DEFAULT '0' COMMENT '库存',
  `spec_sort` INT DEFAULT '0' COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1启用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

-- 商品库存表
CREATE TABLE IF NOT EXISTS `product_stock` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `spec_id` BIGINT NOT NULL COMMENT '规格ID',
  `region_id` BIGINT NOT NULL COMMENT '地区ID',
  `stock` INT NOT NULL DEFAULT '0' COMMENT '库存数量',
  `lock_stock` INT NOT NULL DEFAULT '0' COMMENT '锁定库存',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spec_region` (`spec_id`, `region_id`),
  KEY `idx_region_id` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存表';

-- =========================================
-- 6. 订单模块（order_）
-- =========================================

-- 订单主表
CREATE TABLE IF NOT EXISTS `order_main` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `region_id` BIGINT NOT NULL COMMENT '地区ID',
  `channel_id` BIGINT DEFAULT NULL COMMENT '渠道商ID',
  `supplier_id` BIGINT DEFAULT NULL COMMENT '供货商ID',
  `address_id` BIGINT NOT NULL COMMENT '收货地址ID',
  `total_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '商品总额',
  `delivery_fee` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '配送费',
  `pay_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '实付金额',
  `commission_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '佣金金额',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `delivery_type` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '配送方式：1同城快递 2门店自取',
  `order_status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '订单状态：0待支付 1待接单 2待发货 3配送中 4已完成 5已取消 6已退款',
  `pay_status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '支付状态：0未支付 1已支付 2已退款',
  `pay_type` VARCHAR(20) DEFAULT '' COMMENT '支付方式：wxpay=微信支付',
  `transaction_id` VARCHAR(64) DEFAULT '' COMMENT '微信交易号',
  `remark` VARCHAR(500) DEFAULT '' COMMENT '订单备注',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_region_id` (`region_id`),
  KEY `idx_channel_id` (`channel_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `spec_id` BIGINT NOT NULL COMMENT '规格ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称（冗余）',
  `spec_name` VARCHAR(200) NOT NULL COMMENT '规格名称（冗余）',
  `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
  `quantity` INT NOT NULL DEFAULT '1' COMMENT '数量',
  `total_amount` DECIMAL(12,2) NOT NULL COMMENT '小计金额',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 退款申请表
CREATE TABLE IF NOT EXISTS `order_refund` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '退款ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `refund_no` VARCHAR(32) NOT NULL COMMENT '退款单号',
  `refund_amount` DECIMAL(12,2) NOT NULL COMMENT '退款金额',
  `refund_type` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '退款类型：1仅退款 2退货退款',
  `refund_reason` VARCHAR(50) NOT NULL COMMENT '退款原因',
  `refund_desc` VARCHAR(500) DEFAULT '' COMMENT '退款说明',
  `refund_status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '退款状态：0待审核 1审核通过 2审核拒绝 3退款中 4已完成',
  `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
  `reject_reason` VARCHAR(500) DEFAULT '' COMMENT '拒绝原因',
  `audit_by` VARCHAR(64) DEFAULT '' COMMENT '审核人',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_refund_status` (`refund_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款申请表';

-- =========================================
-- 7. 物流模块（logistics_）
-- =========================================

-- 物流信息表
CREATE TABLE IF NOT EXISTS `logistics_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `logistics_company` VARCHAR(50) DEFAULT '' COMMENT '物流公司',
  `logistics_no` VARCHAR(50) DEFAULT '' COMMENT '物流单号',
  `logistics_status` VARCHAR(20) DEFAULT '' COMMENT '物流状态',
  `current_location` VARCHAR(200) DEFAULT '' COMMENT '当前位置',
  `traces` TEXT COMMENT '物流轨迹JSON',
  `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `sign_time` DATETIME DEFAULT NULL COMMENT '签收时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_logistics_no` (`logistics_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- =========================================
-- 8. 收货地址模块（address_）
-- =========================================

-- 收货地址表
CREATE TABLE IF NOT EXISTS `address_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `consignee_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `consignee_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `province_code` VARCHAR(20) NOT NULL COMMENT '省份编码',
  `province_name` VARCHAR(50) NOT NULL COMMENT '省份名称',
  `city_code` VARCHAR(20) NOT NULL COMMENT '城市编码',
  `city_name` VARCHAR(50) NOT NULL COMMENT '城市名称',
  `district_code` VARCHAR(20) NOT NULL COMMENT '区县编码',
  `district_name` VARCHAR(50) NOT NULL COMMENT '区县名称',
  `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否默认：0否 1是',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `del_flag` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `spec_id` BIGINT NOT NULL COMMENT '规格ID',
  `quantity` INT NOT NULL DEFAULT '1' COMMENT '数量',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- =========================================
-- 9. 佣金模块（commission_）
-- =========================================

-- 佣金规则表
CREATE TABLE IF NOT EXISTS `commission_rule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
  `channel_id` BIGINT DEFAULT NULL COMMENT '渠道商ID（为空则默认规则）',
  `commission_rate` DECIMAL(5,4) NOT NULL COMMENT '佣金比例',
  `settlement_type` VARCHAR(20) NOT NULL DEFAULT 'week' COMMENT '结算周期：week=周结 month=月结',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1启用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_channel_id` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金规则表';

-- 佣金账单表
CREATE TABLE IF NOT EXISTS `commission_bill` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账单ID',
  `bill_no` VARCHAR(32) NOT NULL COMMENT '账单编号',
  `channel_id` BIGINT NOT NULL COMMENT '渠道商ID',
  `channel_name` VARCHAR(100) NOT NULL COMMENT '渠道商名称',
  `settlement_type` VARCHAR(20) NOT NULL COMMENT '结算周期：week=周结 month=月结',
  `period_start` DATE NOT NULL COMMENT '结算周期开始日期',
  `period_end` DATE NOT NULL COMMENT '结算周期结束日期',
  `order_count` INT NOT NULL DEFAULT '0' COMMENT '订单数量',
  `order_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `commission_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '佣金金额',
  `bill_status` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '账单状态：0待确认 1待结算 2已结算 3已打款',
  `confirm_time` DATETIME DEFAULT NULL COMMENT '确认时间',
  `settle_time` DATETIME DEFAULT NULL COMMENT '结算时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '打款时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_channel_id` (`channel_id`),
  KEY `idx_bill_status` (`bill_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金账单表';

-- 佣金明细表
CREATE TABLE IF NOT EXISTS `commission_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `bill_id` BIGINT DEFAULT NULL COMMENT '账单ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
  `channel_id` BIGINT NOT NULL COMMENT '渠道商ID',
  `channel_name` VARCHAR(100) NOT NULL COMMENT '渠道商名称',
  `order_amount` DECIMAL(12,2) NOT NULL COMMENT '订单金额',
  `commission_rate` DECIMAL(5,4) NOT NULL COMMENT '佣金比例',
  `commission_amount` DECIMAL(12,2) NOT NULL COMMENT '佣金金额',
  `settlement_type` VARCHAR(20) NOT NULL COMMENT '结算周期',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_channel_id` (`channel_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金明细表';

-- =========================================
-- 10. 资金模块（finance_）
-- =========================================

-- 账户余额表
CREATE TABLE IF NOT EXISTS `account_balance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `account_type` VARCHAR(20) NOT NULL COMMENT '账户类型：platform=平台 channel=渠道商 supplier=供货商',
  `account_id` BIGINT NOT NULL COMMENT '关联ID（渠道商ID/供货商ID）',
  `account_name` VARCHAR(100) NOT NULL COMMENT '账户名称',
  `balance` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `freeze_amount` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `total_income` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '累计收入',
  `total_expense` DECIMAL(12,2) NOT NULL DEFAULT '0.00' COMMENT '累计支出',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account_type`, `account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户余额表';

-- 资金流水表
CREATE TABLE IF NOT EXISTS `flow_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `flow_no` VARCHAR(32) NOT NULL COMMENT '流水编号',
  `account_type` VARCHAR(20) NOT NULL COMMENT '账户类型',
  `account_id` BIGINT NOT NULL COMMENT '账户ID',
  `flow_type` VARCHAR(20) NOT NULL COMMENT '流水类型：income=收入 expense=支出',
  `business_type` VARCHAR(50) NOT NULL COMMENT '业务类型：order_pay=订单支付 refund=退款 settlement=结算',
  `business_no` VARCHAR(32) NOT NULL COMMENT '业务编号（订单号/退款单号等）',
  `amount` DECIMAL(12,2) NOT NULL COMMENT '金额',
  `balance_before` DECIMAL(12,2) NOT NULL COMMENT '变动前余额',
  `balance_after` DECIMAL(12,2) NOT NULL COMMENT '变动后余额',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0失败 1成功',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_no` (`flow_no`),
  KEY `idx_account` (`account_type`, `account_id`),
  KEY `idx_business` (`business_type`, `business_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金流水表';

-- =========================================
-- 11. 营销模块（marketing_）
-- =========================================

-- Banner配置表
CREATE TABLE IF NOT EXISTS `banner_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `region_id` BIGINT NOT NULL COMMENT '地区ID（0表示全局）',
  `banner_title` VARCHAR(100) NOT NULL COMMENT '标题',
  `banner_image` VARCHAR(500) NOT NULL COMMENT '图片URL',
  `banner_link` VARCHAR(500) DEFAULT '' COMMENT '跳转链接',
  `link_type` VARCHAR(20) DEFAULT '' COMMENT '链接类型：product=商品 page=页面 url=外部链接',
  `order_num` INT DEFAULT '0' COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1启用',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_region_id` (`region_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Banner配置表';

-- 活动配置表
CREATE TABLE IF NOT EXISTS `activity_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` VARCHAR(100) NOT NULL COMMENT '活动名称',
  `activity_type` VARCHAR(50) NOT NULL COMMENT '活动类型',
  `region_id` BIGINT NOT NULL COMMENT '地区ID',
  `activity_image` VARCHAR(500) DEFAULT '' COMMENT '活动图片',
  `activity_desc` TEXT COMMENT '活动描述',
  `rule_config` TEXT COMMENT '规则配置JSON',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1启用',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_region_id` (`region_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动配置表';

-- =========================================
-- 12. 初始化数据
-- =========================================

-- 插入系统管理员角色
INSERT INTO `sys_role` (`role_name`, `role_key`, `role_sort`, `status`, `remark`) VALUES
('超级管理员', 'super_admin', 1, 0, '系统超级管理员'),
('运营管理员', 'operator', 2, 0, '运营管理人员'),
('供货商', 'supplier', 3, 0, '供货商角色'),
('渠道商', 'channel', 4, 0, '渠道商角色');

-- 插入系统管理员用户（密码: admin123）
INSERT INTO `sys_user` (`username`, `password`, `nick_name`, `user_type`, `status`, `remark`) VALUES
('admin', '0192023a7bbd73250516f069df18b800', '系统管理员', 'system', 0, '系统管理员账号');

-- 插入系统配置
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `remark`) VALUES
('年龄校验开关', 'age.check.enabled', 'false', 'N', '是否开启18+年龄校验'),
('渠道来源展示', 'channel.source.visible', 'true', 'N', '是否展示渠道来源'),
('默认佣金比例', 'commission.default.rate', '0.05', 'N', '默认佣金比例5%'),
('小程序名称', 'mini.program.name', '体彩衍生品商城', 'N', ''),
('版权信息', 'copyright', '©2026 体彩衍生品商城', 'N', '');

-- 插入字典数据
-- 订单状态
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '待支付', '0', 'order_status', 0),
(1, '待接单', '1', 'order_status', 0),
(2, '待发货', '2', 'order_status', 0),
(3, '配送中', '3', 'order_status', 0),
(4, '已完成', '4', 'order_status', 0),
(5, '已取消', '5', 'order_status', 0),
(6, '已退款', '6', 'order_status', 0);

-- 支付状态
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '未支付', '0', 'pay_status', 0),
(1, '已支付', '1', 'pay_status', 0),
(2, '已退款', '2', 'pay_status', 0);

-- 退款状态
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '待审核', '0', 'refund_status', 0),
(1, '审核通过', '1', 'refund_status', 0),
(2, '审核拒绝', '2', 'refund_status', 0),
(3, '退款中', '3', 'refund_status', 0),
(4, '已完成', '4', 'refund_status', 0);

-- 退款原因
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '商品损坏', 'damaged', 'refund_reason', 0),
(1, '商品与描述不符', 'not_match', 'refund_reason', 0),
(2, '物流太慢', 'delivery_slow', 'refund_reason', 0),
(3, '其他', 'other', 'refund_reason', 0);

-- 系统开关
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '正常', '0', 'sys_normal_disable', 0),
(1, '停用', '1', 'sys_normal_disable', 0);

-- 用户类型
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '普通用户', 'customer', 'user_type', 0),
(1, '系统用户', 'system', 'user_type', 0);

-- 配送方式
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '同城快递', '1', 'delivery_type', 0),
(1, '门店自取', '2', 'delivery_type', 0);

-- 渠道类型
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '门店', 'store', 'channel_type', 0),
(1, '商场', 'market', 'channel_type', 0),
(2, '社区', 'community', 'channel_type', 0);

-- 结算周期
INSERT INTO `sys_dict_data` (`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`) VALUES
(0, '周结', 'week', 'settlement_type', 0),
(1, '月结', 'month', 'settlement_type', 0);

-- 插入默认地区
INSERT INTO `region_info` (`region_name`, `region_code`, `parent_id`, `order_num`, `status`, `remark`) VALUES
('福州市', 'FZ', 0, 1, 0, '福建省福州市'),
('仓山区', 'FZCS', 1, 1, 0, '福州市仓山区'),
('莆田市', 'PT', 0, 2, 0, '福建省莆田市');

-- 插入默认商品分类
INSERT INTO `category` (`category_name`, `parent_id`, `order_num`, `status`, `remark`) VALUES
('鲜花', 0, 1, 0, '鲜花品类'),
('玫瑰', 1, 1, 0, '玫瑰花'),
('康乃馨', 1, 2, 0, '康乃馨'),
('生日蛋糕', 0, 2, 0, '蛋糕品类');
