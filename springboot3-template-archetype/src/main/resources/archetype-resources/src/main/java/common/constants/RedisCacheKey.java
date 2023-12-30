package ${groupId}.common.constants;

/**
 * Redis 缓存key
 *
 * @author alex meng
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

    /**
     * 权限模块缓存KEY
     */
    public interface Permission {
        /**
         * 权限模块根路径
         */
        String PERMISSION_ROOT = RedisCacheKey.ROOT + "permission:";
        /**
         * 所有权限信息缓存
         */
        String PERMISSION_ALL = PERMISSION_ROOT + "all";
    }

}
