package com.mqtt.tow.server;

import com.mqtt.tow.config.MqttHander;
import com.mqtt.tow.entity.MqttOpntions;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ Author L
 * @ Date 2021/5/10 14:53
 * @ DESC
 */
@Slf4j
@Component
public class BootNettyClient {


    public void start(String clientId) {
        MqttOpntions mqttOpntions = new MqttOpntions();
        mqttOpntions.setClientIdentifier(clientId);
        Bootstrap bootstrap= new Bootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10485760)
                .option(ChannelOption.SO_RCVBUF, 10485760)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                        channelPipeline.addLast(new IdleStateHandler(60,0,0));
                        channelPipeline.addLast(new MqttHander());
                    }
                });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 1883).sync();
            ChannelFuture sync = channelFuture.channel().closeFuture().sync();
            Channel channel = sync.channel();


            /*if (channel == null) {
                reconnet();
            }*/
        } catch (InterruptedException e) {
            log.info("连接失败！");
        } finally {
            bossGroup.shutdownGracefully();
            log.info("执行完毕！");
        }
    }

//    public void reconnet() {
//        if (channel == null) {
//            try {
//                ChannelFuture channelFuture = bootstrap.connect("localhost", 1883).sync();
//                ChannelFuture sync = channelFuture.channel().closeFuture().sync();
//                channel = sync.channel();
//            } catch (Exception e) {
//                log.info("连接失败！");
//            }
//        } else {
//            start("L");
//        }
//    }

}
