package ${groupId}.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${groupId}.common.enums.BizCodeEnum;
import ${groupId}.common.exception.BizAssert;
import ${groupId}.common.model.result.ApiResult;
import ${groupId}.common.model.result.PageResultRecord;
import ${groupId}.model.po.PermissionPO;
import ${groupId}.model.vo.request.PermissionAddRequest;
import ${groupId}.model.vo.request.PermissionPageRequest;
import ${groupId}.service.IPermissionService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author alex meng
 * @createDate 2023-12-29 17:40
 */
@RestController
@RequestMapping("/permission")
@Tag(name = "权限接口")
public class PermissionController {

    @Resource
    private IPermissionService permissionService;

    @Operation(summary = "添加权限")
    @PostMapping("/")
    public ApiResult<PermissionPO> addPermission(@Valid @RequestBody PermissionAddRequest request) {
        // 值填充
        PermissionPO permission = new PermissionPO();
        permission.setResourceType(request.getResourceType());
        permission.setName(request.getName());
        permission.setPath(request.getPath());
        // 添加权限
        PermissionPO addResult = permissionService.add(permission);
        return ApiResult.created(addResult);
    }

    @Operation(summary = "查询权限")
    @GetMapping("/{id}")
    public ApiResult<PermissionPO> getPermission(@PathVariable("id") String id) {
        PermissionPO permission = permissionService.getById(id);
        return ApiResult.success(permission);
    }

    @Operation(summary = "查询权限列表")
    @GetMapping("/")
    public ApiResult<PageResultRecord<PermissionPO>> getPermissions(PermissionPageRequest request) {
        // 参数填充
        Page<PermissionPO> permissionPage = request.getPage();
        Wrapper<PermissionPO> wrapper = new LambdaQueryWrapper<>(PermissionPO.class)
                .like(StringUtils.isNotBlank(request.getName()), PermissionPO::getName, request.getName())
                .like(StringUtils.isNotBlank(request.getPath()), PermissionPO::getPath, request.getPath())
                .eq(ObjectUtils.isNotEmpty(request.getResourceType()), PermissionPO::getResourceType, request.getResourceType());

        // 查询
        Page<PermissionPO> page = permissionService.page(permissionPage, wrapper);
        return ApiResult.success(page);
    }

    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    public ApiResult<PermissionPO> updatePermission(@PathVariable("id") Long id,
                                                    @RequestBody PermissionAddRequest request) {
        // 查询权限
        PermissionPO selectResult = permissionService.getById(id);
        BizAssert.notNull(selectResult, BizCodeEnum.PERMISSION_NOT_EXIST);
        // 参数填充
        selectResult.setResourceType(request.getResourceType());
        selectResult.setName(request.getName());
        selectResult.setPath(request.getPath());
        permissionService.updateById(selectResult);
        return ApiResult.updated(selectResult);
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> deletePermission(@PathVariable("id") Long id) {
        boolean remove = permissionService.removeById(id);
        return ApiResult.deleted(remove);
    }

    @Operation(summary = "批量删除权限")
    @DeleteMapping("/")
    public ApiResult<Boolean> deletePermissions(@RequestBody List<Long> ids) {
        boolean remove = permissionService.removeByIds(ids);
        return ApiResult.deleted(remove);
    }
}
