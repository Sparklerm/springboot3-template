package ${groupId}.common.model.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ${groupId}.common.constants.BizConstant;
import ${groupId}.common.enums.BizCodeEnum;
import ${groupId}.common.enums.YesNoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 接口统一返回
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "接口统一返回格式")
public class ApiResult<T> extends BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据体")
    private T data;

    private ApiResult() {
    }

    public ApiResult(String code, String message, boolean success, T data) {
        super(code, message, success);
        this.data = data;
    }

    public static <T> ApiResult<T> success(String code, String message, T data) {
        return new ApiResult<>(code, message, YesNoEnum.YES.getValue(), data);
    }

    public static <T> ApiResult<T> success(BizCodeEnum status, T data) {
        return success(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResult<T> success(BizCodeEnum status) {
        return success(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResult<T> success(T data) {
        return success(BizCodeEnum.SUCCESS, data);
    }

    public static <T> ApiResult<T> success() {
        return success(BizCodeEnum.SUCCESS, null);
    }

    public static <T> ApiResult<T> error(String code, String message, T data) {
        return new ApiResult<>(code, message, YesNoEnum.NO.getValue(), data);
    }

    public static <T> ApiResult<T> error(String code, String message) {
        return error(code, message, null);
    }

    public static <T> ApiResult<T> error(BizCodeEnum status, T data) {
        return error(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResult<T> error(BizCodeEnum status) {
        return error(status.getCode(), status.getMessage(), null);
    }

    public static <T> ApiResult<T> error(T data) {
        return error(BizCodeEnum.ERROR, data);
    }

    public static <T> ApiResult<T> error() {
        return error(BizCodeEnum.ERROR, null);
    }


    // ==================== 特定操作类型返回 ====================

    public static ApiResult<String> created() {
        return success(BizConstant.DEFAULT_CREATE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<T> created(T data) {
        return success(data);
    }

    public static ApiResult<String> updated() {
        return success(BizConstant.DEFAULT_UPDATE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<T> updated(T data) {
        return success(data);
    }

    public static ApiResult<String> deleted() {
        return success(BizConstant.DEFAULT_DELETE_OPTION_SUCCESS_MESSAGE);
    }

    public static <T> ApiResult<T> deleted(T data) {
        return success(data);
    }

    // ==================== 以下为分页数据封装返回 ====================

    public static <T> ApiResult<PageResultRecord<T>> success(IPage<T> data) {
        return success(PageResultRecord.page2Result(data));
    }

    public static <T> ApiResult<PageResultRecord<T>> success(Long total, List<T> data) {
        return success(PageResultRecord.page2Result(total, data));
    }
}

