package com.example.boot3.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot3.model.po.RolePermissionRelationshipPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author MENGJIAO
 * @description 针对表【role_permission_relationship】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see com.example.boot3.model.po.RolePermissionRelationshipPO
 */
@Mapper
public interface IRolePermissionRelationshipDao extends BaseMapper<RolePermissionRelationshipPO> {

}




