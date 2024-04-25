package net.ledestudio.smc.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.ledestudio.smc.api.EventManager;

public class SimpleHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        EventManager.getMessageEvents().forEach(event -> event.onMessageReceived(ctx, msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        EventManager.getExceptionCaughtEvents().forEach(event -> event.onExceptionCaught(ctx, cause));
    }

}
