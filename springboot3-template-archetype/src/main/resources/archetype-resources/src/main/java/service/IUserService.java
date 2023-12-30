package ${package}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ${package}.model.dto.UserLoginResultDTO;
import ${package}.model.po.RolePO;
import ${package}.model.po.UserPO;

import java.util.List;

/**
 * @author alex meng
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-12-23 22:39:27
 */
public interface IUserService extends IService<UserPO> {
    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param nikeName 昵称
     * @return 注册结果
     */
    UserPO register(String username, String password, String nikeName);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token信息
     */
    UserLoginResultDTO login(String username, String password);

    /**
     * 退出登录
     *
     * @param username 用户名
     */
    void logout(String username);


    /**
     * 查询用户角色信息
     *
     * @param id 用户Id
     * @return 用户角色
     */
    List<RolePO> getRole(Long id);

    /**
     * 用户绑定角色
     *
     * @param id      用户Id
     * @param roleIds 角色Id
     * @return 绑定结果
     */
    Integer bindRole(Long id, List<Long> roleIds);

    /**
     * 用户解绑角色
     *
     * @param id      用户Id
     * @param roleIds 角色Id
     * @return 解绑结果
     */
    Integer unbindRole(Long id, List<Long> roleIds);
}
