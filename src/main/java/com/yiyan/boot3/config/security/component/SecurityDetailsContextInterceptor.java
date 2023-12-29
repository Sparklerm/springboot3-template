package com.yiyan.boot3.config.security.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * 拦截器，将当前会话的用户信息保存到上下文
 *
 * @author Alex Meng
 * @createDate 2023-11-21 01:04
 */
@Component
public class SecurityDetailsContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 保存用户信息到当前会话，无验证信息时不保存
        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {
            SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();
            SecurityDetailsContextHolder.setContext(principal);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 会话结束，清除上下文信息
        SecurityDetailsContextHolder.clear();
    }
}
