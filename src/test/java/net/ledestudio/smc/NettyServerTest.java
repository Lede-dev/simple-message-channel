package net.ledestudio.smc;

import net.ledestudio.smc.core.SimpleServer;
import org.junit.jupiter.api.Test;

public class NettyServerTest {

    @Test
    public void nettyServerTest() throws InterruptedException {
        new SimpleServer(7716).start();

        while (true) {
            Thread.sleep(1000);
        }
    }



}
