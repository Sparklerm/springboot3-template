package com.yiyan.boot3.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiyan.boot3.common.enums.BizCodeEnum;
import com.yiyan.boot3.common.exception.BizAssert;
import com.yiyan.boot3.common.model.result.ApiResult;
import com.yiyan.boot3.common.model.result.PageResultRecord;
import com.yiyan.boot3.model.po.PermissionPO;
import com.yiyan.boot3.model.po.RolePO;
import com.yiyan.boot3.model.vo.request.RoleAddRequest;
import com.yiyan.boot3.model.vo.request.RolePageRequest;
import com.yiyan.boot3.service.IRoleService;
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
@RequestMapping("/role")
@Tag(name = "角色接口")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @Operation(summary = "添加角色")
    @PostMapping("/")
    public ApiResult<RolePO> addRole(@Valid @RequestBody RoleAddRequest request) {
        // 值填充
        RolePO role = new RolePO();
        role.setName(request.getName());
        role.setIsDefault(request.getIsDefault());
        role.setRemark(request.getRemark());

        // 添加权限
        RolePO addResult = roleService.add(role);
        return ApiResult.created(addResult);
    }

    @Operation(summary = "查询角色")
    @GetMapping("/{id}")
    public ApiResult<RolePO> getRole(@PathVariable("id") String id) {
        RolePO role = roleService.getById(id);
        return ApiResult.success(role);
    }

    @Operation(summary = "查询角色列表")
    @GetMapping("/")
    public ApiResult<PageResultRecord<RolePO>> getRoles(RolePageRequest request) {
        // 参数填充
        Page<RolePO> rolePage = request.getPage();
        Wrapper<RolePO> wrapper = new LambdaQueryWrapper<>(RolePO.class)
                .like(StringUtils.isNotBlank(request.getName()), RolePO::getName, request.getName())
                .eq(ObjectUtils.isNotEmpty(request.getIsDefault()), RolePO::getIsDefault, request.getIsDefault());

        // 查询
        Page<RolePO> page = roleService.page(rolePage, wrapper);
        return ApiResult.success(page);
    }

    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public ApiResult<RolePO> updateRole(@PathVariable("id") Long id,
                                        @RequestBody RoleAddRequest request) {
        // 查询权限
        RolePO selectResult = roleService.getById(id);
        BizAssert.notNull(selectResult, BizCodeEnum.PERMISSION_NOT_EXIST);
        // 参数填充
        selectResult.setName(request.getName());
        selectResult.setIsDefault(request.getIsDefault());
        selectResult.setRemark(request.getRemark());
        roleService.updateById(selectResult);
        return ApiResult.updated(selectResult);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> deleteRole(@PathVariable("id") Long id) {
        boolean remove = roleService.removeById(id);
        return ApiResult.deleted(remove);
    }

    @Operation(summary = "批量删除角色")
    @DeleteMapping("/")
    public ApiResult<Boolean> deleteRoles(@RequestBody List<Long> ids) {
        boolean remove = roleService.removeByIds(ids);
        return ApiResult.deleted(remove);
    }

    @Operation(summary = "查询角色权限")
    @GetMapping("/permission/{id}")
    public ApiResult<List<PermissionPO>> getRolePermissions(@PathVariable("id") Long id) {
        // 查询角色信息
        RolePO role = roleService.getById(id);
        BizAssert.notNull(role, BizCodeEnum.ROLE_NOT_EXIST);

        // 查询角色权限
        List<PermissionPO> permissionList = roleService.getPermission(id);
        return ApiResult.success(permissionList);
    }

    @Operation(summary = "角色绑定权限")
    @PostMapping("/permission/{id}/bind")
    public ApiResult<Integer> bindPermission(@PathVariable("id") Long id, @RequestBody List<Long> permissionIds) {
        // 判断角色是否存在
        RolePO role = roleService.getById(id);
        BizAssert.notNull(role, BizCodeEnum.ROLE_NOT_EXIST);
        // 绑定权限
        Integer result = roleService.bindPermission(id, permissionIds);
        return ApiResult.created(result);
    }

    @Operation(summary = "角色解绑权限")
    @PostMapping("/permission/{id}/unbind")
    public ApiResult<Integer> unbindPermission(@PathVariable("id") Long id, @RequestBody List<Long> permissionIds) {
        // 判断角色是否存在
        RolePO role = roleService.getById(id);
        BizAssert.notNull(role, BizCodeEnum.ROLE_NOT_EXIST);
        // 解绑权限
        Integer result = roleService.unbindPermission(id, permissionIds);
        return ApiResult.created(result);
    }
}
