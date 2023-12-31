package ${package}.${artifactId}.common.log;

/**
 * 业务日志操作
 *
 * @author alex meng
 * @createDate 2023-12-24 12:52
 */
public class BizLogHolder {

    private static final ThreadLocal<BizLog> THREAD_LOCAL = new ThreadLocal<>();

    private BizLogHolder() {
    }

    public static BizLog getContext() {
        return THREAD_LOCAL.get();
    }

    public static void setContext(BizLog t) {
        THREAD_LOCAL.set(t);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
