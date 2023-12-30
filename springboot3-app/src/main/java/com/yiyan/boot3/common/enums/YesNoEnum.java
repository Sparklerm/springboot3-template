package com.yiyan.boot3.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 是否判断枚举
 *
 * @author alex meng
 * @createDate 2021-02-05
 */
@Getter
public enum YesNoEnum {

    /**
     * Yes
     */
    YES(1, "是", Boolean.TRUE),
    /**
     * No
     */
    NO(0, "否", Boolean.FALSE),
    ;

    private final Integer key;
    private final String name;
    private final Boolean value;

    YesNoEnum(Integer key, String name, Boolean value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    /**
     * 根据枚举KEY获取枚举
     *
     * @param key the key
     * @return by key
     */
    public static YesNoEnum getByKey(Integer key) {
        return Arrays.stream(values()).filter(e -> Objects.equals(key, e.key)).findFirst().orElse(null);
    }

}
