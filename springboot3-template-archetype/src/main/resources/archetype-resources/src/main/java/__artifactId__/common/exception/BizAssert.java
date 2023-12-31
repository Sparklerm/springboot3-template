package ${package}.${artifactId}.common.exception;


import ${package}.${artifactId}.common.enums.BizCodeEnum;
import ${package}.${artifactId}.common.enums.BizCodeEnumFormat;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author alex meng
 * @createDate 2020-2-27
 */
public class BizAssert {

    private BizAssert() {
    }

    /**
     * error.
     *
     * @param message the message
     */
    public static void error(String message) {
        throw new BizException(message);
    }

    /**
     * error.
     *
     * @param errorCode the error code
     */
    public static void error(BizCodeEnumFormat errorCode) {
        throw new BizException(errorCode);
    }

    /**
     * error.
     */
    public static void error(BizCodeEnumFormat errorCode, String message) {
        throw new BizException(errorCode, message);
    }

    /**
     * error.
     */
    public static void error() {
        throw new BizException(BizCodeEnum.BIZ_ERROR);
    }

    /**
     * error.
     */
    public static void error(boolean flag, String errorMessage) {
        if (flag) {
            throw new BizException(errorMessage);
        }
    }

    public static void error(boolean flag, BizCodeEnumFormat errorCode) {
        if (flag) {
            throw new BizException(errorCode);
        }
    }

    public static void isNull(@Nullable Object object, BizCodeEnumFormat status) {
        if (ObjectUtils.isNotEmpty(object)) {
            throw new BizException(status);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (ObjectUtils.isNotEmpty(object)) {
            throw new BizException(message);
        }
    }

    public static void notNull(@Nullable Object object, BizCodeEnumFormat status) {
        if (ObjectUtils.isEmpty(object)) {
            throw new BizException(status);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (ObjectUtils.isEmpty(object)) {
            throw new BizException(message);
        }
    }

    public static void notBlank(String object, BizCodeEnumFormat status) {
        if (StringUtils.isBlank(object)) {
            throw new BizException(status);
        }
    }

    public static void notBlank(String object, String message) {
        if (StringUtils.isBlank(object)) {
            throw new BizException(message);
        }
    }

    public static void isTrue(boolean flag, String message) {
        if (!flag) {
            throw new BizException(message);
        }
    }

    public static void isTrue(boolean flag, BizCodeEnumFormat status) {
        if (!flag) {
            throw new BizException(status);
        }
    }

    public static void isFalse(boolean flag, String message) {
        if (flag) {
            throw new BizException(message);
        }
    }

    public static void isFalse(boolean flag, BizCodeEnumFormat status) {
        if (flag) {
            throw new BizException(status);
        }
    }
}
