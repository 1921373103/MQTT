package com.mqtt.client.bootstrap;

import cn.hutool.core.util.RandomUtil;
import com.mqtt.client.config.MyMqttClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ Author L
 * @ Date 2021/5/8 15:15
 * @ DESC
 */
@Component
public class ClientTask implements Runnable{

    @Autowired
    public MyMqttClient mqttClient;

    @SneakyThrows
    @Override
    public void run() {
        mqttClient.init(RandomUtil.randomString(32));
    }


}
