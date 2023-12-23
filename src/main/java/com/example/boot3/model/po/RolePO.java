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
 * @TableName role
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role")
@Data
public class RolePO extends BasePO implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 默认角色：0：非默认，1：默认
     */
    private Integer isDefault;
    /**
     * 备注
     */
    private String remark;
}