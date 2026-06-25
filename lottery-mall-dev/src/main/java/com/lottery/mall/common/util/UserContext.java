package com.lottery.mall.common.util;

/**
 * 当前用户上下文（基于ThreadLocal）
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_TYPE_HOLDER = new ThreadLocal<>();

    /**
     * 设置用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 设置用户类型
     */
    public static void setUserType(String userType) {
        USER_TYPE_HOLDER.set(userType);
    }

    /**
     * 获取用户类型
     */
    public static String getUserType() {
        return USER_TYPE_HOLDER.get();
    }

    /**
     * 清除上下文
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
        USER_TYPE_HOLDER.remove();
    }
}