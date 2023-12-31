package ${package}.${artifactId}.common.utils.redis.aspect;

import cn.hutool.core.util.StrUtil;
import ${package}.${artifactId}.common.enums.BizCodeEnum;
import ${package}.${artifactId}.common.exception.BizAssert;
import ${package}.${artifactId}.common.utils.IpUtils;
import ${package}.${artifactId}.common.utils.SpElUtils;
import ${package}.${artifactId}.common.utils.redis.annotation.RedisWindowRateLimit;
import ${package}.${artifactId}.common.utils.redis.enums.RedisLimitLevel;
import ${package}.${artifactId}.common.utils.redis.enums.RedisLimitType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Redis 窗口限流切面
 *
 * @author alex meng
 * @createDate 2023-10-07 10:45
 */
@Slf4j
@Aspect
@Component
public class RedisWindowsRateLimitAspect {

    private static final String LIMIT_KEY_PREFIX = "redis-window-limiter:";

    @Autowired
    private RedissonClient redisson;

    @Pointcut("@annotation(${package}.${artifactId}.common.utils.redis.annotation.RedisWindowRateLimit)")
    public void rateLimitAnnotation() {
    }

    @Around("rateLimitAnnotation()")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取注解信息
        RedisWindowRateLimit rateLimit = method.getAnnotation(RedisWindowRateLimit.class);
        // 获取限流级别
        int level = rateLimit.level().getLevel();
        // 获取注解参数
        String prefix = StrUtil.isBlank(rateLimit.prefixKey()) ? SpElUtils.getMethodKey(method) : rateLimit.prefixKey();
        // 获取key
        StringBuilder key = new StringBuilder();
        if (level == RedisLimitLevel.IP.getLevel()) {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            key = new StringBuilder(prefix + ":" + IpUtils.getClientIp(request));
        } else {
            if (StringUtils.isNotBlank(rateLimit.key())) {
                Map<String, String> keyMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), rateLimit.key());
                if (keyMap.size() > 1) {
                    key = new StringBuilder(prefix);
                    for (Map.Entry<String, String> entry : keyMap.entrySet()) {
                        key.append(":").append(entry.getValue());
                    }
                } else {
                    key = new StringBuilder(prefix + ":" + keyMap.values().iterator().next());
                }
            }
        }
        key.insert(0, LIMIT_KEY_PREFIX);
        // 获取限流时间窗口
        int timeWindow = rateLimit.timeWindow();
        // 获取限流时间窗口内最大请求数
        int maxPermits = rateLimit.maxPermits();
        // 执行lua脚本
        RedisLimitType redisLimitType = rateLimit.type();
        if (redisLimitType == RedisLimitType.SLIDING_WINDOW_RATE_LIMIT) {
            slidingLimit(key.toString(), timeWindow, maxPermits);
        } else {
            flexLimit(key.toString(), timeWindow, maxPermits);
        }

        return joinPoint.proceed();
    }

    /**
     * 固定窗口限流
     *
     * @param key        key
     * @param timeWindow 限流时间窗口
     * @param maxPermits 限流时间窗口内最大请求数
     * @return 是否限流
     */
    private void flexLimit(String key, int timeWindow, int maxPermits) {
        // 获取lua脚本执行对象
        RScript script = redisson.getScript();
        // lua 脚本
        String lua =
                "local current = redis.call('incr', KEYS[1])/n" +
                        "if current == 1 then${symbol_escape}n" +
                        "    redis.call('expire', KEYS[1], ARGV[1])/n" +
                        "end${symbol_escape}n" +
                        "if current <= tonumber(ARGV[2]) then${symbol_escape}n" +
                        "    return 1${symbol_escape}n" +
                        "else${symbol_escape}n" +
                        "    return 0${symbol_escape}n" +
                        "end";
        // lua 脚本参数
        List<Object> params = new java.util.ArrayList<>(3);
        params.add(key);
        // 执行lua脚本
        boolean allowed = script.eval(
                RScript.Mode.READ_WRITE,
                lua,
                RScript.ReturnType.BOOLEAN,
                params,
                timeWindow,
                maxPermits
        );
        if (!allowed) {
            BizAssert.error(BizCodeEnum.REQUEST_FREQUENTLY);
        }
    }

    /**
     * 滑动窗口限流
     *
     * @param key        key
     * @param timeWindow 限流时间窗口
     * @param maxPermits 限流时间窗口内最大请求数
     * @return 是否限流
     */
    private void slidingLimit(String key, int timeWindow, int maxPermits) {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 获取窗口时间
        Double oldSet = Double.valueOf(String.valueOf(currentTime - timeWindow * 1000L));
        Double score = Double.valueOf(String.valueOf(currentTime));
        Double scoreValue = score;

        RScript script = redisson.getScript();
        String lua = "redis.call('zremrangeByScore', KEYS[1], 0, ARGV[1])/n" +
                "local res = redis.call('zcard', KEYS[1])/n" +
                "if (res == nil) or (res < tonumber(ARGV[3])) then${symbol_escape}n" +
                "    redis.call('zadd', KEYS[1], ARGV[2], ARGV[4])/n" +
                "    return 1${symbol_escape}n" +
                "else${symbol_escape}n" +
                "    return 0${symbol_escape}n" +
                "end";

        boolean allowed = script.eval(
                RScript.Mode.READ_WRITE,
                lua,
                RScript.ReturnType.BOOLEAN,
                Collections.singletonList(key),
                oldSet,
                score,
                maxPermits,
                scoreValue);

        if (!allowed) {
            BizAssert.error(BizCodeEnum.REQUEST_FREQUENTLY);
        }
    }
}
