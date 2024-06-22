package cn.youyou.yycache.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 完成对网络传输字节的解码
 * byte -> String
 */
@Slf4j
public class YYCacheDecoder extends ByteToMessageDecoder {

    AtomicLong counter = new AtomicLong();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        log.info("decodeCount: {}", counter.incrementAndGet());
        if (in.readableBytes() <= 0) {
            return;
        }

        int count = in.readableBytes();
        int index = in.readerIndex();
        log.info("count: {}, index: {}", count, index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);
        String message = new String(bytes);
        log.info("receive message: {}", message);

        out.add(message);
    }
}
