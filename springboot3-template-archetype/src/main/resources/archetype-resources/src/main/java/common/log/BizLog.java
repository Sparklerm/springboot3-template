package ${groupId}.common.log;

import lombok.Builder;
import lombok.Data;

/**
 * 业务日志信息
 *
 * @author alex meng
 * @createDate 2023-12-24 12:53
 */
@Data
@Builder
public class BizLog {

    public static final String USER_ID_LOG = "user-id";
    public static final String REQUEST_ID_LOG = "request-id";
    public static final String CLIENT_IP_LOG = "client-ip";

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 请求Id
     */
    private String requestId;

    /**
     * 请求客户端IP
     */
    private String clientIp;
}
