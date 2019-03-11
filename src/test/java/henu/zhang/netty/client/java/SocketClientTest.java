package henu.zhang.netty.client.java;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author 张向兵
 * @date 2019-03-11.
 */
public class SocketClientTest {

    @Test
    public void run() throws Exception {
        new SocketClient("127.0.0.1", 8000).run();
    }
}