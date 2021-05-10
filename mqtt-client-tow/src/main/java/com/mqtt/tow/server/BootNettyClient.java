package com.mqtt.tow.server;

import com.mqtt.tow.config.MqttHander;
import com.mqtt.tow.controller.BootstrapClient;
import com.mqtt.tow.entity.ConnectOptions;
import com.mqtt.tow.handle.PingRunnable;
import com.mqtt.tow.utils.CancelbleExecutorService;
import com.mqtt.tow.utils.Status;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

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
public class BootNettyClient extends AbstractBootstrapClient {

    CancelbleExecutorService executorService = new CancelbleExecutorService(2);

    private NioEventLoopGroup bossGroup;

    private Bootstrap bootstrap = new Bootstrap();

    @Override
    public void shutdown() {

    }

    @Override
    public void initEventPool() {
        bootstrap= new Bootstrap();
        bossGroup = new NioEventLoopGroup(4, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
    }

    @Override
    public Channel start() {
        initEventPool();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10485760)
                .option(ChannelOption.SO_RCVBUF, 10485760)
                .remoteAddress("127.0.0.1",1883)
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
        try {
            return bootstrap.connect("127.0.0.1", 1883).sync().channel();
        } catch (InterruptedException e) {
            log.info("连接失败！",e);
        }
        return null;
    }

    public void init() {

//        Bootstrap bootstrap = new Bootstrap();
        bossGroup = new NioEventLoopGroup(4);
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10485760)
                .option(ChannelOption.SO_RCVBUF, 10485760)
                .localAddress(1883)
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

        connection();
/*        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 1883);

        channelFuture.addListener((ChannelFuture future) -> {
            whenConnectFutureResult(future, "127.0.0.1", 1883);
        });*/
    }

    /**
     * 连接
     *
     * @return
     */
    protected ChannelFuture connection() {

        String host = "127.0.0.1";
        Integer port = 1883;


        ChannelFuture channelFuture = bootstrap.connect(host, port);


        channelFuture.addListener((ChannelFuture future) -> {
            whenConnectFutureResult(future, host, port);
        });

        return channelFuture;
    }

    private void whenConnectFutureResult(ChannelFuture future, String host, Integer port) {


        AtomicBoolean hasConnect = Status.hasConnect;

        if (future.isSuccess()) {

            hasConnect.set(true);

            PingRunnable pingRunnable = new PingRunnable(future.channel(),
                    BootNettyClient.this,
                    10,
                    executorService);

            pingRunnable.updatehasResp(true);
            executorService.schedule(pingRunnable, 10, TimeUnit.SECONDS);


            if (log.isDebugEnabled()) {
                log.debug("connect the server successful!{},{}", host, port);
            }


            /*if (afterConnect != null) {
                afterConnect.accept(Connetor.this);
            }*/




        } else {
            /**
             * 连接失败，重新连接
             */
            if (hasConnect.get()) {
//                log.info(connectorOption.getDeviceId() + "已经连接,无需重复连接");
                return;
            }
            executorService.schedule(() -> {
                reconnection(future.channel());
            }, 10, TimeUnit.SECONDS);
            log.warn("连接失败");
        }
    }

    /**
     * 重连接
     *
     * @return
     */
    public void reconnection(Channel channel) {


        log.warn("开始重连");
        AtomicBoolean hasConnect = Status.hasConnect;
        /**
         * 清理调调度线程池
         */
        executorService.reset();
        hasConnect.set(false);

        if (channel != null && channel.isActive()) {
            channel.close();

        }


        start();
    }

}
