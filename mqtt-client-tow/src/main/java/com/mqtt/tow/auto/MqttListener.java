package com.mqtt.tow.auto;

public interface MqttListener{

    void callBack(String topic,String msg);

    void callThrowable(Throwable e);
}