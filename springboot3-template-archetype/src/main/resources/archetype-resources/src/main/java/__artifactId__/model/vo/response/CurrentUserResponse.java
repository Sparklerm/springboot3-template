package ${package}.${artifactId}.model.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author alex meng
 * @createDate 2023-12-30 11:07
 */
@Data
@Schema(description = "用户登录结果")
public class CurrentUserResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键Id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickName;

}
