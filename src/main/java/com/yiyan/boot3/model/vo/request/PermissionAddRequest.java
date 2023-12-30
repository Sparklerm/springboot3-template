package com.yiyan.boot3.model.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Alex Meng
 * @createDate 2023-12-30 10:08
 */
@Data
@Schema(description = "添加权限请求")
public class PermissionAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源类型：0：API接口，1：前端菜单。默认0")
    private Integer resourceType;

    @Schema(description = "资源名")
    @NotBlank(message = "权限名不可为空")
    private String name;

    @Schema(description = "资源路径")
    @NotBlank(message = "资源路径不能为空")
    private String path;
}
