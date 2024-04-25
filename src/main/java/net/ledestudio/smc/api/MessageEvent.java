package net.ledestudio.smc.api;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

public abstract class MessageEvent {

    public abstract void onMessageReceived(@NotNull ChannelHandlerContext ctx, @NotNull String msg);

}
