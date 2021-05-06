package com.mqtt.client;

import com.mqtt.client.config.MyMqttClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ Author L
 * @ Date 2021/4/30 11:34
 * @ DESC
 */

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        new MyMqttClient().init("123");
    }
}
