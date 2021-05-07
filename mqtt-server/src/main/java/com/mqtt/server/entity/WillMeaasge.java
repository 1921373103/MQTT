package com.mqtt.server.entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @ Author L
 * @ Date 2021/5/7 17:47
 * @ DESC
 */
@Data
public class WillMeaasge {

    public String Topic;

    public ChannelHandlerContext ctx;

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }
}
