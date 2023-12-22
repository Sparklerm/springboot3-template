package com.example.boot3.config.security;

import com.example.boot3.common.exception.UserAccessDeniedHandler;
import com.example.boot3.common.exception.UserAuthenticationEntryPoint;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置文件
 *
 * @author Alex Meng
 * @createDate 2023-12-23 04:55
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private UserAccessDeniedHandler userAccessDeniedHandler;
    @Resource
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    /**
     * 注册密码加密Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sm4PasswordEncoder("1234567890123456");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Spring Security 过滤链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // 禁用明文验证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用默认登录页
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用默认登出页
                .logout(AbstractHttpConfigurer::disable)
                // 禁用session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 异常配置
                .exceptionHandling(exception -> exception
                        // 登录或未登录过期异常
                        .authenticationEntryPoint(userAuthenticationEntryPoint)
                        // 无权限异常
                        .accessDeniedHandler(userAccessDeniedHandler)
                )
                // 配置拦截信息
                .authorizeHttpRequests(authorization -> authorization
                        // 允许所有的OPTIONS请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 放行登录接口
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        // 允许任意请求被已登录的用户访问
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .build();
    }
}
