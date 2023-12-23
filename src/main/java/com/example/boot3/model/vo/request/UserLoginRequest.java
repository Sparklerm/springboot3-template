package com.example.boot3.model.vo.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 23:56
 */
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;
}
