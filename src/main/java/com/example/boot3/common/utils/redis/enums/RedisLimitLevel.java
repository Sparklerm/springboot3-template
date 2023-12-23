package com.example.boot3.common.utils.redis.enums;

import lombok.Getter;

/**
 * 限流级别
 *
 * @author Alex Meng
 * @createDate 2023-10-07 11:39
 */
@Getter
public enum RedisLimitLevel {
    /**
     * 方法级别
     */
    METHOD(0),
    /**
     * IP级别
     */
    IP(1);

    private final int level;

    RedisLimitLevel(int level) {
        this.level = level;
    }

}
