package com.example.boot3.common.model.result;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础返回实体
 *
 * @author Alex Meng
 * @createDate 2020-11-13 13:17
 */
@Data
public class BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求Status
     */
    private String code;

    /**
     * 业务信息
     */
    private String message;

    public BaseResult() {
    }

    public BaseResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
