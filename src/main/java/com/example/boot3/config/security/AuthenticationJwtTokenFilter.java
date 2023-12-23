package com.example.boot3.config.security;

import com.example.boot3.common.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Resource
    private SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader(this.tokenHeader);
        // 读取请求头中的token
        if (StringUtils.isNotBlank(requestToken)) {
            // 判断token是否有效
            boolean verifyToken = JwtUtils.isValidToken(requestToken);
            if (verifyToken) {
                // 解析token中的用户信息
                String username = JwtUtils.getSubject(requestToken);
                UserDetails userDetails = securityUserDetailsService.loadUserByUsername(username);
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
        filterChain.doFilter(request, response);
    }
}
