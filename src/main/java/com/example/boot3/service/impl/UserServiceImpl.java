package com.example.boot3.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot3.common.constants.BizConstant;
import com.example.boot3.common.constants.RedisCacheKey;
import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.exception.BizAssert;
import com.example.boot3.common.utils.JsonUtils;
import com.example.boot3.common.utils.JwtUtils;
import com.example.boot3.common.utils.StrUtils;
import com.example.boot3.common.utils.redis.RedisService;
import com.example.boot3.config.security.component.SecurityUserDetails;
import com.example.boot3.dao.IAdminUserDao;
import com.example.boot3.model.dto.UserLoginResultDTO;
import com.example.boot3.model.po.UserPO;
import com.example.boot3.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author MENGJIAO
 * @description 针对表【admin_user(管理员用户表)】的数据库操作Service实现
 * @createDate 2023-12-23 22:39:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<IAdminUserDao, UserPO>
        implements IUserService {

    @Resource
    private IAdminUserDao adminUserDao;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisService redisService;

    @Override
    public Integer register(String username, String password, String nikeName) {
        // 查询是否有相同用户名的用户
        UserPO hasUser = adminUserDao.selectByUsername(username);
        BizAssert.isNull(hasUser, BizCodeEnum.USERNAME_ALREADY_REGISTER);
        // 注册用户
        UserPO adminUser = new UserPO();
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
        // 将鉴权Token存入Redis
        redisService.setString(StrUtils.format(RedisCacheKey.AdminUser.USER_TOKEN, userInfo.getUsername()),
                accessToken,
                BizConstant.DEFAULT_TOKEN_EXPIRE,
                TimeUnit.SECONDS);

        return UserLoginResultDTO.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(String username) {
        // 在Redis中删除用户鉴权Token
        redisService.remove(StrUtils.format(RedisCacheKey.AdminUser.USER_TOKEN, username));
    }
}




