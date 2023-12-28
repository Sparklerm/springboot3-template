package com.example.boot3.config.security.component;

import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.model.result.ApiResult;
import com.example.boot3.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 请求资源路径过滤
 *
 * @author Alex Meng
 * @createDate 2023-12-28 11:56
 */
@Slf4j
@Component
public class ResourcePathFilter extends HttpFilter {

    private static List<String> apiPaths = new ArrayList<>();
    @Resource
    SecurityProperties securityProperties;
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Value("${server.servlet.context-path:''}")
    private String contextPath;

    @Override
    public void init() throws ServletException {
        // 获取所有Controller定义的接口
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        apiPaths = new ArrayList<>(handlerMethods.size());

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            // 获取Controller的类
            Class<?> controllerClass = handlerMethod.getBeanType();
            // 判断是否为@RestController注解的类，以确定是API接口
            if (AnnotatedElementUtils.hasAnnotation(controllerClass, RestController.class)) {
                // 获取PatternsRequestCondition对象
                PathPatternsRequestCondition patternsCondition = mappingInfo.getPathPatternsCondition();
                // 检查是否为null
                if (patternsCondition != null) {
                    // 获取模式集合
                    Set<String> patterns = patternsCondition.getPatternValues();
                    apiPaths.addAll(patterns);
                }
            }
        }
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取当前接口的请求路径
        String requestUrl = request.getRequestURI();
        String requestPath = StringUtils.replace(requestUrl, contextPath, "");
        PathMatcher pathMatcher = new AntPathMatcher();

        // 判断是否为接口请求
        for (String resourcePath : apiPaths) {
            if (pathMatcher.match(resourcePath, requestPath)) {
                super.doFilter(request, response, chain);
                return;
            }
        }
        // 处理静态资源请求
        String[] staticWhitelist = securityProperties.getStaticWhitelist();
        for (String staticPath : staticWhitelist) {
            if (pathMatcher.match(staticPath, requestPath)) {
                super.doFilter(request, response, chain);
                return;
            }
        }

        // 打印错误信息
        error(response);
    }

    private void error(HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(JsonUtils.toJsonStr(ApiResult.error(BizCodeEnum.RESOURCE_NOT_FOUNT)));
        } catch (IOException ex) {
            log.error("error:" + ex.getMessage());
        }
    }
}
