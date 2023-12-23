package com.example.boot3.common.constants;

/**
 * Redis 缓存key
 *
 * @author Alex Meng
 * @createDate 2023-11-23 07:57
 */
public class RedisCacheKey {
    /**
     * 应用Redis缓存根路径
     */
    public static final String ROOT = "boot3:";

    /**
     * 用户模块缓存KEY
     */
    public interface AdminUser {
        /**
         * 用户模块根路径
         */
        String USER_ROOT = RedisCacheKey.ROOT + "admin-user:";
        /**
         * 用户登录Token 1. username
         */
        String USER_TOKEN = USER_ROOT + "token:{0}";
    }

}
