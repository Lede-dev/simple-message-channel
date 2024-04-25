package net.ledestudio.smc.service;

import io.netty.channel.ChannelHandlerContext;
import net.ledestudio.smc.api.EventManager;
import net.ledestudio.smc.api.ExceptionCaughtEvent;
import net.ledestudio.smc.api.MessageEvent;
import net.ledestudio.smc.api.SimpleMessageChannel;
import net.ledestudio.smc.core.SimpleClient;
import net.ledestudio.smc.core.SimpleServer;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class TestApplication {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Server [0] | Client [1] : ");
        Scanner scanner = new Scanner(System.in);
        int type = scanner.nextInt();

        EventManager.registerMessageEvents(new MessageEvent() {
            @Override
            public void onMessageReceived(@NotNull ChannelHandlerContext ctx, @NotNull String msg) {
                System.out.println(msg);
            }
        });

        EventManager.registerExceptionCaughtEvents(new ExceptionCaughtEvent() {
            @Override
            public void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                cause.printStackTrace();
            }
        });

        if (type == 0) {
            SimpleMessageChannel channel = new SimpleServer(7716);
            channel.start();
            while (true) {
                System.out.println("Waiting Client Input");
                Thread.sleep(1000 * 5);
            }
        } else {
            SimpleMessageChannel channel = new SimpleClient("localhost", 7716);
            channel.start();
            while(true) {
                String message = scanner.nextLine();
                channel.sendMessage(message);
            }
        }
    }

}
