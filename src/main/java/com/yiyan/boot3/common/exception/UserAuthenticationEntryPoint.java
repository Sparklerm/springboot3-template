package com.yiyan.boot3.common.exception;


import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.model.result.ApiResult;
import com.yiyan.boot3.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义未登录或者token失效时的返回结果
 *
 * @author Alex Meng
 * @createDate 2023-11-21 01:04
 */
@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(JsonUtils.toJsonStr(ApiResult.error(BizCodeEnum.UNAUTHORIZED)));
        } catch (IOException ex) {
            log.error("error:" + ex.getMessage());
        }
    }
}