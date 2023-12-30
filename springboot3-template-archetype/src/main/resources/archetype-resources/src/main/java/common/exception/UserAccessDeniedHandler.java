package ${package}.common.exception;


import ${package}.common.enums.BizCodeEnum;
import ${package}.common.model.result.ApiResult;
import ${package}.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义无权限访问的返回结果
 *
 * @author alex meng
 * @createDate 2023-11-21 01:04
 */
@Slf4j
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(JsonUtils.toJsonStr(ApiResult.error(BizCodeEnum.FORBIDDEN)));
        } catch (IOException ex) {
            log.error("error:" + ex.getMessage());
        }
    }
}