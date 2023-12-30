package ${groupId}.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author alex meng
 * @createDate 2023-12-24 00:16
 */
@Data
@Builder
@Schema(description = "用户登录结果")
public class UserLoginResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "鉴权Token")
    private String accessToken;

    @Schema(description = "刷新Token")
    private String refreshToken;
}
