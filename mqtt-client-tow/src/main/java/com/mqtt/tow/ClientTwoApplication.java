package com.mqtt.tow;

import com.mqtt.tow.server.BootNettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ Author L
 * @ Date 2021/5/10 15:03
 * @ DESC
 */
@SpringBootApplication
public class ClientTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientTwoApplication.class,args);
        new BootNettyClient().start();
    }
}
