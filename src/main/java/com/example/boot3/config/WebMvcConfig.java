package com.example.boot3.config;

import com.example.boot3.config.security.component.SecurityDetailsContextInterceptor;
import com.example.boot3.config.security.component.SecurityProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Alex Meng
 * @createDate 2023-12-24 01:28
 */
@Slf4j
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private SecurityDetailsContextInterceptor securityDetailsContextInterceptor;

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 会话上下文拦截器
        registry.addInterceptor(securityDetailsContextInterceptor)
                // 排除白名单
                .excludePathPatterns(securityProperties.getWhitelist())
                // 拦截路径
                .addPathPatterns("/**");
    }
}
