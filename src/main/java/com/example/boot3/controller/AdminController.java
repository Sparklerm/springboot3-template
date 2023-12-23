package com.example.boot3.controller;

import com.example.boot3.common.model.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alex Meng
 * @createDate 2023-12-24 02:35
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/get")
    public ApiResult<String> get() {
        return ApiResult.success("这是管理员 Get API");
    }

    @GetMapping("/post")
    public ApiResult<String> post() {
        return ApiResult.success("这是管理员 Post API");
    }
}
