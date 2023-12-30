package ${groupId}.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${groupId}.model.po.PermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author alex meng
 * @description 针对表【permission】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see ${groupId}.model.po.PermissionPO
 */
@Mapper
public interface IPermissionDao extends BaseMapper<PermissionPO> {

    /**
     * 根据角色查询权限信息
     *
     * @param roleId 角色Id
     * @return 角色权限
     */
    List<PermissionPO> selectByRoleId(@Param("roleId") Long roleId);
}




