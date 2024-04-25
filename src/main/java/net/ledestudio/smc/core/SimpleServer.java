package net.ledestudio.smc.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.ledestudio.smc.api.SimpleMessageChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class SimpleServer implements SimpleMessageChannel {

    private static final Logger LOGGER = Logger.getLogger("smc-server");

    private final int port;
    private final int threads;

    private final ChannelGroup channels = new DefaultChannelGroup("smc", GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    public SimpleServer(int port) {
        this(port, 4);
    }

    public SimpleServer(int port, int threads) {
        this.port = port;
        this.threads = threads;
    }

    @Override
    public void start() {
        bossEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("smc-boss"));
        workerEventLoopGroup = new NioEventLoopGroup(threads, new DefaultThreadFactory("smc-worker"));

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossEventLoopGroup, workerEventLoopGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new SimpleInitializer());

        try {
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(port)).sync();
            Channel channel = future.channel();
            channels.add(channel);
            LOGGER.info("start smc server.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void close() {
        channels.close().awaitUninterruptibly();
        if (workerEventLoopGroup != null) {
            workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        }
        if (bossEventLoopGroup != null) {
            bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        }
        LOGGER.info("close smc server.");
    }

    @Override
    public boolean isStarted() {
        return !channels.isEmpty();
    }

    @Override
    public void sendMessage(@NotNull String message) {
        channels.writeAndFlush(message.concat("\n"));
    }

}
