package cn.youyou.yycache.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 完成对客户端发送过来的指令的解析与响应
 */
public class YYCacheHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
