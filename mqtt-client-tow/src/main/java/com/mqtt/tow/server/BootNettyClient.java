package com.mqtt.tow.server;

import cn.hutool.core.util.RandomUtil;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    public EventLoopGroup bossGroup;

    public void start(String clientId) {
        MqttOpntions mqttOpntions = new MqttOpntions();
        mqttOpntions.setClientIdentifier(clientId);
        Bootstrap bootstrap = new Bootstrap();
        bossGroup = new NioEventLoopGroup(1);
//        try {
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
                            channelPipeline.addLast(new IdleStateHandler(60, 0, 0));
                            channelPipeline.addLast(new MqttHander());
                        }
                    });

            /*ChannelFuture channelFuture = bootstrap.connect("localhost", 1883).sync();
            ChannelFuture sync = channelFuture.channel().closeFuture().sync();
            Channel channel = sync.channel();*/

            // 创建客户端连接
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 1883);
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    System.out.println("connect failed, exit!");
                    channelFuture.channel().eventLoop().schedule(() -> start(RandomUtil.randomString(32)), 5, TimeUnit.SECONDS);
                }
            });
        try {
            channelFuture.get();
//            CompletableFuture completableFuture = new CompletableFuture();
//            completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        /*} catch (Exception e) {
            log.error("fail");
        } finally {
            bossGroup.shutdownGracefully().sync();
        }*/



    }


}
