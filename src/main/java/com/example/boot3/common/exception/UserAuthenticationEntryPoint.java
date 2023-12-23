package com.example.boot3.common.exception;


import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.model.result.ApiResult;
import com.example.boot3.common.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义未登录或者token失效时的返回结果
 *
 * @author Alex Meng
 * @createDate 2023-11-21 01:04
 */
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        ApiResult<String> result = ApiResult.error(BizCodeEnum.UNAUTHORIZED);
        response.getWriter().println(JsonUtils.toJson(result));
        response.getWriter().flush();
    }
}