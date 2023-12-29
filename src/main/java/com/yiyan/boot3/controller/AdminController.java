package com.yiyan.boot3.controller;

import com.yiyan.boot3.common.model.result.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Meng
 * @createDate 2023-12-24 02:35
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin权限接口")
public class AdminController {

    @Operation(summary = "Admin Get API")
    @GetMapping("/get")
    public ApiResult<String> get() {
        return ApiResult.success("这是管理员 Get API");
    }

    @Operation(summary = "Admin Post API")
    @PostMapping("/post")
    public ApiResult<String> post() {
        return ApiResult.success("这是管理员 Post API");
    }
}
