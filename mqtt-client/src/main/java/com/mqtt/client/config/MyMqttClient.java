package com.mqtt.client.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
public class MyMqttClient  {
 
	public MqttClient mqttClient = null;
	private MemoryPersistence memoryPersistence = null;
	private MqttConnectOptions mqttConnectOptions = null;

	public void init(String clientId) {
		//初始化连接设置对象
		mqttConnectOptions = new MqttConnectOptions();
		//初始化MqttClient
		if(null != mqttConnectOptions) {
//			true可以安全地使用内存持久性作为客户端断开连接时清除的所有状态
			mqttConnectOptions.setCleanSession(true);
//			设置连接超时
			mqttConnectOptions.setConnectionTimeout(30);
//			设置持久化方式
			memoryPersistence = new MemoryPersistence();
			if(null != memoryPersistence && null != clientId) {
				try {
					mqttClient = new MqttClient("tcp://127.0.0.1:1883", clientId,memoryPersistence);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				
			}
		}else {
			System.out.println("mqttConnectOptions对象为空");
		}
		
		// System.out.println(mqttClient.isConnected());
		//设置连接和回调
		if(null != mqttClient) {
			if(!mqttClient.isConnected()) {
			
//			创建回调函数对象
				MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback();
//			客户端添加回调函数
				mqttClient.setCallback(mqttReceriveCallback);
//			创建连接
				try {
					System.out.println("创建连接");
					mqttClient.connect(mqttConnectOptions);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		}else {
			System.out.println("mqttClient为空");
		}
		System.out.println(mqttClient.isConnected());
		publishMessage("123", "hello Wrold!", 1);
	}
	
//	关闭连接
	public void closeConnect() {
		//关闭存储方式
		if(null != memoryPersistence) {
			try {
				memoryPersistence.close();
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("memoryPersistence is null");
		}
		
//		关闭连接
		if(null != mqttClient) {
			if(mqttClient.isConnected()) {
				try {
					mqttClient.disconnect();
					mqttClient.close();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println("mqttClient is not connect");
			}
		}else {
			System.out.println("mqttClient is null");
		}
	}
	
//	发布消息
	public void publishMessage(String pubTopic,String message,int qos) {
		if(null != mqttClient&& mqttClient.isConnected()) {
			System.out.println("发布消息   "+mqttClient.isConnected());
			System.out.println("id:"+mqttClient.getClientId());
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(qos);
			mqttMessage.setPayload(message.getBytes());
			
			MqttTopic topic = mqttClient.getTopic(pubTopic);
			
			if(null != topic) {
				try {
					MqttDeliveryToken publish = topic.publish(mqttMessage);
					if(!publish.isComplete()) {
						System.out.println("消息发布成功");
					}
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			reConnect();
		}
	}
//	重新连接
	public void reConnect() {
		if(null != mqttClient) {
			if(!mqttClient.isConnected()) {
				if(null != mqttConnectOptions) {
					try {
						mqttClient.connect(mqttConnectOptions);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.println("mqttConnectOptions is null");
				}
			}else {
				System.out.println("mqttClient is null or connect");
			}
		}else {
			init("123");
		}
		
	}
//	订阅主题
	public void subTopic(String topic) {
		if(null != mqttClient&& mqttClient.isConnected()) {
			try {
				mqttClient.subscribe(topic, 1);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("mqttClient is error");
		}
	}
	
	
//	清空主题
	public void cleanTopic(String topic) {
		if(null != mqttClient&& !mqttClient.isConnected()) {
			try {
				mqttClient.unsubscribe(topic);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("mqttClient is error");
		}
	}
 
}
