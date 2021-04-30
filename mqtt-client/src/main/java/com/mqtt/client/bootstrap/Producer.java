package com.mqtt.client.bootstrap;

import io.netty.channel.Channel;

import java.util.List;

/**
 * @ Author L
 * @ Date 2021/4/30 16:11
 * @ DESC
 */

public interface Producer {

    Channel getChannel();

    void  close();

    void pub(String topic,String message,boolean retained,int qos);

    void pub(String topic,String message);

    void pub(String topic,String message,int qos);

    void pub(String topic,String message,boolean retained);

    void unsub(List<String> topics);

    void unsub();

    void disConnect();
}
