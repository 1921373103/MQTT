package com.mqtt.server.server;

import com.mqtt.common.ip.IpUtils;
import com.mqtt.server.adapter.BootChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ Author L
 * @ Date 2021/4/30 9:13
 * @ DESC
 */
@Slf4j
public class BootNettyServer {

    private int port = 1883;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;

    private Channel serverChannel;

    private ServerBootstrap bootstrap;

    /**
     * 	启动服务
     * @throws InterruptedException
     */
    public void startup() {
        try{
            bossGroup = new NioEventLoopGroup(1);
            workGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 32)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline channelPipeline = ch.pipeline();
                    // 设置读写空闲超时时间
                    channelPipeline.addLast(new IdleStateHandler(600, 600, 1200));
                    channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                    channelPipeline.addLast("decoder", new MqttDecoder());
                    channelPipeline.addLast(new BootChannelInboundHandler());
                }
            });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            System.out.println("start exception"+e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        if(workGroup!=null && bossGroup!=null ){
            try {
                bossGroup.shutdownGracefully().sync();// 优雅关闭
                workGroup.shutdownGracefully().sync();
                System.out.println("shutdown success");
            } catch (InterruptedException e) {
                log.info("服务端关闭资源失败【" + IpUtils.getHost() + ":" + port + "】");
            }
        }
    }


}
