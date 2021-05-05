package com.mqtt.client;

import com.mqtt.client.config.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.SpringApplication.run;

/**
 * @ Author L
 * @ Date 2021/4/30 11:34
 * @ DESC
 */

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) throws MqttException {
        ConfigurableApplicationContext run = run(ClientApplication.class,args);
        new MyMqttClient().init("123");
    }
}
