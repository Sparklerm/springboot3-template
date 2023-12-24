package com.example.boot3.common.model.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.boot3.common.constants.BizConstant;
import com.example.boot3.common.enums.BizCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 接口统一返回
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "接口统一返回格式")
public class ApiResult<T> extends BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "数据体")
    private T data;

    private ApiResult() {
    }

    public ApiResult(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public static <T> ApiResult<T> success(String code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    public static <T> ApiResult<T> success(BizCodeEnum status, T data) {
        return success(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResult<T> success(BizCodeEnum status) {
        return success(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResult<T> success(T data) {
        return success(BizCodeEnum.SUCCESS, data);
    }

    public static <T> ApiResult<T> success() {
        return success(BizCodeEnum.SUCCESS, null);
    }

    public static ApiResult<String> createSuccess() {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMessage(), BizConstant.DEFAULT_CREATE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<String> createSuccess(T data) {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(),
                BizCodeEnum.SUCCESS.getMessage(),
                BizConstant.DEFAULT_CREATE_OPTION_SUCCESS_MESSAGE + " : " + data);
    }

    public static ApiResult<String> updateSuccess() {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMessage(), BizConstant.DEFAULT_UPDATE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<String> updateSuccess(T data) {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(),
                BizCodeEnum.SUCCESS.getMessage(),
                BizConstant.DEFAULT_UPDATE_OPTION_SUCCESS_MESSAGE + " : " + data);
    }

    public static ApiResult<String> deleteSuccess() {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMessage(), BizConstant.DEFAULT_DELETE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<String> deleteSuccess(T data) {
        return new ApiResult<>(BizCodeEnum.SUCCESS.getCode(),
                BizCodeEnum.SUCCESS.getMessage(),
                BizConstant.DEFAULT_DELETE_OPTION_SUCCESS_MESSAGE + " : " + data);
    }

    public static <T> ApiResult<T> error(String code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }

    public static <T> ApiResult<T> error(String code, String message) {
        return error(code, message, null);
    }

    public static <T> ApiResult<T> error(BizCodeEnum status, T data) {
        return error(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResult<T> error(BizCodeEnum status) {
        return error(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResult<T> error(T data) {
        return error(BizCodeEnum.ERROR, data);
    }

    public static <T> ApiResult<T> error() {
        return error(BizCodeEnum.ERROR, null);
    }

    // ==================== 以下为分页返回 ====================

    public static <T> ApiResult<PageResultRecord<T>> success(IPage<T> data) {
        return success(PageResultRecord.page2Result(data));
    }

    public static <T> ApiResult<PageResultRecord<T>> success(Long total, List<T> data) {
        return success(PageResultRecord.page2Result(total, data));
    }
}

