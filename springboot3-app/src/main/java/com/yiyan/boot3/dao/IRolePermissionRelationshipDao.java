package com.yiyan.boot3.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyan.boot3.model.po.RolePermissionRelationshipPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author alex meng
 * @description 针对表【role_permission_relationship】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see com.yiyan.boot3.model.po.RolePermissionRelationshipPO
 */
@Mapper
public interface IRolePermissionRelationshipDao extends BaseMapper<RolePermissionRelationshipPO> {

}




