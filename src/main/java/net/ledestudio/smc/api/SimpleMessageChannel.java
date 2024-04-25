package net.ledestudio.smc.api;

import org.jetbrains.annotations.NotNull;

public interface SimpleMessageChannel {

    void start();

    void close();

    boolean isStarted();

    void sendMessage(@NotNull String message);

}
