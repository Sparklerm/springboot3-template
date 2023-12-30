package ${groupId}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ${groupId}.model.po.PermissionPO;

/**
 * @author alex meng
 * @description 针对表【permission】的数据库操作Service接口
 * @createDate 2023-12-24 02:38:19
 */
public interface IPermissionService extends IService<PermissionPO> {

    /**
     * @param permission 权限信息
     * @return 创建后的权限信息
     */
    PermissionPO add(PermissionPO permission);
}
