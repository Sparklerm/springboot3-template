package ${package}.model.vo.request;

import ${package}.common.model.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author alex meng
 * @createDate 2023-12-30 11:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色分页查询参数")
public class RolePageRequest extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "默认角色")
    private Integer isDefault;
}
