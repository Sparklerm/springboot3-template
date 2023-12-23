package com.example.boot3.common.exception;


import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.model.result.ApiResult;
import com.example.boot3.common.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义无权限访问的返回结果
 *
 * @author Alex Meng
 * @createDate 2023-11-21 01:04
 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        ApiResult<String> result = ApiResult.error(BizCodeEnum.FORBIDDEN);
        response.getWriter().println(JsonUtils.toJson(result));
        response.getWriter().flush();
    }
}