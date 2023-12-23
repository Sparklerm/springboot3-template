package com.example.boot3.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot3.dao.IRoleDao;
import com.example.boot3.model.po.RolePO;
import com.example.boot3.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * @author MENGJIAO
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-12-24 02:38:19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, RolePO>
        implements IRoleService {

}




