package com.mqtt.tow;

import cn.hutool.core.util.RandomUtil;
import com.mqtt.tow.bootstrap.ClientTask;
import com.mqtt.tow.server.BootNettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ Author L
 * @ Date 2021/5/10 15:03
 * @ DESC
 */
@SpringBootApplication
public class ClientTwoApplication implements CommandLineRunner {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ClientTask clientTask;

    @Autowired
    private BootNettyClient bootNettyClient;

    public static void main(String[] args) {
        SpringApplication.run(ClientTwoApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 100; i++) {
            // Thread.sleep(400);
//            threadPoolExecutor.execute(clientTask);
//            clientTask.run();
            bootNettyClient.start(RandomUtil.randomString(32));
        }
    }
}
