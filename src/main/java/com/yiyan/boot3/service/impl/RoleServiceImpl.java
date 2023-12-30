package com.yiyan.boot3.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.exception.BizAssert;
import com.yiyan.boot3.dao.IRoleDao;
import com.yiyan.boot3.model.po.RolePO;
import com.yiyan.boot3.service.IRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author MENGJIAO
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-12-24 02:38:19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, RolePO>
        implements IRoleService {

    @Resource
    private IRoleDao roleDao;

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
}




