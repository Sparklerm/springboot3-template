package com.example.boot3.config.security;

import com.example.boot3.common.exception.UserAccessDeniedHandler;
import com.example.boot3.common.exception.UserAuthenticationEntryPoint;
import com.example.boot3.config.security.component.AuthenticationJwtTokenFilter;
import com.example.boot3.config.security.component.PermitUrlsProperties;
import com.example.boot3.config.security.component.SecurityUserDetailsService;
import com.example.boot3.config.security.component.Sm4PasswordEncoder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

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
    private SecurityUserDetailsService securityUserDetailsService;
    @Resource
    private UserAccessDeniedHandler userAccessDeniedHandler;
    @Resource
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    @Value("${sm4.key}")
    private String sm4Key;
    @Resource
    private AuthenticationJwtTokenFilter authenticationJwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 注册密码加密Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sm4PasswordEncoder(sm4Key);
    }

    /**
     * 放行的接口
     */
    @Resource
    private PermitUrlsProperties permitUrlsProperties;

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
                                // 放行登录
                                .requestMatchers(permitUrlsProperties.getUrls().toArray(new String[0])).permitAll()
                                // 允许任意请求被已登录的用户访问
                                .anyRequest().authenticated()
//                        .anyRequest().access((authentication, object) -> {
//                            boolean isMatch = false;
//                            // 获取当前请求的URL
//                            String requestUrl = object.getRequest().getRequestURI();
//                            // 获取所有权限信息
//                            Map<String, String> apiPermission = apiPermissionMap();
//                            // 遍历权限信息匹配当前请求的URL
//                            for (Map.Entry<String, String> entry : apiPermission.entrySet()) {
//                                if (new AntPathMatcher().match(entry.getKey(), requestUrl)) {
//                                    isMatch = true;
//                                    // 匹配到接口权限，判断当前用户是否拥有该权限
//                                    Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
//                                    for (GrantedAuthority authority : authorities) {
//                                        if (entry.getValue().equals(authority.getAuthority())) {
//                                            return new AuthorizationDecision(true);
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//
//                            //说明请求的 URL 地址和数据库的地址没有匹配上，对于这种请求，统一只要登录就能访问
//                            if (!isMatch) {
//                                if (authentication.get() instanceof AnonymousAuthenticationToken) {
//                                    return new AuthorizationDecision(false);
//                                }
//                                return new AuthorizationDecision(true);
//                            }
//                            return new AuthorizationDecision(false);
//                        })
                )
                .userDetailsService(securityUserDetailsService)
                // 添加自定义JWT过滤器
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public Map<String, String> apiPermissionMap() {
        Map<String, String> apiPermission = new HashMap<>();
        apiPermission.put("/auth/fun1", "fun1");
        apiPermission.put("/auth/fun2", "fun2");
        return apiPermission;
    }
}
