package com.example.boot3.config.security.component;

import com.google.common.collect.ObjectArrays;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Security 自定义参数
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * 加密密钥
     */
    private String sk;

    /**
     * 静态资源白名单
     */
    private String[] staticWhitelist;

    /**
     * API 白名单
     */
    private String[] apiWhitelist;

    /**
     * 白名单
     */
    private String[] whiteList;

    public String[] getWhiteList() {
        return ObjectArrays.concat(this.apiWhitelist, this.staticWhitelist, String.class);
    }
}