package cn.youyou.yycache.plugin.server;

import cn.youyou.yycache.netty.YYCacheDecoder;
import cn.youyou.yycache.netty.YYCacheHandler;
import cn.youyou.yycache.plugin.YYPlugin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YYCacheServer implements YYPlugin {

    int port = 6379;

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    Channel channel;

    @Override
    public String getName() {
        return "YYCacheServer-Plugin";
    }

    @Override
    public void init() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("yycache-boss"));
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("yycache-worker"));
        log.info(getName() + " init OK");
    }

    @Override
    public void startup() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YYCacheDecoder());
                            socketChannel.pipeline().addLast(new YYCacheHandler());
                        }
                    });

            channel = serverBootstrap.bind(port).sync().channel();
            log.info("yycache server started on port {}", port);
            log.info(getName() + " startup OK");
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
        if (channel != null) {
            channel.close();
            channel = null;
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
        log.info(getName() + " shutdown OK");
    }
}
