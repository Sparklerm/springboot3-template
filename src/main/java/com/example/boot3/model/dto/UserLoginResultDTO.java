package com.example.boot3.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Alex Meng
 * @createDate 2023-12-24 00:16
 */
@Data
@Builder
@Schema(name = "用户登录结果")
public class UserLoginResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "鉴权Token")
    private String accessToken;

    @Schema(name = "刷新Token")
    private String refreshToken;
}
