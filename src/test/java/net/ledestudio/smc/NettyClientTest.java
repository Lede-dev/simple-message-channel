package net.ledestudio.smc;

import net.ledestudio.smc.core.SimpleClient;
import org.junit.jupiter.api.Test;

public class NettyClientTest {

    @Test
    public void nettyClientTest() throws InterruptedException {
        SimpleClient client = new SimpleClient("localhost", 7716);
        client.start();
    }

}
