package com.mqtt.tow.handle;

import com.mqtt.tow.server.BootNettyClient;
import com.mqtt.tow.utils.CancelbleExecutorService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 发送心跳
 *
 * @author tianzhenjiu
 */
@Slf4j
public class PingRunnable implements Runnable {

    final Channel channel;

    /**
     * 发送ping之后是否回复pong
     */
    final AtomicBoolean hasResp;

    final BootNettyClient connetor;

    final CancelbleExecutorService executorService;

    final Integer pingTimeOut;


    public PingRunnable(Channel channel, BootNettyClient connetor,
                        Integer pingTimeOut,
                        CancelbleExecutorService executorService) {
        super();
        this.channel = channel;
        this.connetor = connetor;
        this.pingTimeOut = pingTimeOut;
        this.executorService = executorService;
        this.hasResp = new AtomicBoolean(false);
    }

    @Override
    public void run() {

        /**
         * 只要链路不可用，或者没有收到心跳回复就重新连接
         */
        if (!channel.isActive() || !hasResp.get()) {
            connetor.reconnection(channel);
            log.info("规定时间内未收到PONG报文，将会重新连接");
            return;
        }


        channel.writeAndFlush(
                new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0)))
                .addListener((ChannelFuture future) -> {

                    /**
                     * 只要心跳发出去了就设置了 没收到心跳
                     */
                    if (updatehasResp(false)) {
                        executorService.schedule(PingRunnable.this, pingTimeOut, TimeUnit.SECONDS);
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("发送心跳");
                    }

                });

    }


    /**
     * 更新hasResp
     *
     * @param updateVal
     * @return
     */
    public boolean updatehasResp(boolean updateVal) {

        if (!hasResp.compareAndSet(!updateVal, updateVal)) {
            return hasResp.get() == updateVal;
        }

        return true;
    }


}