package com.lottery.mall.common.interceptor;

import com.lottery.mall.common.annotation.LoginRequired;
import com.lottery.mall.common.exception.BusinessException;
import com.lottery.mall.common.result.ResultCode;
import com.lottery.mall.common.util.JwtUtils;
import com.lottery.mall.common.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 认证拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理方法级别的请求
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        // 检查方法或类上是否有 @LoginRequired 注解
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        if (loginRequired == null) {
            loginRequired = method.getDeclaringClass().getAnnotation(LoginRequired.class);
        }

        // 不需要登录
        if (loginRequired == null) {
            return true;
        }

        // 从 Header 中获取 Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        // Bearer token 格式
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 Token
        if (!jwtUtils.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        // 解析用户信息
        Long userId = jwtUtils.getUserId(token);
        String userType = jwtUtils.getUserType(token);

        // 校验用户类型
        if (!loginRequired.value().equals(userType)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 放入上下文
        UserContext.setUserId(userId);
        UserContext.setUserType(userType);

        log.debug("认证通过: userId={}, userType={}", userId, userType);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清除上下文，避免内存泄漏
        UserContext.clear();
    }
}