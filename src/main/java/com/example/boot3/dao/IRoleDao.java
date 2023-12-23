package com.example.boot3.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot3.model.po.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MENGJIAO
 * @description 针对表【role】的数据库操作DAO接口
 * @createDate 2023-12-24 02:38:19
 * @see com.example.boot3.model.po.RolePO
 */
@Mapper
public interface IRoleDao extends BaseMapper<RolePO> {

    /**
     * 根据用户Id查询用户角色
     *
     * @param userId 用户Id
     * @return 用户角色
     */
    List<RolePO> selectByUserId(@Param("userId") Long userId);
}




