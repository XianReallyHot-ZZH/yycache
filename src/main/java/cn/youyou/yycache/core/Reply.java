package cn.youyou.yycache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command指令执行返回的对象
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply<T> {

    // 回车换行符
    private static final String CRLF = "\r\n";

    // redis协议中响应简单字符串的标识符
    private static final String STR_PREFIX = "+";

    // redis协议中响应复杂字符串的标识符
    private static final String BULK_PREFIX = "$";

    // redis协议中响应数字的标识符
    private static final String NUMBER_PREFIX = ":";

    // redis协议中响应数组的标识符
    private static final String ARRAY_PREFIX = "*";

    // redis协议中响应异常的标识符
    private static final String ERROR_PREFIX = "-";

    // redis协议中响应服务器信息
    private static final String SERVER_INFO = "YYCache Server[v1.0.0], created by XianReallyHot-ZZH." + CRLF
            + "Mock Redis Server, at 2024-06-15 in HangZhou." + CRLF;


    T value;

    ReplyType type;

    /**
     * simpleString类型的构造方法
     * @param value
     * @return
     */
    public static Reply<String> simpleString(String value) {
        return new Reply<>(value, ReplyType.SIMPLE_STRING);
    }

    /**
     * 对应RESP协议中error类型的构造方法
     * @param value
     * @return
     */
    public static Reply<String> error(String value) {
        return new Reply<>(value, ReplyType.ERROR);
    }

    /**
     * 对应RESP协议中integer类型的构造方法
     * @param value
     * @return
     */
    public static Reply<Integer> integer(Integer value) {
        return new Reply<>(value, ReplyType.INT);
    }

    /**
     * 对应RESP协议中bulkString类型的构造方法
     * @param value
     * @return
     */
    public static Reply<String> bulkString(String value) {
        return new Reply<>(value, ReplyType.BULK_STRING);
    }

    /**
     * 对应RESP协议中array类型的构造方法
     * @param value
     * @return
     */
    public static Reply<String[]> array(String[] value) {
        return new Reply<>(value, ReplyType.ARRAY);
    }

    /**
     * 按协议编码
     * @return
     */
    public String encode() {
        return switch (type) {
            case SIMPLE_STRING -> simpleStringEncode((String) value);
            case ERROR -> errorEncode((String) value);
            case INT -> integerEncode((Integer) value);
            case BULK_STRING -> bulkStringEncode((String) value);
            case ARRAY -> arrayEncode((String[]) value);
            default -> errorEncode("this command is not supported");
        };
    }

    /**
     * 对应RESP协议中simpleString类型的编码
     * @param content
     * @return
     */
    private String simpleStringEncode(String content) {
        String ret;
        if (content == null) {
            ret = "-1" + CRLF;
        } else if (content.isEmpty()) {
            ret = "0" + CRLF;
        } else {
            ret = STR_PREFIX + content + CRLF;
        }
        return ret;
    }

    /**
     * 对应RESP协议中bulkString类型的编码
     * @param content
     * @return
     */
    private String bulkStringEncode(String content) {
        String ret;
        if (content == null) {
            ret = "$-1" + CRLF;
        } else if (content.isEmpty()) {
            ret = "$0" + CRLF;
        } else {
            ret = BULK_PREFIX + content.getBytes().length + CRLF + content + CRLF;
        }
        return ret;
    }

    /**
     * 对应RESP协议中integer类型的编码
     * @param i
     * @return
     */
    private String integerEncode(int i) {
        return NUMBER_PREFIX + i + CRLF;
    }

    /**
     * 对应RESP协议中array类型的编码
     * @param array
     * @return
     */
    private String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append(ARRAY_PREFIX + "-1" + CRLF);
        } else if (array.length == 0) {
            sb.append(ARRAY_PREFIX + "0" + CRLF);
        } else {
            sb.append(ARRAY_PREFIX + array.length + CRLF);
            for (Object obj : array) {
                if (obj == null) {
                    sb.append("$-1" + CRLF);
                } else {
                    if (obj instanceof String) {
                        sb.append(bulkStringEncode((String) obj));
                    } else if (obj instanceof Integer) {
                        sb.append(integerEncode((Integer) obj));
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 对应RESP协议中error类型的编码
     * @param msg
     * @return
     */
    private String errorEncode(String msg) {
        return ERROR_PREFIX + msg + CRLF;
    }
}
