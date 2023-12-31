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
 * 管理员用户表
 *
 * @TableName admin_user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")
@Data
public class UserPO extends BasePO implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 状态 1：启用  0：禁用
     */
    private Integer status;
}