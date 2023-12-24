package com.example.boot3.controller;

import com.example.boot3.common.model.result.ApiResult;
import com.example.boot3.config.security.component.SecurityDetailsContextHolder;
import com.example.boot3.config.security.component.SecurityUserDetails;
import com.example.boot3.model.dto.UserLoginResultDTO;
import com.example.boot3.model.po.UserPO;
import com.example.boot3.model.vo.request.UserLoginRequest;
import com.example.boot3.model.vo.request.UserRegisterRequest;
import com.example.boot3.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 23:12
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {

    @Resource
    private IUserService adminUserService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResult<String> register(@RequestBody @Valid UserRegisterRequest request) {
        adminUserService.register(request.getUsername(), request.getPassword(), request.getNikeName());
        return ApiResult.success("用户注册成功");
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResult<UserLoginResultDTO> doLogin(@RequestBody @Valid UserLoginRequest request) {
        UserLoginResultDTO loginResult = adminUserService.login(request.getUsername(), request.getPassword());
        return ApiResult.success(loginResult);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/current")
    public ApiResult<UserPO> current() {
        SecurityUserDetails userDetails = SecurityDetailsContextHolder.getContext();
        UserPO user = userDetails.getUser();
        return ApiResult.success(user);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ApiResult<String> doLogout() {
        SecurityUserDetails userDetails = SecurityDetailsContextHolder.getContext();
        UserPO user = userDetails.getUser();
        adminUserService.logout(user.getUsername());
        return ApiResult.success("退出登录成功");
    }
}
