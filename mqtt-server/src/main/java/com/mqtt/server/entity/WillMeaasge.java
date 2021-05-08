package com.mqtt.server.entity;

import lombok.Data;

/**
 * 遗嘱消息
 *
 * @author L
 * @create 2021-05-08
 */
@Data
public class WillMeaasge {

    private String willTopic;

    private String willMessage;

    private  boolean isRetain;

    private int qos;

}