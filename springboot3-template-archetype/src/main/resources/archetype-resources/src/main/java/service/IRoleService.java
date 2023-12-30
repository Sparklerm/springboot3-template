package ${package}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ${package}.model.po.PermissionPO;
import ${package}.model.po.RolePO;

import java.util.List;

/**
 * @author alex meng
 * @description 针对表【role】的数据库操作Service接口
 * @createDate 2023-12-24 02:38:19
 */
public interface IRoleService extends IService<RolePO> {

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return 创建后的角色信息
     */
    RolePO add(RolePO role);

    /**
     * 角色绑定权限
     *
     * @param id            角色Id
     * @param permissionIds 权限Id
     * @return 绑定条数
     */
    Integer bindPermission(Long id, List<Long> permissionIds);

    /**
     * 角色解绑权限
     *
     * @param id            角色Id
     * @param permissionIds 权限Id
     * @return 解绑条数
     */
    Integer unbindPermission(Long id, List<Long> permissionIds);

    /**
     * 查询角色权限
     *
     * @param id 角色Id
     * @return
     */
    List<PermissionPO> getPermission(Long id);
}
