package com.example.boot3.config.security.component;

import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.exception.BizAssert;
import com.example.boot3.dao.IPermissionDao;
import com.example.boot3.dao.IRoleDao;
import com.example.boot3.dao.IUserDao;
import com.example.boot3.model.po.PermissionPO;
import com.example.boot3.model.po.RolePO;
import com.example.boot3.model.po.UserPO;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 06:17
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private IUserDao userDao;
    @Resource
    private IRoleDao roleDao;
    @Resource
    private IPermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        UserPO adminUser = userDao.selectByUsername(username);
        BizAssert.notNull(adminUser, BizCodeEnum.USER_NOT_EXIST);
        // 查询用户角色信息
        List<RolePO> hasRole = roleDao.selectByUserId(adminUser.getId());
        // 查询角色权限
        List<PermissionPO> userAllPermissions = new ArrayList<>();
        for (RolePO role : hasRole) {
            List<PermissionPO> permissions = permissionDao.selectByRoleId(role.getId());
            userAllPermissions.addAll(permissions);
        }
        return new SecurityUserDetails(adminUser, userAllPermissions);
    }
}
