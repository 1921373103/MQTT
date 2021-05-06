package com.mqtt.server.adapter;

import com.mqtt.common.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ Author L
 * @ Date 2021/4/30 11:23
 * @ DESC
 */
@Component
@Slf4j
public class BootChannelInboundHandler extends ChannelInboundHandlerAdapter {

    /**
     * 	客户端与服务端第一次建立连接时执行 在channelActive方法之前执行
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 	客户端与服务端 断连时执行 channelInactive方法之后执行
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * 	从客户端收到新的数据时，这个方法会在收到消息时被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception, IOException {
        if (null != msg) {
            MqttMessage mqttMessage = (MqttMessage) msg;
            log.info("info--"+mqttMessage.toString());
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            Channel channel = ctx.channel();

            if(mqttFixedHeader.messageType().equals(MqttMessageType.CONNECT)){
                //	在一个网络连接上，客户端只能发送一次CONNECT报文。服务端必须将客户端发送的第二个CONNECT报文当作协议违规处理并断开客户端的连接
                //	to do 建议connect消息单独处理，用来对客户端进行认证管理等 这里直接返回一个CONNACK消息
                BootMqttMsgBack.connack(channel, mqttMessage);
            }

            switch (mqttFixedHeader.messageType()){
                case PUBLISH:		//	客户端发布消息
                    //	PUBACK报文是对QoS 1等级的PUBLISH报文的响应
                    // System.out.println("123");
                    BootMqttMsgBack.puback(channel, mqttMessage);
                    break;
                case PUBREL:		//	发布释放
                    //	PUBREL报文是对PUBREC报文的响应
                    //	to do
                    BootMqttMsgBack.pubcomp(channel, mqttMessage);
                    break;
                case SUBSCRIBE:		//	客户端订阅主题
                    //	客户端向服务端发送SUBSCRIBE报文用于创建一个或多个订阅，每个订阅注册客户端关心的一个或多个主题。
                    //	为了将应用消息转发给与那些订阅匹配的主题，服务端发送PUBLISH报文给客户端。
                    //	SUBSCRIBE报文也（为每个订阅）指定了最大的QoS等级，服务端根据这个发送应用消息给客户端
                    // 	to do
                    BootMqttMsgBack.suback(channel, mqttMessage);
                    break;
                case UNSUBSCRIBE:	//	客户端取消订阅
                    //	客户端发送UNSUBSCRIBE报文给服务端，用于取消订阅主题
                    //	to do
                    BootMqttMsgBack.unsuback(channel, mqttMessage);
                    break;
                case PINGREQ:		//	客户端发起心跳
                    //	客户端发送PINGREQ报文给服务端的
                    //	在没有任何其它控制报文从客户端发给服务的时，告知服务端客户端还活着
                    //	请求服务端发送 响应确认它还活着，使用网络以确认网络连接没有断开
                    BootMqttMsgBack.pingresp(channel, mqttMessage);
                    break;
                case DISCONNECT:	//	客户端主动断开连接
                    //	DISCONNECT报文是客户端发给服务端的最后一个控制报文， 服务端必须验证所有的保留位都被设置为0
                    //	to do
                    break;
                default:
                    break;
            }
        }
    }

    // 服务端发布消息
/*    private void publish(Channel channel,MqttPublishMessage mqttMessage) {
        MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
        MqttPublishVariableHeader mqttPublishVariableHeader = mqttMessage.variableHeader();
        ByteBuf payload = mqttMessage.payload();
        byte[] bytes = ByteBufUtil.copyByteBuf(payload); //
        if(mqttListener!=null){
            mqttListener.callBack(mqttPublishVariableHeader.topicName(),new String(bytes));
        }
        switch (mqttFixedHeader.qosLevel()){
            case AT_MOST_ONCE:
                break;
            case AT_LEAST_ONCE:
                mqttHandlerApi.pubBackMessage(channel,mqttPublishVariableHeader.messageId());
                break;
            case EXACTLY_ONCE:
                mqttProducer.pubRecMessage(channel,mqttPublishVariableHeader.messageId());
                break;
        }

    }*/

    /**
     * 	从客户端收到新的数据、读取完成时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
    }

    /**
     * 	当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            // 远程主机强迫关闭一个现有的连接异常
            ctx.close();
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    /**
     * 	客户端与服务端第一次建立连接时执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 	客户端与服务端 断连时执行
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
    }

    /**
     * 	服务端 当读超时时 会调用这个方法,发送遗嘱消息
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                Channel channel = ctx.channel();
                String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
                /*// 发送遗嘱消息
                if (this.protocolProcess.getSessionStoreService().containsKey(clientId)) {
                    SessionStore sessionStore = this.protocolProcess.getSessionStoreService().get(clientId);
                    if (sessionStore.getWillMessage() != null) {
                        this.protocolProcess.publish().processPublish(ctx.channel(), sessionStore.getWillMessage());
                    }
                }*/
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }


    public MqttPublishMessage buildPublish(String str, String topicName, Integer messageId)
    {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, str.length());
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topicName, messageId);//("MQIsdp",3,false,false,false,0,false,false,60);
        ByteBuf payload = Unpooled.wrappedBuffer(str.getBytes(CharsetUtil.UTF_8));
        MqttPublishMessage msg = new MqttPublishMessage(mqttFixedHeader, variableHeader, payload);
        return msg;
    }






}
