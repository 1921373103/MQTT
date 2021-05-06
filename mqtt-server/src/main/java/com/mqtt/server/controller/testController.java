package com.mqtt.server.controller;

import com.mqtt.server.adapter.BootChannelInboundHandler;
import com.mqtt.server.adapter.BootMqttMsgBack;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author L
 * @ Date 2021/5/6 16:03
 * @ DESC
 */
@RestController
public class testController {

    @Autowired
    public BootChannelInboundHandler bootMqttMsgBack;

    @GetMapping("/test")
    public void test() {
        MqttPublishMessage success = bootMqttMsgBack.buildPublish("连接成功！", "success", 1);
        System.out.println("success = " + success);
    }




}
