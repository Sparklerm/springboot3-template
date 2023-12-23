package com.example.boot3.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot3.model.po.AdminUserPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author MENGJIAO
 * @description 针对表【admin_user(管理员用户表)】的数据库操作Mapper
 * @createDate 2023-12-23 22:39:27
 * @see com.example.boot3.model.po.AdminUserPO
 */
public interface IAdminUserDao extends BaseMapper<AdminUserPO> {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    AdminUserPO selectByUsername(@Param("username") String username);
}




