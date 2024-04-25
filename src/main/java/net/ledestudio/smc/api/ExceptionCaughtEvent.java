package net.ledestudio.smc.api;

import io.netty.channel.ChannelHandlerContext;

public abstract class ExceptionCaughtEvent {

    public abstract void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause);

}
