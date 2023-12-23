package com.example.boot3.config.security.component;

import cn.hutool.crypto.SecureUtil;
import com.example.boot3.common.constants.RedisCacheKey;
import com.example.boot3.common.utils.JsonUtils;
import com.example.boot3.common.utils.JwtUtils;
import com.example.boot3.common.utils.StrUtils;
import com.example.boot3.common.utils.redis.RedisService;
import com.example.boot3.model.po.AdminUserPO;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 07:38
 */
@Component
public class AuthenticationJwtTokenFilter extends OncePerRequestFilter {

    @Resource
    private SecurityUserDetailsService securityUserDetailsService;
    @Resource
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader(JwtUtils.getCurrentConfig().getHeader());
        // 读取请求头中的token
        if (StringUtils.isNotBlank(requestToken)) {
            // 判断token是否有效
            boolean verifyToken = JwtUtils.isValidToken(requestToken);
            if (!verifyToken) {
                filterChain.doFilter(request, response);
            }

            // 解析token中的用户信息
            String subject = JwtUtils.getSubject(requestToken);
            if (StringUtils.isNotBlank(subject) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 解密token中的用户信息
                String decodeSubject = SecureUtil.aes(JwtUtils.getCurrentConfig().getSecretKey().getBytes()).decryptStr(subject);
                AdminUserPO userInfo = JsonUtils.toObj(decodeSubject, AdminUserPO.class);
                // 验证token是否有效
                String cacheToken =
                        redisService.getString(StrUtils.format(RedisCacheKey.AdminUser.USER_TOKEN, userInfo.getUsername()));
                if (StringUtils.isNotBlank(cacheToken) && cacheToken.equals(requestToken)) {
                    SecurityUserDetails userDetails = (SecurityUserDetails) securityUserDetailsService.loadUserByUsername(userInfo.getUsername());
                    // 保存用户信息到当前会话
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    // 将authentication填充到安全上下文
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
