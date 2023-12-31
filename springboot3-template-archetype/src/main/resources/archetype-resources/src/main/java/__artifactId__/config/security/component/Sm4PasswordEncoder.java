package ${package}.${artifactId}.config.security.component;

import ${package}.${artifactId}.common.utils.encrypt.EncryptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 国密SM4加密类
 *
 * @author alex meng
 * @createDate 2023-12-23 05:15
 */
public class Sm4PasswordEncoder implements PasswordEncoder {

    private final String secretKey;

    public Sm4PasswordEncoder(String secretKey) {
        if (secretKey.length() != 16) {
            throw new Error("SM4 key length is 16 bits");
        }
        this.secretKey = secretKey;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return EncryptUtils.encryptBySm4(rawPassword.toString(), secretKey, EncryptUtils.Sm4EncryptStrType.HEX);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return EncryptUtils.matchBySm4(rawPassword.toString(), encodedPassword, secretKey);
    }
}
