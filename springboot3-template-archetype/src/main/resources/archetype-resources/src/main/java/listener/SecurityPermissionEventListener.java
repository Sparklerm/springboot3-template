package ${groupId}.listener;

import ${groupId}.common.constants.RedisCacheKey;
import ${groupId}.common.utils.redis.RedisService;
import ${groupId}.config.security.SecurityConfiguration;
import ${groupId}.model.event.SecurityPermissionEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author alex meng
 * @createDate 2023-12-30 14:51
 */
@Slf4j
@Component
public class SecurityPermissionEventListener {

    @Resource
    private RedisService redisService;

    @EventListener
    @Async("taskPool")
    public synchronized void onApplicationEvent(SecurityPermissionEvent event) {
        log.info("SecurityPermissionEventListener  开始执行 TaskId: {}", event.getTaskId());
        // 移除Redis 权限缓存
        redisService.remove(RedisCacheKey.Permission.PERMISSION_ALL);
        // 移除本地缓存
        SecurityConfiguration.clearPermissionMap();
        log.info("SecurityPermissionEventListener  执行结束 TaskId: {}", event.getTaskId());
    }
}
