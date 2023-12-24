package com.example.boot3.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean 属性拷贝工具类
 * <p>
 * 基于Json序列化方式实现Copy
 *
 * @author Alex Meng
 * @createDate 2022-12-11
 */
@Slf4j
public class BeanCopierUtils {

    private BeanCopierUtils() {
    }

    /**
     * 简单属性拷贝，通过反序列化方式实现Copy，适用于属性名一致的对象
     *
     * @param <S>         数据源类型
     * @param <T>         目标数据类型
     * @param source      数据源
     * @param targetClass 目标数据类型Class
     * @return 转换后的目标数据 t
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        String sourceJson = JsonUtils.toJsonStr(source);
        return JsonUtils.toObj(sourceJson, targetClass);
    }

    /**
     * 简单属性拷贝 -- 指定属性复制
     *
     * @param <S>         数据源类型
     * @param <T>         目标数据类型
     * @param source      数据源
     * @param targetClass 目标数据类型Class
     * @param callBack    定义回调函数，指定Copy规则
     * @return 转换后的目标数据 t
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass, BeanCopyUtilCallBack<S, T> callBack) {
        T t = copyProperties(source, targetClass);
        if (callBack != null) {
            callBack.callBack(source, t);
        }
        return t;
    }

    /**
     * 集合数据的拷贝
     *
     * @param <S>         数据源类型
     * @param <T>         目标数据类型
     * @param sources     数据源类
     * @param targetClass 目标类Class
     * @return 转换后的集合 list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Class<T> targetClass) {
        return copyListProperties(sources, targetClass, null);
    }


    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param <S>         数据源类型
     * @param <T>         目标数据类型
     * @param sources     数据源类
     * @param targetClass 目标类Class
     * @param callBack    回调函数，指定Copy规则
     * @return 转换后的集合 list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Class<T> targetClass, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = copyProperties(source, targetClass);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    /**
     * 判断字段是否在类中
     *
     * @param fieldName 字段名
     * @param clazz     类
     * @return boolean
     */
    public static <T> boolean fieldInClass(String fieldName, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The interface Bean copy util call back.
     *
     * @param <S> 数据源类型
     * @param <T> 目标数据类型
     */
    @FunctionalInterface
    public interface BeanCopyUtilCallBack<S, T> {

        /**
         * 定义默认回调方法
         *
         * @param s the s
         * @param t the t
         */
        void callBack(S s, T t);
    }

}
