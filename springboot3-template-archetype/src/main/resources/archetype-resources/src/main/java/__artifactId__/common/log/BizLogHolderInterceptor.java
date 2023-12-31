package ${package}.${artifactId}.common.log;

import cn.hutool.core.util.IdUtil;
import ${package}.${artifactId}.config.security.component.SecurityDetailsContextHolder;
import ${package}.${artifactId}.config.security.component.SecurityUserDetails;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author alex meng
 * @createDate 2023-12-24 13:12
 */
@Component
public class BizLogHolderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        SecurityUserDetails userDetails = SecurityDetailsContextHolder.getContext();

        BizLog bizLog = BizLog.builder()
                .userId(ObjectUtils.isNotEmpty(userDetails) ? userDetails.getUser().getId().toString() : "")
                .requestId(StringUtils.isNotBlank(request.getHeader(BizLog.REQUEST_ID_LOG)) ?
                        request.getParameter(BizLog.REQUEST_ID_LOG) :
                        IdUtil.fastSimpleUUID())
                .clientIp(request.getRemoteHost())
                .build();
        BizLogHolder.setContext(bizLog);

        // 输入MDC信息
        MDC.put(BizLog.USER_ID_LOG, bizLog.getUserId());
        MDC.put(BizLog.REQUEST_ID_LOG, bizLog.getRequestId());
        MDC.put(BizLog.CLIENT_IP_LOG, bizLog.getClientIp());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 会话结束，清除上下文信息
        BizLogHolder.clear();
        // 清除MDC信息
        MDC.remove(BizLog.USER_ID_LOG);
        MDC.remove(BizLog.REQUEST_ID_LOG);
        MDC.remove(BizLog.CLIENT_IP_LOG);
    }
}