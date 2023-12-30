package ${groupId}.common.model.event;

import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author alex meng
 * @createDate 2023-12-30 14:45
 */
@Data
@Schema(description = "Base事件对象")
public abstract class BaseEvent<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "事件Id")
    private String taskId = IdUtil.fastSimpleUUID();

    @Schema(description = "事件数据")
    private T data;

}
