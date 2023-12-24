package com.example.boot3.config.security.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Security 自定义参数
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * 白名单
     */
    private List<String> whitelist;

    /**
     * 加密密钥
     */
    private String sk;

}