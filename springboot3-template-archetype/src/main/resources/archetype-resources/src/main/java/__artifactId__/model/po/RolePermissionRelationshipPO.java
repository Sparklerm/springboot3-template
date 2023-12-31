package ${package}.${artifactId}.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import ${package}.${artifactId}.common.model.base.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName role_permission_relationship
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role_permission_relationship")
@Data
public class RolePermissionRelationshipPO extends BasePO implements Serializable {
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色Id
     */
    private Long roleId;

    /**
     * 权限Id
     */
    private Long permissionId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}