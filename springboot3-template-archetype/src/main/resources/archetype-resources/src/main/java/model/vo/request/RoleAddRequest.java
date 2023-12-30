package ${package}.model.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * @author alex meng
 * @createDate 2023-12-30 11:37
 */
@Data
@Schema(description = "添加新角色请求参数")
public class RoleAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色名")
    @NotBlank(message = "角色名不可为空")
    private String name;

    @Schema(description = "默认角色：0：非默认，1：默认；默认值 0")
    private Integer isDefault;

    @Schema(description = "备注")
    private String remark;
}
