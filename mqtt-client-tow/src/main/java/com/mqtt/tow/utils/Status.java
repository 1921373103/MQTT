package com.mqtt.tow.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class Status {

	public static final AtomicBoolean hasConnect = new AtomicBoolean(false);
	public static final AtomicBoolean isLogin=new AtomicBoolean(false);
}