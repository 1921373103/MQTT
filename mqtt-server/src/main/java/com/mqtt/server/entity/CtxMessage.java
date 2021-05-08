package com.mqtt.server.entity;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @ Author L
 * @ Date 2021/5/7 17:47
 * @ DESC
 */
@Data
public class CtxMessage {

    public String id;

    public String Topic;

    public ChannelHandlerContext ctx;

    public int qos;
}
