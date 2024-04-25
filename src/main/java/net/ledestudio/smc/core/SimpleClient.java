package net.ledestudio.smc.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.ledestudio.smc.api.SimpleMessageChannel;
import net.ledestudio.smc.core.SimpleInitializer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class SimpleClient implements SimpleMessageChannel {

    private static final Logger LOGGER = Logger.getLogger("smc-client");

    private final String host;
    private final int port;
    private final int threads;

    private Channel serverChannel;
    private EventLoopGroup eventLoopGroup;

    public SimpleClient(String host, int port) {
        this(host, port, 4);
    }

    public SimpleClient(String host, int port, int threads) {
        this.host = host;
        this.port = port;
        this.threads = threads;
    }

    @Override
    public void start() {
        eventLoopGroup = new NioEventLoopGroup(threads, new DefaultThreadFactory("smc-client"));

        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(new InetSocketAddress(host, port));
        bootstrap.handler(new SimpleInitializer());

        try {
            serverChannel = bootstrap.connect().sync().channel();
            LOGGER.info("connect smc client to server.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
        LOGGER.info("close smc client.");
    }

    @Override
    public void sendMessage(@NotNull String message) {
        serverChannel.writeAndFlush(message.concat("\n"));
    }
}
