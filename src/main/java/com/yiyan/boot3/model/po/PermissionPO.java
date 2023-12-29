package com.yiyan.boot3.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiyan.boot3.common.model.base.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName permission
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "permission")
@Data
public class PermissionPO extends BasePO implements Serializable {
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源类型：0：API接口，1：前端菜单。默认0
     */
    private Integer resourceType;

    /**
     * 资源名
     */
    private String name;

    /**
     * 资源路径
     */
    private String path;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}