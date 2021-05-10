package com.mqtt.tow.server;

import com.mqtt.tow.config.MqttHander;
import com.mqtt.tow.controller.BootstrapClient;
import com.mqtt.tow.entity.ConnectOptions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * @ Author L
 * @ Date 2021/5/10 15:40
 * @ DESC
 */

public abstract class AbstractBootstrapClient implements BootstrapClient {

    public void initHandler(ChannelPipeline channelPipeline, ConnectOptions clientBean, MqttHander mqttHander) {
        channelPipeline.addLast("decoder", new MqttDecoder());
        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
        channelPipeline.addLast(new IdleStateHandler(clientBean.getHeart(),0,0));
        channelPipeline.addLast(mqttHander);
    }


}
