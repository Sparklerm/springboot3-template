package com.example.boot3.model.dto;

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
public class UserLoginResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鉴权Token
     */
    private String accessToken;
}
