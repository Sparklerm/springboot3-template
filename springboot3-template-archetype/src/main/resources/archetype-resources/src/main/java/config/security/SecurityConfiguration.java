package ${package}.config.security;

import cn.hutool.core.util.URLUtil;
import ${package}.common.constants.BizConstant;
import ${package}.common.constants.RedisCacheKey;
import ${package}.common.enums.YesNoEnum;
import ${package}.common.exception.UserAccessDeniedHandler;
import ${package}.common.exception.UserAuthenticationEntryPoint;
import ${package}.common.utils.redis.RedisService;
import ${package}.config.security.component.AuthenticationJwtTokenFilter;
import ${package}.config.security.component.ResourcePathFilter;
import ${package}.config.security.component.SecurityProperties;
import ${package}.config.security.component.SecurityUserDetailsService;
import ${package}.config.security.component.Sm4PasswordEncoder;
import ${package}.model.po.PermissionPO;
import ${package}.service.IPermissionService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Security配置文件
 *
 * @author alex meng
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
    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private RedisService redisService;
    @Resource
    private AuthenticationJwtTokenFilter authenticationJwtTokenFilter;
    @Resource
    private ResourcePathFilter resourcePathFilter;

    /**
     * 权限集
     */
    private static Map<String, ConfigAttribute> permissionMap = new ConcurrentHashMap<>();

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 注册密码加密Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sm4PasswordEncoder(securityProperties.getSk());
    }

    /**
     * 加载所有接口权限
     */
    @PostConstruct
    public synchronized void loadPermissions() {
        // 清空当前缓存
        clearPermissionMap();
        // 从缓存中获取
        List<PermissionPO> permissionList = redisService.getFromList(RedisCacheKey.Permission.PERMISSION_ALL);
        if (permissionList.isEmpty()) {
            // 从数据库获取
            permissionList = permissionService.list();
            // 存入缓存
            redisService.addToList(RedisCacheKey.Permission.PERMISSION_ALL, permissionList, BizConstant.DEFAULT_TOKEN_EXPIRE, ChronoUnit.SECONDS);
        }
        // 组装权限信息
        Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
        for (PermissionPO permission : permissionList) {
            map.put(permission.getPath(),
                    new org.springframework.security.access.SecurityConfig(permission.getId() + ":" + permission.getName()));
        }
        permissionMap = map;
    }

    /**
     * 清除本地缓存
     */
    public static synchronized void clearPermissionMap() {
        permissionMap.clear();
        permissionMap = null;
    }

    /**
     * Spring Security 过滤链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 白名单获取
        String[] whiteList = securityProperties.getWhiteList();

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
                        // 放行白名单
                        .requestMatchers(whiteList).permitAll()
                        // 根据权限配置进行动态过滤
                        .anyRequest().access((authentication, object) -> {
                            // 如果没有权限资源则重新加载
                            if (permissionMap == null || permissionMap.isEmpty()) {
                                loadPermissions();
                            }
                            // 获取当前的访问路径
                            String requestURI = object.getRequest().getRequestURI();
                            String path = URLUtil.getPath(requestURI);
                            PathMatcher pathMatcher = new AntPathMatcher();
                            // 白名单请求直接放行
                            for (String url : whiteList) {
                                if (pathMatcher.match(url, requestURI)) {
                                    return new AuthorizationDecision(YesNoEnum.YES.getValue());
                                }
                            }
                            // 获取访问该路径所需权限
                            List<ConfigAttribute> apiNeedPermissions = new ArrayList<>();
                            for (Map.Entry<String, ConfigAttribute> config : permissionMap.entrySet()) {
                                if (pathMatcher.match(config.getKey(), path)) {
                                    apiNeedPermissions.add(config.getValue());
                                }
                            }
                            // 如果接口没有配置权限则直接放行
                            if (apiNeedPermissions.isEmpty()) {
                                return new AuthorizationDecision(YesNoEnum.YES.getValue());
                            }
                            // 获取当前登录用户权限信息
                            Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                            // 判断当前用户是否有足够的权限访问
                            for (ConfigAttribute configAttribute : apiNeedPermissions) {
                                // 将访问所需资源和用户拥有资源进行比对
                                String needAuthority = configAttribute.getAttribute();
                                for (GrantedAuthority grantedAuthority : authorities) {
                                    if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                                        // 权限匹配放行
                                        return new AuthorizationDecision(YesNoEnum.YES.getValue());
                                    }
                                }
                            }
                            return new AuthorizationDecision(YesNoEnum.NO.getValue());
                        })
                )
                .userDetailsService(securityUserDetailsService)
                // 添加自定义过滤器
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(resourcePathFilter, AuthenticationJwtTokenFilter.class)
                .build();
    }

    /**
     * 配置跨域
     */
    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许所有域名进行跨域调用
        corsConfiguration.addAllowedOriginPattern("*");
        //允许跨越发送cookie
        corsConfiguration.setAllowCredentials(true);
        //放行全部原始头信息
        corsConfiguration.addAllowedHeader("*");
        //允许所有请求方法跨域调用
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }
}
