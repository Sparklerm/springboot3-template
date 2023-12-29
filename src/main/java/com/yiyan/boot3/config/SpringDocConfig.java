package com.yiyan.boot3.config;

import com.yiyan.boot3.common.utils.JwtUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 接口文档配置
 *
 * @author Alex Meng
 * @createDate 2023-12-24 05:39
 */
@Configuration
public class SpringDocConfig {

    @Resource
    private springDocsProperties springDocsProperties;

    @Bean
    public OpenAPI openApi() {
        JwtUtils.JwtConfig currentConfig = JwtUtils.getCurrentConfig();
        return new OpenAPI()
                // 文档描述信息
                .info(new Info()
                        .title(springDocsProperties.title)
                        .description(springDocsProperties.description)
                        .version(springDocsProperties.version)
                )
                // 添加全局的header参数
                .addSecurityItem(new SecurityRequirement()
                        .addList(currentConfig.getHeader()))
                .components(new Components()
                        .addSecuritySchemes(currentConfig.getHeader(), new SecurityScheme()
                                .name(currentConfig.getHeader())
                                .scheme(currentConfig.getTokenHead())
                                .bearerFormat("JWT")
                                .type(SecurityScheme.Type.HTTP))
                );
    }

    @Component
    @ConfigurationProperties(prefix = "springdocs")
    @Setter
    public static class springDocsProperties {
        private String title;
        private String description;
        private String version;
    }
}
