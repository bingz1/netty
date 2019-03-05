package henu.zhang.netty;

import henu.zhang.netty.server.TimeServer;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 张向兵
 */
@SpringBootApplication
public class NettyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
        TimeServer tcpServer = context.getBean(TimeServer.class);
        tcpServer.bind(8000);
    }
}
