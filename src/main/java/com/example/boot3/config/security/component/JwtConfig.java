package com.example.boot3.config.security.component;

import com.example.boot3.common.utils.JwtUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 07:36
 */
@Slf4j
@Component
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expirationTime}")
    private long expirationTime;
    @Value("${jwt.tokenHeader}")
    private String header;

    @PostConstruct
    public void jwtInit() {
        JwtUtils.initialize(issuer, secretKey, expirationTime, header);
        log.info("JwtUtils初始化完成");
    }
}
