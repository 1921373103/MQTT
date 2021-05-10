package com.mqtt.tow.utils;

/**
 * 回调接口
 *
 * @ Author L
 * @ Date 2021/5/10 14:08
 * @ DESC
 */
public interface MqttListener {

    void callBack(String topic,String msg);

    void callThrowable(Throwable e);
}
