package com.mqtt.tow.entity;

import lombok.Data;
@Data
public class ConnectOptions {

    private long connectTime = 10;

    private String serverIp = "127.0.0.1";

    private int port = 1883;

    private boolean keepalive = true;

    private boolean reuseaddr = true;

    private boolean tcpNodelay = true;

    private int workThread = 1;

    private MqttOpntions mqtt;

    public long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isKeepalive() {
        return keepalive;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }

    public boolean isReuseaddr() {
        return reuseaddr;
    }

    public void setReuseaddr(boolean reuseaddr) {
        this.reuseaddr = reuseaddr;
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }

    public void setTcpNodelay(boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
    }

    public int getWorkThread() {
        return workThread;
    }

    public void setWorkThread(int workThread) {
        this.workThread = workThread;
    }

    public MqttOpntions getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttOpntions mqtt) {
        this.mqtt = mqtt;
    }


}