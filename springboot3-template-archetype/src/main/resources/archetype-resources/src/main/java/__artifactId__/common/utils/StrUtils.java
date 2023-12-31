package ${package}.${artifactId}.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.AntPathMatcher;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author alex meng
 * @createDate 2023 /01/01
 */
public class StrUtils {

    /**
     * The constant SPLIT_REGEX_COMMA.
     */
    public static final String SPLIT_REGEX_COMMA = ",";
    /**
     * The constant REGEX_NUMERIC.
     */
    public static final String REGEX_NUMERIC = "^[+-]?//d*$";
    /**
     * 金额小数位校验
     */
    public static final Pattern MONEY_CHECK = Pattern.compile("^[+]?(//d+(.//d{1,2})?)$");
    private static final String UNDERLINE = "_";

    private StrUtils() {
    }

    /**
     * Split filter blank list.
     *
     * @param str   the str
     * @param regex the regex
     * @return list list
     */
    public static List<String> splitFilterBlank(String str, String regex) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str) || org.apache.commons.lang3.StringUtils.isBlank(regex)) {
            return new ArrayList<>();
        }
        String[] splits = str.split(regex);
        List<String> results = new ArrayList<>();
        for (String split : splits) {
            if (org.apache.commons.lang3.StringUtils.isBlank(split)) {
                continue;
            }
            results.add(split);
        }
        return results;
    }

    /**
     * Comma split filter blank as long list list.
     *
     * @param str the str
     * @return list list
     */
    public static List<Long> commaSplitFilterBlankAsLongList(String str) {
        List<String> strings = splitFilterBlank(str, SPLIT_REGEX_COMMA);
        List<Long> longs = new ArrayList<>();
        for (String string : strings) {
            if (!StrUtils.isNumeric(string)) {
                throw new IllegalArgumentException("请传入正确的数字");
            }
            Long num = Long.valueOf(string);
            longs.add(num);
        }
        return longs;
    }

    /**
     * Is numeric boolean.
     *
     * @param string the string
     * @return the boolean
     */
    public static boolean isNumeric(String string) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(string) && string.matches(REGEX_NUMERIC);
    }

    /**
     * Build url string.
     *
     * @param url    the url
     * @param params the params
     * @return the string
     */
    public static String buildUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        for (Map.Entry<String, String> param : params.entrySet()) {
            builder.append("&");
            builder.append(param.getKey());
            builder.append("=");
            builder.append(param.getValue());
        }
        return builder.toString();
    }

    /**
     * Gets string.
     *
     * @param obj the obj
     * @param def the def
     * @return the string
     */
    public static String getString(Object obj, String def) {
        return Objects.isNull(obj) ? def : String.valueOf(obj);
    }

    /**
     * Comma split filter blank as integer list list.
     *
     * @param str the str
     * @return the list
     */
    public static List<Integer> commaSplitFilterBlankAsIntegerList(String str) {
        List<String> strings = splitFilterBlank(str, SPLIT_REGEX_COMMA);
        List<Integer> its = new ArrayList<>();
        for (String string : strings) {
            if (!StrUtils.isNumeric(string)) {
                throw new IllegalArgumentException("请传入正确的数字");
            }
            Integer num = Integer.valueOf(string);
            its.add(num);
        }
        return its;
    }

    /**
     * 根据文件路径截取文件名
     *
     * @param path the path
     * @return the file name by path
     */
    public static String getFileNameByPath(String path) {
        if (org.apache.commons.lang3.StringUtils.isBlank(path)) {
            return null;
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 获取后缀
     *
     * @param path the path
     * @return the suffix
     */
    public static String getSuffix(String path) {
        if (org.apache.commons.lang3.StringUtils.isBlank(path)) {
            return null;
        }
        return path.substring(path.lastIndexOf("."));
    }

    /**
     * 拼接多个字符
     *
     * @param targets the targets
     */
    public static String append(String... targets) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String target : targets) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(target)) {
                stringBuilder.append(target);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Gets integer.
     *
     * @param value the value
     * @return the integer
     */
    public static Integer getInteger(String value) {
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    /**
     * Gets double.
     *
     * @param value the value
     * @return the double
     */
    public static Double getDouble(String value) {
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            return null;
        }
        return Double.valueOf(value);
    }

    /**
     * Empty string string.
     *
     * @param s the s
     * @return the string
     */
    public static String emptyString(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return "未知";
        }
        return s;
    }

    /**
     * 金额小数位校验
     * 允许0位，1位，2位小数
     *
     * @param money the money
     * @return 是否满足校验规则 boolean
     */
    public static boolean moneyCheck(String money) {
        if (org.apache.commons.lang3.StringUtils.isBlank(money)) {
            return Boolean.FALSE;
        }
        if (StrUtils.MONEY_CHECK.matcher(money).matches()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 填充字符串，填充再字符串前
     *
     * @param str        原始字符串
     * @param filledChar 填充字符
     * @param length     目标填充长度
     * @return the 填充后的字符串
     */
    public static String fillBefore(String str, char filledChar, int length) {
        return StrUtil.fill(str, filledChar, length, true);
    }

    /**
     * 填充字符串，填充再字符串后
     *
     * @param str        原始字符串
     * @param filledChar 填充字符
     * @param length     目标填充长度
     * @return string 填充后的字符串
     */
    public static String fillAfter(String str, char filledChar, int length) {
        return StrUtil.fill(str, filledChar, length, false);
    }


    /**
     * 获取字符串byte数组
     *
     * @param str 原始字符串
     * @return the byte [ ]
     */
    public static byte[] bytes(String str) {
        return CharSequenceUtil.bytes(str, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 字符串最小长度校验
     *
     * @param str    入参字符串
     * @param minLen 字符串最小长度
     * @return boolean boolean
     */
    public static boolean minLenCheck(String str, int minLen) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str) || str.length() < minLen) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 字符串最大长度校验
     *
     * @param str    入参字符串
     * @param maxLen 字符串最大长度
     * @return the boolean
     */
    public static boolean maxLenCheck(String str, int maxLen) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str) || str.length() > maxLen) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return boolean
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str     指定字符串
     * @param strList 需要检查的字符串数组
     * @return 是否匹配 boolean
     */
    public static boolean matches(String str, List<String> strList) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str) || CollUtil.isEmpty(strList)) {
            return false;
        }
        for (String pattern : strList) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线
     *
     * @param param     字符串
     * @param upperCase 是否全大写
     * @return 结果
     */
    public static String camelToUnderline(String param, boolean... upperCase) {
        if (param == null || param.trim().isEmpty()) {
            return "";
        }

        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(StrUtils.UNDERLINE);
            }
            if (upperCase[0]) {
                //统一都转大写
                sb.append(Character.toUpperCase(c));
            } else {
                //统一都转小写
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param input 输入字符串
     * @return 下划线字符串
     */
    public static String convertCamelToUnderline(String input) {
        if (org.apache.commons.lang3.StringUtils.isBlank(input)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isUpperCase(currentChar)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * 占位符替换
     * <p>
     * 例子： String template = "name:{0},age:{1}";
     * params = ["alex", 18];
     * result : name:alex,age:18
     *
     * @param template 模板字符串
     * @param params   填充参数
     * @return 替换后的字符串 string
     */
    public static String format(String template, Object... params) {
        return MessageFormat.format(template, params);
    }

}
