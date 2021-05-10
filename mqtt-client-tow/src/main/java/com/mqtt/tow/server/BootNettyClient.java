package com.mqtt.tow.server;

import cn.hutool.socket.nio.NioServer;
import com.mqtt.tow.config.MqttHander;
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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ Author L
 * @ Date 2021/5/10 14:53
 * @ DESC
 */
@Slf4j
public class BootNettyClient {

    private NioEventLoopGroup bossGroup;

    private Bootstrap bootstrap;

    public void initEventPool() {
        bootstrap= new Bootstrap();
        bossGroup = new NioEventLoopGroup(4, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
    }

    public void start() {
        initEventPool();
        try {
            bossGroup = new NioEventLoopGroup(1);

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
                            channelPipeline.addLast(new IdleStateHandler(60,60,60));
                            channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            channelPipeline.addLast("decoder",new MqttDecoder());
                            channelPipeline.addLast(new MqttHander());
                        }
                    });
            ChannelFuture connect = bootstrap.connect("127.0.0.1", 1883);
            System.out.println("connect = " + connect);
        } catch (Exception e) {
            log.error("连接失败！");
        }
    }

}
