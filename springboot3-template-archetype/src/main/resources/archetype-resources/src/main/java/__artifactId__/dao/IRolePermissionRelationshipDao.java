package ${package}.${artifactId}.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.${artifactId}.model.po.RolePermissionRelationshipPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author alex meng
 * @description 针对表【role_permission_relationship】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see ${package}.${artifactId}.model.po.RolePermissionRelationshipPO
 */
@Mapper
public interface IRolePermissionRelationshipDao extends BaseMapper<RolePermissionRelationshipPO> {

}




