package com.mqtt.client;

import ch.qos.logback.core.net.server.Client;
import com.mqtt.client.bootstrap.ClientTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ Author L
 * @ Date 2021/4/30 11:34
 * @ DESC
 */

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ClientTask clientTask;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 2; i++) {
            threadPoolExecutor.execute(clientTask);
        }

    }
}
