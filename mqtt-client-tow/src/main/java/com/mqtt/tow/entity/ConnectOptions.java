package com.mqtt.tow.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ Author L
 * @ Date 2021/5/10 15:44
 * @ DESC
 */
@Component
@ConfigurationProperties(prefix ="mqtt.client")
@Data
public class ConnectOptions {

    private long connectTime = 10;

    private String serverIp = "127.0.0.1";

    private int port = 1883;

    private boolean keepalive = true;

    private boolean reuseaddr = true;

    private boolean tcpNodelay = true;

    private int backlog = 1024;

    private  int  sndbuf = 10485760;

    private int revbuf = 10485760;

    private int heart = 60;

    private boolean ssl = false;

    private int bossThread;

    private int workThread;

    private MqttOpntions mqtt;

    @Data
    public static class MqttOpntions{

        private  String clientIdentifier = "1234";

        private  String willTopic = "close";

        private  String willMessage = "bey";

        private  String userName = null;

        private  String password = null;

        private  boolean hasUserName = false;

        private  boolean hasPassword = false;

        private  boolean hasWillRetain = true;

        private  int willQos = 1;

        private  boolean hasWillFlag = true;

        private  boolean hasCleanSession = true;

        private int KeepAliveTime = 10;


    }




}
