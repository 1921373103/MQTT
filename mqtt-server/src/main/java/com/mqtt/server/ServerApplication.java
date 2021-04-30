package com.mqtt.server;

import com.mqtt.server.server.BootNettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ Author L
 * @ Date 2021/4/30 9:05
 * @ DESC
 */
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServerApplication.class);
        app.run(args);
        new BootNettyServer().startup();
    }
}
