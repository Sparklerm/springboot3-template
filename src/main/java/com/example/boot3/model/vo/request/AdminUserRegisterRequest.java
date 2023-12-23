package com.example.boot3.model.vo.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员用户注册请求参数
 *
 * @author Alex Meng
 * @createDate 2023-12-23 23:14
 */
@Data
public class AdminUserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String nikeName;
}
