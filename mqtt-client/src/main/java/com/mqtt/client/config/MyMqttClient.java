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

	public void init(String clientId) throws MqttException {
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
					mqttClient = new MqttClient("tcp://127.0.0.1:1883", clientId, memoryPersistence);
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
			if(mqttClient.isConnected()) {
			
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
		// publishMessage("123", "hello Wrold!", 1);
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

	/**
	 * 	发布消息
	 */
	public void publishMessage(String pubTopic,String message,int qos) throws MqttException {
		MqttDeliveryToken publish = null;
		reConnect();
		if(null != mqttClient&& mqttClient.isConnected()) {
			System.out.println("发布消息   "+mqttClient.isConnected());
			System.out.println("id:"+mqttClient.getClientId());
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(qos);
			mqttMessage.setPayload(message.getBytes());
			
			MqttTopic topic = mqttClient.getTopic(pubTopic);
			
			if(null != topic) {
				try {
					publish = topic.publish(mqttMessage);
					if(publish.isComplete() == false) {
						System.out.println("消息发布成功");
						// subTopic(pubTopic);
					}
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			reConnect();
		}
		publish.waitForCompletion();
	}


	/**
	 * 	重新连接
	 */
	public void reConnect() throws MqttException {
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

	/**
	 * 订阅主题 此方法默认的的Qos等级为：1
	 */
	public void subTopic(String topic) throws MqttException {
		reConnect();
		if(null != mqttClient && mqttClient.isConnected()) {
			try {
				mqttClient.subscribe(topic);
				System.out.println("订阅成功 = " +  topic);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("链接异常");
		}
	}

	/**
	 * 订阅某一个主题，可携带Qos
	 *
	 * @param topic 所要订阅的主题
	 * @param qos   消息质量：0、1、2
	 */
	public void subTopic(String topic, int qos) throws MqttException {
		reConnect();
		if(null != mqttClient && mqttClient.isConnected()) {
			try {
				mqttClient.subscribe(topic, qos);
				System.out.println("订阅成功 = " +  topic);
				System.out.println("消息质量 = " +  qos);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("链接异常");
		}
	}

	/**
	 * 消息订阅，消息侦听器
	 */


	/**
	 * 清空主题
	 */
	public void cleanTopic(String topic) throws MqttException {
		reConnect();
		if(null != mqttClient&& mqttClient.isConnected()) {
			try {
				mqttClient.unsubscribe(topic);
				System.out.println("成功清空主题 = " +  topic);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("清空失败 = " +  topic);
			}
		}else {
			System.out.println("mqttClient is error");
		}
	}
 
}
