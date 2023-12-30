package ${package}.config.security.component;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Spring Security当前会话用户信息保存
 *
 * @author alex meng
 * @createDate 2023-11-21 01:04
 */
public class SecurityDetailsContextHolder {

    private static final TransmittableThreadLocal<SecurityUserDetails> THREAD_LOCAL = new TransmittableThreadLocal<>();

    private SecurityDetailsContextHolder() {
    }

    public static SecurityUserDetails getContext() {
        return THREAD_LOCAL.get();
    }

    public static void setContext(SecurityUserDetails t) {
        THREAD_LOCAL.set(t);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}