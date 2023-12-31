package ${package}.${artifactId}.config.security.component;

import ${package}.${artifactId}.common.enums.BizCodeEnum;
import ${package}.${artifactId}.common.exception.BizAssert;
import ${package}.${artifactId}.dao.IPermissionDao;
import ${package}.${artifactId}.dao.IRoleDao;
import ${package}.${artifactId}.dao.IUserDao;
import ${package}.${artifactId}.model.po.PermissionPO;
import ${package}.${artifactId}.model.po.RolePO;
import ${package}.${artifactId}.model.po.UserPO;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alex meng
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
