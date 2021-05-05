package com.mqtt.client.controller;

import com.mqtt.client.config.MyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author L
 * @ Date 2021/4/30 14:11
 * @ DESC
 */

@RestController
public class TestController {

    @Autowired
    public MyMqttClient mqttClient;

    /**
     * 发布消息
     *
     * @param pubTopic 不知道是啥
     * @param message 消息
     * @param qos 请求服务器
     */
    @GetMapping("/test/{pubTopic}/{message}/{qos}")
    public void push(@PathVariable("pubTopic")String pubTopic, @PathVariable("message")String message, @PathVariable("qos")int qos) throws MqttException {
        mqttClient.publishMessage(pubTopic,message,qos);
    }

    /**
     * 订阅主题
     *
     * @param topic 主题名称
     */
    @GetMapping("/ding/{topic}")
    public void sub(@PathVariable("topic") String topic) throws MqttException {
        mqttClient.subTopic(topic);
    }

    /**
     * 订阅主题，可携带Qos
     *
     * @param topic 所要订阅的主题
     * @param qos   消息质量：0、1、2
     */
    @GetMapping("/ding/{topic}/{qos}")
    public void sub(@PathVariable("topic") String topic,@PathVariable("qos") int qos) throws MqttException {
        mqttClient.subTopic(topic, qos);
    }

    /**
     * 清空主题
     *
     * @param topic 主题
     */
    @GetMapping("/cleanTopic/{topic}")
    public void cleanTopic(@PathVariable("topic") String topic) throws MqttException {
        mqttClient.cleanTopic(topic);
    }

    /**
     * 重新连接
     *
     * @throws MqttException
     */
    @GetMapping("/reConnect")
    public void reConnect() throws MqttException {
        mqttClient.reConnect();
    }


}
