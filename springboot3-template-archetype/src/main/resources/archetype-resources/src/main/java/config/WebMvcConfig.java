package ${groupId}.config;

import ${groupId}.common.log.BizLogHolderInterceptor;
import ${groupId}.config.security.component.SecurityDetailsContextInterceptor;
import ${groupId}.config.security.component.SecurityProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author alex meng
 * @createDate 2023-12-24 01:28
 */
@Slf4j
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private BizLogHolderInterceptor bizLogHolderInterceptor;
    @Resource
    private SecurityDetailsContextInterceptor securityDetailsContextInterceptor;

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 会话上下文拦截器
        registry.addInterceptor(securityDetailsContextInterceptor)
                // 排除白名单
                .excludePathPatterns(securityProperties.getStaticWhitelist())
                .excludePathPatterns(securityProperties.getApiWhitelist())
                .addPathPatterns("/**")
                .order(1);
        // 业务日志拦截器
        registry.addInterceptor(bizLogHolderInterceptor)
                // 排除静态资源白名单
                .excludePathPatterns(securityProperties.getStaticWhitelist())
                .addPathPatterns("/**")
                .order(2);
    }
}
