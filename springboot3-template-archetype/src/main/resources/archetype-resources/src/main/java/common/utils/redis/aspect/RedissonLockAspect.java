package ${groupId}.common.utils.redis.aspect;

import cn.hutool.core.util.StrUtil;
import ${groupId}.common.utils.SpElUtils;
import ${groupId}.common.utils.redis.RedisLockService;
import ${groupId}.common.utils.redis.annotation.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁切面
 *
 * @author alex meng
 * @createDate 2023-09-18 07:17
 */
@Slf4j
@Aspect
@Component
// 确保比事务注解先执行，分布式锁在事务外
@Order(0)
public class RedissonLockAspect {

    @Autowired
    private RedisLockService redisLockService;

    @Pointcut("@annotation(${groupId}.common.utils.redis.annotation.RedissonLock)")
    public void lockPointcut() {
    }

    @Around("lockPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        // 默认方法限定名+注解排名（可能多个）
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = prefix + ":" + SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        int waitTime = redissonLock.waitTime();
        TimeUnit timeUnit = redissonLock.unit();
        return redisLockService.executeWithLockThrows(key, waitTime, timeUnit, joinPoint::proceed);
    }
}
