package com.mqtt.client.Thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties poolProperties) {
        return new ThreadPoolExecutor(
                poolProperties.getCoreSize()
                , poolProperties.getMaxSize()
                , poolProperties.getKeepAliveTime()
                , TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(10000000)
                , Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.AbortPolicy()
        );
    }

}