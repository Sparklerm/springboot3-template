package com.example.boot3.common.utils.encrypt;

import cn.hutool.crypto.SmUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 加密工具类
 *
 * @author Alex Meng
 * @createDate 2023-12-24 11:43
 */
public class EncryptUtils {

    private EncryptUtils() {
    }

    /**
     * SM4加密
     *
     * @param content        待加密字符
     * @param secretKey      秘钥
     * @param encryptStrType 加密后的字符串类型
     * @return 加密后的字符串
     */
    public static String encryptBySm4(String content, String secretKey, Sm4EncryptStrType encryptStrType) {
        if (encryptStrType == Sm4EncryptStrType.HEX) {
            return SmUtil.sm4(secretKey.getBytes(StandardCharsets.UTF_8)).encryptHex(content, StandardCharsets.UTF_8);
        }
        return SmUtil.sm4(secretKey.getBytes(StandardCharsets.UTF_8)).encryptBase64(content, StandardCharsets.UTF_8);
    }

    /**
     * SM4解密
     *
     * @param encodeContent 加密字符串
     * @param secretKey     密钥
     * @return 解密后的字符串
     */
    public static String decryptBySm4(String encodeContent, String secretKey) {
        return SmUtil.sm4(secretKey.getBytes(StandardCharsets.UTF_8)).decryptStr(encodeContent, StandardCharsets.UTF_8);
    }

    /**
     * SM4 明文-密文匹配
     *
     * @param content       明文
     * @param encodeContent 密文
     * @param secretKey     密钥
     * @return 是否匹配
     */
    public static boolean matchBySm4(String content, String encodeContent, String secretKey) {
        return Objects.equals(content, decryptBySm4(encodeContent, secretKey));
    }

    public enum Sm4EncryptStrType {
        BASE64, HEX
    }
}
