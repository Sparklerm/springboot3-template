package com.example.boot3.controller;

import com.example.boot3.common.utils.JwtUtils;
import com.example.boot3.config.security.SecurityUserDetails;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 05:01
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 模拟登录
     */
    @PostMapping("/login")
    public String login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException e) {
            return "登录失败";
        }
        return JwtUtils.generateToken(username, 60);
    }

    @GetMapping("/fun1")
    public String fun1() {
        return "fun1";
    }

    @GetMapping("/fun2")
    public String fun2() {
        return "fun2";
    }

}
