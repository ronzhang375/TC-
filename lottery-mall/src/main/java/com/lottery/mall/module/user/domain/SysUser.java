package com.lottery.mall.module.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 昵称 */
    private String nickName;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像URL */
    private String avatar;

    /** 微信OpenID */
    private String wxOpenid;

    /** 微信UnionID */
    private String wxUnionid;

    /** 用户类型：customer=普通用户 */
    private String userType;

    /** 状态：0正常 1停用 */
    private Integer status;

    /** 最后登录日期 */
    private LocalDate loginDate;

    /** 最后登录IP */
    private String loginIp;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 删除标志：0正常 1删除 */
    @TableLogic
    private Integer delFlag;

    /** 备注 */
    private String remark;
}
