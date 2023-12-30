package com.yiyan.boot3.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiyan.boot3.model.po.RolePO;

/**
 * @author MENGJIAO
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
}
