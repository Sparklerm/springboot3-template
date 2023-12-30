package ${groupId}.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import ${groupId}.common.enums.BizCodeEnum;
import ${groupId}.common.exception.BizAssert;
import ${groupId}.dao.IPermissionDao;
import ${groupId}.dao.IRoleDao;
import ${groupId}.dao.IRolePermissionRelationshipDao;
import ${groupId}.model.po.PermissionPO;
import ${groupId}.model.po.RolePO;
import ${groupId}.model.po.RolePermissionRelationshipPO;
import ${groupId}.service.IRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author alex meng
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-12-24 02:38:19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, RolePO>
        implements IRoleService {

    @Resource
    private IRoleDao roleDao;
    @Resource
    IRolePermissionRelationshipDao rolePermissionRelationshipDao;
    @Resource
    IPermissionDao permissionDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public RolePO add(RolePO role) {
        // 查询是否存在同名角色
        Wrapper<RolePO> wrapper = new LambdaQueryWrapper<RolePO>()
                .eq(RolePO::getName, role.getName());
        RolePO rolePO = roleDao.selectOne(wrapper);
        BizAssert.isNull(rolePO, BizCodeEnum.ROLE_ALREADY_EXIST);
        // 添加角色
        roleDao.insert(role);
        return role;
    }

    @Override
    @Transactional
    public Integer bindPermission(Long id, List<Long> permissionIds) {
        // 查询角色已有权限信息
        List<Long> hasPermissionIds = getHasPermissionIds(id);
        // 获取需要绑定的权限
        Sets.SetView<Long> difference = Sets.difference(new HashSet<>(permissionIds), new HashSet<>(hasPermissionIds));
        // 组装关系对象
        List<RolePermissionRelationshipPO> relationships = new ArrayList<>(difference.size());
        difference.forEach(permissionId -> {
            RolePermissionRelationshipPO relationship = new RolePermissionRelationshipPO();
            relationship.setRoleId(id);
            relationship.setPermissionId(permissionId);
            relationships.add(relationship);
        });
        // 数据插入
        transactionTemplate.execute((status) -> {
            relationships.forEach(row -> rolePermissionRelationshipDao.insert(row));
            return Boolean.TRUE;
        });
        return relationships.size();
    }

    @Override
    public Integer unbindPermission(Long id, List<Long> permissionIds) {
        // 查询角色已有权限信息
        List<Long> hasPermissionIds = getHasPermissionIds(id);
        // 比对权限差异,获取待绑定权限
        Sets.SetView<Long> intersection = Sets.intersection(new HashSet<>(hasPermissionIds), new HashSet<>(permissionIds));
        // 解绑权限，数据更新
        transactionTemplate.execute((status -> {
            intersection.forEach(permissionId -> {
                Wrapper<RolePermissionRelationshipPO> deleteWrapper = new LambdaQueryWrapper<>(RolePermissionRelationshipPO.class)
                        .eq(RolePermissionRelationshipPO::getRoleId, id)
                        .eq(RolePermissionRelationshipPO::getPermissionId, permissionId);
                rolePermissionRelationshipDao.delete(deleteWrapper);
            });
            return Boolean.TRUE;
        }));
        return intersection.size();
    }

    private List<Long> getHasPermissionIds(Long id) {
        Wrapper<RolePermissionRelationshipPO> wrapper = new LambdaQueryWrapper<RolePermissionRelationshipPO>()
                .select(RolePermissionRelationshipPO::getPermissionId)
                .eq(RolePermissionRelationshipPO::getRoleId, id);
        List<RolePermissionRelationshipPO> hasPermission = rolePermissionRelationshipDao.selectList(wrapper);
        return hasPermission.stream().map(RolePermissionRelationshipPO::getPermissionId).toList();
    }

    @Override
    public List<PermissionPO> getPermission(Long id) {
        return permissionDao.selectByRoleId(id);
    }
}




