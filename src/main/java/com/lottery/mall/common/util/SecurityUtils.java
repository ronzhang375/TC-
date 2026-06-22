package com.lottery.mall.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import java.util.Base64;

/**
 * 工具类
 */
public class SecurityUtils {

    /**
     * 生成雪花ID
     */
    public static Long snowflakeId() {
        return IdUtil.getSnowflakeNextId();
    }

    /**
     * 生成唯一ID字符串
     */
    public static String uniqueId() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * MD5加密
     */
    public static String md5(String text) {
        return SecureUtil.md5(text);
    }

    /**
     * Base64编码
     */
    public static String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    /**
     * Base64解码
     */
    public static String base64Decode(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText));
    }

    /**
     * 密码加密（加盐）
     */
    public static String encryptPassword(String password, String salt) {
        return SecureUtil.md5(password + salt);
    }

    /**
     * 验证密码
     */
    public static boolean matchPassword(String rawPassword, String encryptedPassword, String salt) {
        return encryptPassword(rawPassword, salt).equals(encryptedPassword);
    }
}
