package com.mqtt.client.utils;

import com.mqtt.client.config.MqttReceriveCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MyMqttRecieveMessage {
 
	private static int QoS = 1;
	private static String Host = "tcp://127.0.0.1:1883";
	private static MemoryPersistence memoryPersistence = null;
	private static MqttConnectOptions mqttConnectOptions = null;
	private static MqttClient mqttClient  = null;

	public static void init(String clientId) {
		mqttConnectOptions = new MqttConnectOptions();
		memoryPersistence = new MemoryPersistence();
		log.info("MyMqttRecieveMessage", "init");
		if(null != memoryPersistence && null != clientId && null != Host) {
			try {
				 mqttClient = new MqttClient(Host, clientId, memoryPersistence);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			log.error("memoryPersistence clientId Host 有空值");
		}
		if(null != mqttConnectOptions) {
			mqttConnectOptions.setCleanSession(true);
			mqttConnectOptions.setConnectionTimeout(30);
			mqttConnectOptions.setKeepAliveInterval(45);
			if(null != mqttClient && !mqttClient.isConnected()) {
				mqttClient.setCallback(new MqttReceriveCallback());
				try {
					mqttClient.connect();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else {
				log.error("mqttClient is error");
			}
		}else {
			log.error("mqttConnectOptions is null");
		}
	}
	
	
    public static void recieve(String topic) {
        int[] Qos = {QoS};
        String[] topics = {topic};
        log.info("MyMqttRecieveMessage", "subTopic");
        if(null != mqttClient && mqttClient.isConnected()) {
        	if(null!=topics && null!=Qos && topics.length>0 && Qos.length>0) {
        		try {
        			mqttClient.subscribe(topics, Qos);
        		} catch (MqttException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}else {
        		log.error("there is error");
        	}
        }else {
        	init("123444");
        	recieve(topic);
        }
    }
 
 
}
	

