package com.example.boot3.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.boot3.common.model.base.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName user_role_relationship
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_role_relationship")
@Data
public class UserRoleRelationshipPO extends BasePO implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 角色Id
     */
    private Long roleId;
}