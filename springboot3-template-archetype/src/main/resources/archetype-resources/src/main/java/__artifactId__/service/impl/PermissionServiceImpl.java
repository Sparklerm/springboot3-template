package ${package}.${artifactId}.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${package}.${artifactId}.common.enums.BizCodeEnum;
import ${package}.${artifactId}.common.exception.BizAssert;
import ${package}.${artifactId}.dao.IPermissionDao;
import ${package}.${artifactId}.model.event.SecurityPermissionEvent;
import ${package}.${artifactId}.model.po.PermissionPO;
import ${package}.${artifactId}.service.IPermissionService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author alex meng
 * @description 针对表【permission】的数据库操作Service实现
 * @createDate 2023-12-24 02:38:19
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<IPermissionDao, PermissionPO>
        implements IPermissionService {

    @Resource
    private IPermissionDao permissionDao;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    public PermissionPO add(PermissionPO permission) {
        // 判断资源路径是否已经创建权限
        Wrapper<PermissionPO> wrapper = new LambdaQueryWrapper<>(PermissionPO.class)
                .eq(PermissionPO::getPath, permission.getPath());
        PermissionPO permissionPO = permissionDao.selectOne(wrapper);
        BizAssert.isNull(permissionPO, BizCodeEnum.PERMISSION_ALREADY_EXISTS);
        // 创建权限
        int insert = permissionDao.insert(permission);
        if (insert > 0) {
            eventPublisher.publishEvent(new SecurityPermissionEvent());
        }
        return permission;
    }
}




