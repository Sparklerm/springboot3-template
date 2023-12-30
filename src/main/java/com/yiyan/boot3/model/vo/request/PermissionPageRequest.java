package com.yiyan.boot3.model.vo.request;

import com.yiyan.boot3.common.model.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Alex Meng
 * @createDate 2023-12-30 10:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "权限分页查询条件")
public class PermissionPageRequest extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限名")
    private String name;

    @Schema(description = "资源路径")
    private String path;

    @Schema(description = "资源类型")
    private Integer resourceType;
}

