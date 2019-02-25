package henu.zhang.netty;

import henu.zhang.netty.server.TimeServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterviewApplication {

    public static void main(String[] args) {
        new TimeServer().bind(8000);
    }
}
