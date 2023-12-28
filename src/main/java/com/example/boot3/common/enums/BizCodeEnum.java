package com.example.boot3.common.enums;

import lombok.Getter;

/**
 * 业务状态码枚举
 * 1xxx 系统异常，如参数校验异常、数据库异常等
 * 2xxx 业务异常，如用户不存在、用户已存在等
 *
 * @author Alex Meng
 * @createDate 2022-11-9
 */
@Getter
public enum BizCodeEnum implements StatusCodeEnumFormat {
    SUCCESS("200", "操作成功"),
    ERROR("-1", "操作失败"),
    BIZ_ERROR("500", "通用业务异常"),
    UNAUTHORIZED("401", "认证失败请重新登录"),
    FORBIDDEN("403", "权限不足无法访问"),
    RESOURCE_NOT_FOUNT("404", "请求资源不存在"),
    // === 系统异常
    PARAM_ERROR("1001", "参数错误"),
    LOCK_ERROR("1002", "获取锁失败"),
    REQUEST_FREQUENTLY("1003", "请求过于频繁，请稍后再试"),
    // === 业务异常
    SECURITY_USERNAME_PASSWORD_ERROR("2001", "用户名或密码错误"),
    SECURITY_USER_IS_DISABLED("2002", "用户已被禁用"),
    USERNAME_ALREADY_REGISTER("2003", "用户名已注册"),
    USER_NOT_EXIST("2004", "用户不存在"),
    ;

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态信息
     */
    private final String message;

    BizCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
