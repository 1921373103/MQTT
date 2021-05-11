package com.mqtt.tow.config;

import com.mqtt.common.util.ByteBufUtil;
import com.mqtt.tow.entity.MqttOpntions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @ Author L
 * @ Date 2021/5/10 15:13
 * @ DESC
 */
@Component
@Slf4j
public class MqttHander extends ChannelInboundHandlerAdapter {

    /**
     * 	客户端与服务端第一次建立连接时执行 在channelActive方法之前执行
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 	客户端与服务端第一次建立连接时执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        MqttOpntions mqtt = new MqttOpntions();
        byte[] willMessage = mqtt.getWillMessage().getBytes();
        byte[] password = mqtt.getPassword().getBytes();
        log.info("【DefaultMqttHandler：channelActive】"+ctx.channel().localAddress().toString()+"启动成功");
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT,false, MqttQoS.AT_LEAST_ONCE,false,10);
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_3_1_1.protocolName(),MqttVersion.MQTT_3_1_1.protocolLevel(),mqtt.isHasUserName(),mqtt.isHasPassword(),mqtt.isHasWillRetain(),mqtt.getWillQos(),mqtt.isHasWillFlag(),mqtt.isHasCleanSession(),mqtt.getKeepAliveTime());
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(mqtt.getClientIdentifier(),mqtt.getWillTopic(),willMessage,mqtt.getUserName(),password);
        MqttConnectMessage mqttSubscribeMessage = new MqttConnectMessage(mqttFixedHeader,mqttConnectVariableHeader,mqttConnectPayload);
        channel.writeAndFlush(mqttSubscribeMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MqttMessage mqttMessage = (MqttMessage) msg;
        log.info("info--" + mqttMessage.toString());

        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        switch (mqttFixedHeader.messageType()){
            case UNSUBACK:
//                mqttHandlerApi.unsubBack(channelHandlerContext.channel(),mqttMessage);
                break;
            case CONNACK:
//                connectBack((MqttConnAckMessage) mqttMessage);
                break;
            case PUBLISH:
//                publishMessage(ctx,(MqttPublishMessage) mqttMessage);
                break;
            case PUBACK: // qos 1回复确认
//                mqttHandlerApi.puback(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREC: //
//                mqttHandlerApi.pubrec(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBREL: //
//                mqttHandlerApi.pubrel(channelHandlerContext.channel(),mqttMessage);
                break;
            case PUBCOMP: //
//                mqttHandlerApi.pubcomp(channelHandlerContext.channel(),mqttMessage);
                break;
            case SUBACK:
//                mqttHandlerApi.suback(channelHandlerContext.channel(),(MqttSubAckMessage)mqttMessage);
                break;
            default:
                break;
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
