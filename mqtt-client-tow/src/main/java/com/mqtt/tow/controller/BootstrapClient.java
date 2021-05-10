package com.mqtt.tow.controller;

import io.netty.channel.Channel;

public interface BootstrapClient {


    void shutdown();

    void initEventPool();

    Channel start();


}