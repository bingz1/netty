package henu.zhang.netty;

import henu.zhang.netty.server.websocket.WebSocketTimeServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 张向兵
 */
@SpringBootApplication
@EnableAsync
public class NettyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
        WebSocketTimeServer webSocketTimeServer = context.getBean(WebSocketTimeServer.class);
        webSocketTimeServer.bind(8000);
    }
}
