package com.mqtt.client.Thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "properties.thread.pool")
@Component
@Getter
@Setter
public class ThreadPoolProperties {

    /**
     * 默认线程数
     */
    private Integer coreSize;

    /**
     * 最大线程数
     */
    private Integer maxSize;

    /**
     * 超时存活时间
     */
    private Integer keepAliveTime;

}