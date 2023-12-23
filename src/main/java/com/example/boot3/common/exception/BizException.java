package com.example.boot3.common.exception;

import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.enums.StatusCodeEnumFormat;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义异常类
 *
 * @author Alex Meng
 * @createDate 2022/11/9
 */
@Getter
public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 错误信息
     */
    private final String errorMessage;


    public BizException() {
        super();
        this.errorMessage = BizCodeEnum.BIZ_ERROR.getMessage();
        this.errorCode = BizCodeEnum.BIZ_ERROR.getCode();
    }

    public BizException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
        this.errorCode = BizCodeEnum.BIZ_ERROR.getCode();
    }

    public BizException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizException(StatusCodeEnumFormat errorInfoInterface) {
        super(errorInfoInterface.getCode());
        this.errorCode = errorInfoInterface.getCode();
        this.errorMessage = errorInfoInterface.getMessage();
    }

    public BizException(StatusCodeEnumFormat errorInfoInterface, String message) {
        super(errorInfoInterface.getCode());
        this.errorCode = errorInfoInterface.getCode();
        this.errorMessage = errorInfoInterface.getMessage() + " : " + message;
    }

    public BizException(StatusCodeEnumFormat errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getCode(), cause);
        this.errorCode = errorInfoInterface.getCode();
        this.errorMessage = errorInfoInterface.getMessage();
    }

    public BizException(String errorCode, String errorMessage, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
