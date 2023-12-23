package com.example.boot3.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.exception.BizAssert;
import com.example.boot3.common.utils.JsonUtils;
import com.example.boot3.common.utils.JwtUtils;
import com.example.boot3.config.security.SecurityUserDetails;
import com.example.boot3.dao.IAdminUserDao;
import com.example.boot3.model.dto.UserLoginResultDTO;
import com.example.boot3.model.po.AdminUserPO;
import com.example.boot3.service.IAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author MENGJIAO
 * @description 针对表【admin_user(管理员用户表)】的数据库操作Service实现
 * @createDate 2023-12-23 22:39:27
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<IAdminUserDao, AdminUserPO>
        implements IAdminUserService {

    @Resource
    private IAdminUserDao adminUserDao;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Integer register(String username, String password, String nikeName) {
        // 查询是否有相同用户名的用户
        AdminUserPO hasUser = adminUserDao.selectByUsername(username);
        BizAssert.isNull(hasUser, BizCodeEnum.USERNAME_ALREADY_REGISTER);
        // 注册用户
        AdminUserPO adminUser = new AdminUserPO();
        adminUser.setUsername(username);
        adminUser.setNickName(nikeName);
        adminUser.setPassword(passwordEncoder.encode(password));
        return adminUserDao.insert(adminUser);
    }

    @Override
    public UserLoginResultDTO login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 判断是否验证成功
        BizAssert.notNull(authentication, BizCodeEnum.USER_NOT_EXIST);
        // 在认证信息authenticate中获取登录成功后的用户信息
        SecurityUserDetails userInfo = (SecurityUserDetails) authentication.getPrincipal();
        // 将当前登录用户信息存入Token
        String encodeSubject =
                SecureUtil.aes(JwtUtils.getCurrentConfig().getSecretKey().getBytes()).encryptHex(JsonUtils.toJson(userInfo.getUser()));
        String accessToken = JwtUtils.generateToken(encodeSubject);

        return UserLoginResultDTO.builder()
                .accessToken(accessToken)
                .build();
    }
}




