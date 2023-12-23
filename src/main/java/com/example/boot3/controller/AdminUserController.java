package com.example.boot3.controller;

import com.example.boot3.common.model.result.ApiResult;
import com.example.boot3.model.dto.UserLoginResultDTO;
import com.example.boot3.model.vo.request.AdminUserLoginRequest;
import com.example.boot3.model.vo.request.AdminUserRegisterRequest;
import com.example.boot3.service.IAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 23:12
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Resource
    private IAdminUserService adminUserService;

    @PostMapping("/register")
    public ApiResult<String> register(@RequestBody AdminUserRegisterRequest request) {
        adminUserService.register(request.getUsername(), request.getPassword(), request.getNikeName());
        return ApiResult.success("用户注册成功");
    }

    @PostMapping("/login")
    public ApiResult<UserLoginResultDTO> doLogin(@RequestBody AdminUserLoginRequest request) {
        UserLoginResultDTO loginResult = adminUserService.login(request.getUsername(), request.getPassword());
        return ApiResult.success(loginResult);
    }
}
