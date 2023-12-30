package com.yiyan.boot3.controller;

import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.exception.BizAssert;
import com.yiyan.boot3.common.model.result.ApiResult;
import com.yiyan.boot3.config.security.component.SecurityDetailsContextHolder;
import com.yiyan.boot3.config.security.component.SecurityUserDetails;
import com.yiyan.boot3.model.dto.UserLoginResultDTO;
import com.yiyan.boot3.model.po.RolePO;
import com.yiyan.boot3.model.po.UserPO;
import com.yiyan.boot3.model.vo.request.UserLoginRequest;
import com.yiyan.boot3.model.vo.request.UserRegisterRequest;
import com.yiyan.boot3.model.vo.response.CurrentUserResponse;
import com.yiyan.boot3.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author alex meng
 * @createDate 2023-12-23 23:12
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {

    @Resource
    private IUserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResult<CurrentUserResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        UserPO register =
                userService.register(request.getUsername(), request.getPassword(), request.getNikeName());
        // 注册结果
        CurrentUserResponse currentUser = new CurrentUserResponse();
        currentUser.setId(register.getId());
        currentUser.setUsername(register.getUsername());
        currentUser.setNickName(register.getNickName());
        return ApiResult.created(currentUser);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResult<UserLoginResultDTO> doLogin(@RequestBody @Valid UserLoginRequest request) {
        UserLoginResultDTO loginResult = userService.login(request.getUsername(), request.getPassword());
        return ApiResult.success(loginResult);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/current")
    public ApiResult<CurrentUserResponse> current() {
        // 获取鉴权用户信息
        SecurityUserDetails userDetails = SecurityDetailsContextHolder.getContext();
        UserPO user = userDetails.getUser();
        // 用户信息返回
        CurrentUserResponse currentUser = new CurrentUserResponse();
        currentUser.setId(user.getId());
        currentUser.setUsername(user.getUsername());
        currentUser.setNickName(user.getNickName());
        return ApiResult.success(currentUser);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ApiResult<String> doLogout() {
        SecurityUserDetails userDetails = SecurityDetailsContextHolder.getContext();
        UserPO user = userDetails.getUser();
        userService.logout(user.getUsername());
        return ApiResult.success("退出登录成功");
    }

    @Operation(summary = "查询用户角色")
    @GetMapping("/role/{id}")
    public ApiResult<List<RolePO>> getRoles(@PathVariable("id") Long id) {
        List<RolePO> roleList = userService.getRole(id);
        return ApiResult.success(roleList);
    }

    @Operation(description = "用户绑定角色")
    @PostMapping("/role/{id}/bind")
    public ApiResult<Integer> bindRole(@PathVariable("id") Long id, @RequestBody List<Long> roleIds) {
        // 判断用户是否存在
        UserPO user = userService.getById(id);
        BizAssert.notNull(user, BizCodeEnum.USER_NOT_EXIST);
        // 绑定角色
        Integer bind = userService.bindRole(id, roleIds);
        return ApiResult.success(bind);
    }

    @Operation(description = "用户解绑角色")
    @PostMapping("/role/{id}/unbind")
    public ApiResult<Integer> unbindRole(@PathVariable("id") Long id, @RequestBody List<Long> roleIds) {
        // 判断用户是否存在
        UserPO user = userService.getById(id);
        BizAssert.notNull(user, BizCodeEnum.USER_NOT_EXIST);
        // 解绑角色
        Integer unbind = userService.unbindRole(id, roleIds);
        return ApiResult.success(unbind);
    }
}
