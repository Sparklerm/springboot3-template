package com.example.boot3.model.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 23:56
 */
@Data
@Schema(name = "用户登录请求参数")
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(name = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
