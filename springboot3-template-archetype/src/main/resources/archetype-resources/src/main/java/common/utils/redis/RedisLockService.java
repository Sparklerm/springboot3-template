package ${groupId}.common.utils.redis;

import ${groupId}.common.enums.BizCodeEnum;
import ${groupId}.common.exception.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 基于Redis Redisson 分布式锁工具类
 *
 * @author alex meng
 * @createDate 2022-12-21
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisLockService {

    private final RedissonClient redissonClient;

    // 编程式Redisson锁
    public <T> T executeWithLockThrows(String key, int waitTime, TimeUnit unit, SupplierThrow<T> supplier) throws Throwable {
        RLock lock = redissonClient.getLock(key);
        log.info("[Redisson 分布式] 获取锁 KEY ：{}", key);
        boolean lockSuccess = lock.tryLock(waitTime, unit);
        if (!lockSuccess) {
            BizAssert.error(BizCodeEnum.LOCK_ERROR);
        }
        try {
            return supplier.execute();//执行锁内的代码逻辑
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[Redisson 分布式锁] 释放锁 KEY ：{}", key);
            }
        }
    }

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier) {
        return executeWithLockThrows(key, waitTime, unit, supplier::get);
    }

    public <T> T executeWithLock(String key, Supplier<T> supplier) {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
    }

    /**
     * 函数式接口，用于执行锁内的代码逻辑
     */
    @FunctionalInterface
    public interface SupplierThrow<T> {
        T execute() throws Throwable;
    }
}
