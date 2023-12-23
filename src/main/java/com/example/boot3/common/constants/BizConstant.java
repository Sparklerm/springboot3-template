package com.example.boot3.common.constants;

/**
 * 业务常量
 *
 * @author Alex Meng
 * @createDate 2023/2/11-21:50
 */
public class BizConstant {

    /**
     * 默认Token头
     */
    public static final String DEFAULT_TOKEN_HEADER = "Authorization";
    /**
     * 默认Token过期时间，七天
     */
    public static final int DEFAULT_TOKEN_EXPIRE = 604800;
    /**
     * 默认刷新Token过期时间，30天
     */
    public static final int DEFAULT_TOKEN_REFRESH_EXPIRE = 2592000;
    /**
     * 默认加密密钥
     */
    public static final String DEFAULT_SECRET = "is_app_default_secret";
    /**
     * 默认创建数据操作成功提示信息
     */
    public static final String DEFAULT_CREATE_OPTION_SUCCESS_MESSAGE = "数据创建成功";
    /**
     * 默认更新数据操作成功提示信息
     */
    public static final String DEFAULT_UPDATE_OPTION_SUCCESS_MESSAGE = "数据更新成功";
    /**
     * 默认删除数据操作成功提示信息
     */
    public static final String DEFAULT_DELETE_OPTION_SUCCESS_MESSAGE = "数据删除成功";

    private BizConstant() {
    }

}
