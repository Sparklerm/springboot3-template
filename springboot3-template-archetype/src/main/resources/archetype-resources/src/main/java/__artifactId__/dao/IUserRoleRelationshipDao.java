package ${package}.${artifactId}.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.${artifactId}.model.po.UserRoleRelationshipPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author alex meng
 * @description 针对表【user_role_relationship】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see ${package}.${artifactId}.model.po.UserRoleRelationshipPO
 */
@Mapper
public interface IUserRoleRelationshipDao extends BaseMapper<UserRoleRelationshipPO> {

}




