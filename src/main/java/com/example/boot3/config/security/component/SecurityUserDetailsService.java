package com.example.boot3.config.security.component;

import com.example.boot3.common.enums.BizCodeEnum;
import com.example.boot3.common.exception.BizAssert;
import com.example.boot3.dao.IAdminUserDao;
import com.example.boot3.model.po.AdminUserPO;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alex Meng
 * @createDate 2023-12-23 06:17
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private IAdminUserDao adminUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        AdminUserPO adminUser = adminUserDao.selectByUsername(username);
        BizAssert.notNull(adminUser, BizCodeEnum.USER_NOT_EXIST);

        return new SecurityUserDetails(adminUser);
    }
}
