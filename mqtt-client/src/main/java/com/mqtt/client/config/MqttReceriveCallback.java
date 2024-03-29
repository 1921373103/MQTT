package com.mqtt.client.config;

import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 回调消息
 * @ Author L
 * @ Date 2021/4/30 13:51
 * @ DESC
 */

public class MqttReceriveCallback implements MqttCallback {

    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    @SneakyThrows
    @Override
    public void connectionLost(Throwable cause) {
        // 通常在这里进行重连
        System.out.println("连接断开，重连！");
        new MyMqttClient().reConnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        /*System.out.println("Client 接收消息主题 : " + topic);
        System.out.println("Client 接收消息Qos : " + message.getQos());
        System.out.println("Client 接收消息内容 : " + new String(message.getPayload()));*/
        atomicInteger.getAndIncrement();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

}
