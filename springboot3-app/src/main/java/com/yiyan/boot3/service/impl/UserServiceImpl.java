package com.yiyan.boot3.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.yiyan.boot3.common.constants.BizConstant;
import com.yiyan.boot3.common.constants.RedisCacheKey;
import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.enums.YesNoEnum;
import com.yiyan.boot3.common.exception.BizAssert;
import com.yiyan.boot3.common.utils.JsonUtils;
import com.yiyan.boot3.common.utils.JwtUtils;
import com.yiyan.boot3.common.utils.StrUtils;
import com.yiyan.boot3.common.utils.encrypt.EncryptUtils;
import com.yiyan.boot3.common.utils.redis.RedisService;
import com.yiyan.boot3.config.security.component.SecurityUserDetails;
import com.yiyan.boot3.dao.IRoleDao;
import com.yiyan.boot3.dao.IUserDao;
import com.yiyan.boot3.dao.IUserRoleRelationshipDao;
import com.yiyan.boot3.model.dto.UserLoginResultDTO;
import com.yiyan.boot3.model.po.RolePO;
import com.yiyan.boot3.model.po.UserPO;
import com.yiyan.boot3.model.po.UserRoleRelationshipPO;
import com.yiyan.boot3.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author alex meng
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-12-23 22:39:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<IUserDao, UserPO>
        implements IUserService {

    @Resource
    private IUserDao userDao;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisService redisService;
    @Resource
    private IRoleDao roleDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IUserRoleRelationshipDao userRoleRelationshipDao;

    @Override
    public UserPO register(String username, String password, String nikeName) {
        // 查询是否有相同用户名的用户
        UserPO hasUser = userDao.selectByUsername(username);
        BizAssert.isNull(hasUser, BizCodeEnum.USERNAME_ALREADY_REGISTER);
        // 组装新用户参数
        UserPO user = new UserPO();
        user.setUsername(username);
        user.setNickName(nikeName);
        user.setPassword(passwordEncoder.encode(password));
        transactionTemplate.execute((status -> {
            // 注册用户
            userDao.insert(user);
            // 查询默认角色
            Wrapper<RolePO> wrapper = new LambdaQueryWrapper<>(RolePO.class)
                    .eq(RolePO::getIsDefault, YesNoEnum.YES.getKey());
            List<Long> defaultRoles = roleDao.selectList(wrapper).stream().map(RolePO::getId).toList();
            List<UserRoleRelationshipPO> relationships = getUserRoleRelationshipList(defaultRoles, user.getId());
            // 绑定默认角色
            relationships.forEach(row -> userRoleRelationshipDao.insert(row));
            return Boolean.TRUE;
        }));
        return user;
    }

    private static List<UserRoleRelationshipPO> getUserRoleRelationshipList(List<Long> roleIds, Long userId) {
        List<UserRoleRelationshipPO> relationships = new ArrayList<>(roleIds.size());
        roleIds.forEach(roleId -> {
            UserRoleRelationshipPO relationship = new UserRoleRelationshipPO();
            relationship.setUserId(userId);
            relationship.setRoleId(roleId);
            relationships.add(relationship);
        });
        return relationships;
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
        String encodeSubject = EncryptUtils.encryptBySm4(JsonUtils.toJsonStr(userInfo.getUser()),
                JwtUtils.getCurrentConfig().getSecretKey(),
                EncryptUtils.Sm4EncryptStrType.HEX);
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

    @Override
    public List<RolePO> getRole(Long id) {
        return roleDao.selectByUserId(id);
    }

    @Override
    public Integer bindRole(Long id, List<Long> roleIds) {
        // 查询用户当前已有角色
        List<Long> hasRoleIds = getHasRoleIds(id);
        // 获取需要绑定的权限
        Sets.SetView<Long> difference =
                Sets.difference(new HashSet<>(roleIds), new HashSet<>(hasRoleIds));
        // 组装关系对象
        List<UserRoleRelationshipPO> relationships = getUserRoleRelationshipList(difference.stream().toList(), id);
        // 数据插入
        transactionTemplate.execute((status) -> {
            relationships.forEach(row -> userRoleRelationshipDao.insert(row));
            return Boolean.TRUE;
        });
        return relationships.size();
    }

    @Override
    public Integer unbindRole(Long id, List<Long> roleIds) {
        // 查询用户当前已有角色
        List<Long> hasRoleIds = getHasRoleIds(id);
        // 获取需要绑定的权限
        Sets.SetView<Long> intersection =
                Sets.intersection(new HashSet<>(roleIds), new HashSet<>(hasRoleIds));
        // 解绑角色，数据更新
        transactionTemplate.execute((status -> {
            intersection.forEach(roleId -> {
                Wrapper<UserRoleRelationshipPO> deleteWrapper = new LambdaQueryWrapper<>(UserRoleRelationshipPO.class)
                        .eq(UserRoleRelationshipPO::getUserId, id)
                        .eq(UserRoleRelationshipPO::getRoleId, roleId);
                userRoleRelationshipDao.delete(deleteWrapper);
            });
            return Boolean.TRUE;
        }));
        return intersection.size();
    }

    private List<Long> getHasRoleIds(Long id) {
        List<RolePO> hasRoles = getRole(id);
        return hasRoles.stream().map(RolePO::getId).toList();
    }
}




