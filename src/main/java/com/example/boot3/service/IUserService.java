package com.example.boot3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.boot3.model.dto.UserLoginResultDTO;
import com.example.boot3.model.po.UserPO;

/**
 * @author MENGJIAO
 * @description 针对表【admin_user(管理员用户表)】的数据库操作Service
 * @createDate 2023-12-23 22:39:27
 */
public interface IUserService extends IService<UserPO> {
    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param nikeName 昵称
     * @return 注册结果
     */
    Integer register(String username, String password, String nikeName);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token信息
     */
    UserLoginResultDTO login(String username, String password);

    /**
     * 退出登录
     *
     * @param username 用户名
     */
    void logout(String username);
}
