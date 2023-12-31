package ${package}.${artifactId}.common.utils.redis.enums;

import lombok.Getter;

/**
 * 窗口限流类型
 *
 * @author alex meng
 * @createDate 2023-10-07 12:14
 */
@Getter
public enum RedisLimitType {
    /**
     * 固定窗口限流
     */
    FIXED_WINDOW_RATE_LIMIT(0),
    /**
     * 滑动窗口限流
     */
    SLIDING_WINDOW_RATE_LIMIT(1);

    private final int type;

    RedisLimitType(int type) {
        this.type = type;
    }
}
