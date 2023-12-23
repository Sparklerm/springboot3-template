package com.example.boot3.config.security;

import cn.hutool.crypto.SmUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 国密SM4加密类
 *
 * @author Alex Meng
 * @createDate 2023-12-23 05:15
 */
public class Sm4PasswordEncoder implements PasswordEncoder {

    private final String privateKey;

    public Sm4PasswordEncoder(String privateKey) {
        if (privateKey.length() != 16) {
            throw new Error("SM4 key length is 16 bits");
        }
        this.privateKey = privateKey;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return SmUtil.sm4(privateKey.getBytes(StandardCharsets.UTF_8)).encryptBase64(rawPassword.toString(), StandardCharsets.UTF_8);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String decodePassword = SmUtil.sm4(privateKey.getBytes(StandardCharsets.UTF_8)).decryptStr(encodedPassword, StandardCharsets.UTF_8);
        return Objects.equals(rawPassword.toString(), decodePassword);
    }
}
