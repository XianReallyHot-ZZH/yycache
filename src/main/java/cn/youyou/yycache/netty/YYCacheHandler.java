package cn.youyou.yycache.netty;

import cn.youyou.yycache.core.YYCache;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 完成对客户端发送过来的指令的解析与响应
 */
@Slf4j
public class YYCacheHandler extends SimpleChannelInboundHandler<String> {

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

    private static final String ERROR_PREFIX = "-";

    private static final String OK = "OK";

    private static final String SERVER_INFO = "YYCache Server[v1.0.0], created by XianReallyHot-ZZH." + CRLF
            + "Mock Redis Server, at 2024-06-15 in HangZhou." + CRLF;

    private static final YYCache cache = new YYCache();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        String[] args = message.split(CRLF);
        log.info("YYCacheHandler receive message => {}", String.join(",", args));

        // 根据RESP协议，解析出来自客户端的指令(发送过来的是协议中定义的数组形式的信息，具体指令在3号位)
        String cmd = args[2].toUpperCase();

        // 先简单写一版COMMAND指令的解析处理
        if ("COMMAND".equals(cmd)) {
            // 返回一个符合协议的数组形式的信息
            String content = "*2" + CRLF
                    + "$7" + CRLF
                    + "COMMAND" + CRLF
                    + "$4" + CRLF
                    + "PING" + CRLF;
            writeByteBuf(ctx, content);
        } else if ("PING".equals(cmd)) {
            String ret = "PONG";
            if (args.length >= 5) {
                ret = args[4];
            }
            writeSimpleString(ctx, ret);
        } else if ("INFO".equals(cmd)) {
            writeBulkString(ctx, SERVER_INFO);
        } else if ("SET".equals(cmd)) {
            cache.set(args[4], args[6]);
            writeSimpleString(ctx, OK);
        } else if ("GET".equals(cmd)) {
            String value = cache.get(args[4]);
            writeBulkString(ctx, value);
        } else if ("STRLEN".equals(cmd)) {
            String value = cache.get(args[4]);
            writeInteger(ctx, value == null ? 0 : value.length());
        } else if ("DEL".equals(cmd)) {
            // 该指令支持批量，所以需要计算出key的个数
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[i * 2 + 4];
            }
            int del = cache.del(keys);
            writeInteger(ctx, del);
        } else if ("EXISTS".equals(cmd)) {
            // 该指令支持批量，所以需要计算出key的个数
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[i * 2 + 4];
            }
            writeInteger(ctx, cache.exists(keys));
        } else if ("MGET".equals(cmd)) {
            // 该指令支持批量，所以需要计算出key的个数
            int len = (args.length - 3) / 2;
            String[] keys = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[i * 2 + 4];
            }
            writeArray(ctx, cache.mget(keys));
        } else if ("MSET".equals(cmd)) {
            // 该指令支持批量，所以需要计算出key和value的个数
            int len = (args.length - 3) / 4;
            String[] keys = new String[len];
            String[] values = new String[len];
            for (int i = 0; i < len; i++) {
                keys[i] = args[i * 4 + 4];
                values[i] = args[i * 4 + 6];
            }
            cache.mset(keys, values);
            writeSimpleString(ctx, OK);
        } else if ("INCR".equals(cmd)) {
            String key = args[4];
            try {
                writeInteger(ctx, cache.incr(key));
            } catch (Exception e) {
                writeError(ctx, "NFE key: " + key + " value: " + cache.get(key) + " is not an integer.");
            }
        } else if ("DECR".equals(cmd)) {
            String key = args[4];
            try {
                writeInteger(ctx, cache.decr(key));
            } catch (Exception e) {
                writeError(ctx, "NFE key: " + key + " value: " + cache.get(key) + " is not an integer.");
            }
        } else {
            writeSimpleString(ctx, OK);
        }


    }

    /**
     * 错误响应封装
     * @param ctx
     * @param msg
     */
    private void writeError(ChannelHandlerContext ctx, String msg) {
        writeByteBuf(ctx, errorEncode(msg));
    }

    private String errorEncode(String msg) {
        return ERROR_PREFIX + msg + CRLF;
    }

    /**
     * 数组的协议响应封装
     * @param ctx
     * @param array
     */
    private void writeArray(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

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
     * 整数的协议响应封装
     * @param ctx
     * @param i
     */
    private void writeInteger(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx, integerEncode(i));
    }

    private String integerEncode(int i) {
        return NUMBER_PREFIX + i + CRLF;
    }

    /**
     * 将符合协议的字符串封装成ByteBuf，并写入到Channel中,是跟netty实现的网络相关的方法
     * @param ctx
     * @param content
     */
    private void writeByteBuf(ChannelHandlerContext ctx, String content) {
        log.info("wrap byte buffer and reply: {}", content);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(content.getBytes());
        ctx.writeAndFlush(buffer);
    }

    /**
     * 简单字符串的协议响应封装
     * @param ctx
     * @param content
     */
    private void writeSimpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, simpleStringEncode(content));
    }

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
     * 复杂字符串的协议响应封装
     * @param ctx
     * @param content
     */
    private void writeBulkString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, bulkStringEncode(content));
    }

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


}
