package com.mqtt.tow.bootstrap;

import cn.hutool.core.util.RandomUtil;
import com.mqtt.tow.server.BootNettyClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ Author L
 * @ Date 2021/5/8 15:15
 * @ DESC
 */
@Component
public class ClientTask implements Runnable{

    @Autowired
    public BootNettyClient bootNettyClient;

    @SneakyThrows
    @Override
    public void run() {
        bootNettyClient.start(RandomUtil.randomString(32));
    }


}
