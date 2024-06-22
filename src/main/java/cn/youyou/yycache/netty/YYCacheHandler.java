package cn.youyou.yycache.netty;

import cn.youyou.yycache.core.Command;
import cn.youyou.yycache.core.Commands;
import cn.youyou.yycache.core.Reply;
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

    private static final YYCache cache = new YYCache();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        String[] args = message.split(CRLF);
        log.info("YYCacheHandler receive message => {}", String.join(",", args));

        // 根据RESP协议，解析出来自客户端的指令(发送过来的是协议中定义的数组形式的信息，具体指令在3号位)
        String cmd = args[2].toUpperCase();

        // 根据指令获取对应的Command实现类
        Command command = Commands.get(cmd);
        if (command == null) {
            writeContext(ctx, Reply.error("ERR unsupported command '" + cmd + "'"));
        } else {
            // TODO：先粗略处理一手异常返回，这一块后续可以继续优化，针对具体的异常返回更具有针对性、更详细的错误提示
            try {
                writeContext(ctx, command.execute(cache, args));
            } catch (Exception e) {
                log.error("Command指令执行异常, exception with msg: {}", e.getMessage());
                Reply<?> reply = Reply.error("EXP exception with msg: '" + e.getMessage() + "'");
                writeContext(ctx, reply);
            }
        }
    }

    /**
     * 将指令执行结果按照协议编码格式进行编码后写入到ChannelHandlerContext中
     *
     * @param ctx
     * @param reply
     */
    private void writeContext(ChannelHandlerContext ctx, Reply<?> reply) {
        String content = reply.encode();
        log.info("wrap byte buffer and reply: {}", content);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(content.getBytes());
        ctx.writeAndFlush(buffer);
    }

}
