package com.mqtt.test;

import cn.hutool.core.util.RandomUtil;
import com.mqtt.client.bootstrap.ClientTask;
import com.mqtt.client.config.MyMqttClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ Author L
 * @ Date 2021/5/10 13:35
 * @ DESC
 */

public class client {

    @Autowired
    public static ClientTask clientTask;

    @Autowired
    public static MyMqttClient mqttClient;

    public static void main(String[] args) {


    }



}

