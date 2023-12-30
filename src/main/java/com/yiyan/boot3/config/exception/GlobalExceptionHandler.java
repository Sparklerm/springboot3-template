package com.yiyan.boot3.config.exception;


import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.exception.BizException;
import com.yiyan.boot3.common.model.result.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 全局异常处理
 *
 * @author MENGJIAO
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param req the req
     * @param e   the e
     * @return ApiResult
     */
    @ExceptionHandler(value = BizException.class)
    public ApiResult<BizException> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("[ {} ] {} 请求异常: {} - {}", req.getMethod(), req.getRequestURL(), e.getErrorCode(), e.getErrorMessage());
        return ApiResult.error(e.getErrorCode(), e.getErrorMessage());
    }

    /**
     * 处理Spring Security 用户名密码校验异常
     *
     * @param req the req
     * @param e   the e
     * @return ApiResult
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ApiResult<BizException> badCredentialsExceptionHandler(HttpServletRequest req, BadCredentialsException e) {
        return ApiResult.error(BizCodeEnum.SECURITY_USERNAME_PASSWORD_ERROR);
    }


    /**
     * 处理Spring Security 用户被禁用异常
     *
     * @param req the req
     * @param e   the e
     * @return ApiResult
     */
    @ExceptionHandler(value = DisabledException.class)
    public ApiResult<BizException> disabledExceptionHandler(HttpServletRequest req, DisabledException e) {
        return ApiResult.error(BizCodeEnum.SECURITY_USER_IS_DISABLED);
    }

    /**
     * 参数异常信息返回
     *
     * @param req the req
     * @param e   the e
     * @return ApiResult
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<Map<String, String>> methodArgumentNotValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        log.error("[ {} ] {} 请求参数校验错误", req.getMethod(), req.getRequestURL());
        Map<String, String> paramExceptionInfo = new TreeMap<>();
        for (ObjectError objectError : allErrors) {
            FieldError fieldError = (FieldError) objectError;
            log.error("参数 {} = {} 校验错误：{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            paramExceptionInfo.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ApiResult.error(BizCodeEnum.PARAM_ERROR, paramExceptionInfo);
    }

    /**
     * 处理其他异常
     *
     * @param req the req
     * @param e   the e
     * @return ApiResult
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<String> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("[ {} ] {} 未定义异常: {} ,{}", req.getMethod(), req.getRequestURL(), e.getMessage(), e.toString());
        return ApiResult.error(BizCodeEnum.ERROR, e.getMessage());
    }
}

