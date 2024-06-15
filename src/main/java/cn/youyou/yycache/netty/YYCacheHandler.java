package cn.youyou.yycache.netty;

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
    private static final String STR_STRING = "+";

    private static final String OK = "OK";

    private static final String SERVER_INFO = "YYCache Server[v1.0.0], created by XianReallyHot-ZZH." + CRLF
            + "Mock Redis Server, at 2024-06-15 in HangZhou." + CRLF;


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
            simpleString(ctx, ret);
        } else if ("INFO".equals(cmd)) {
            bulkString(ctx, SERVER_INFO);
        } else {
            simpleString(ctx, OK);
        }


    }

    /**
     * 将符合协议的字符串封装成ByteBuf，并写入到Channel中
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
    private void simpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, STR_STRING + content + CRLF);
    }

    /**
     * 复杂字符串的协议响应封装
     * @param ctx
     * @param content
     */
    private void bulkString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, "$" + content.getBytes().length + CRLF + content + CRLF);
    }


}
