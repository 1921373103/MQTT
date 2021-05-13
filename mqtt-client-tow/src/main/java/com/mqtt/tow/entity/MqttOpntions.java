package com.mqtt.tow.entity;

import lombok.Data;

@Data
public class MqttOpntions {

        private String clientIdentifier = "";

        private String willTopic = "close";

        private String willMessage = "bey";

        private String userName = null;

        private String password = "";

        private boolean hasUserName = false;

        private boolean hasPassword = false;

        private boolean hasWillRetain = false;

        private int willQos = 1;

        private boolean hasWillFlag = true;

        private boolean hasCleanSession = true;

        private int KeepAliveTime = 300;

        public String getClientIdentifier() {
            return clientIdentifier;
        }

        public void setClientIdentifier(String clientIdentifier) {
            this.clientIdentifier = clientIdentifier;
        }

        public String getWillTopic() {
            return willTopic;
        }

        public void setWillTopic(String willTopic) {
            this.willTopic = willTopic;
        }

        public String getWillMessage() {
            return willMessage;
        }

        public void setWillMessage(String willMessage) {
            this.willMessage = willMessage;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isHasUserName() {
            return hasUserName;
        }

        public void setHasUserName(boolean hasUserName) {
            this.hasUserName = hasUserName;
        }

        public boolean isHasPassword() {
            return hasPassword;
        }

        public void setHasPassword(boolean hasPassword) {
            this.hasPassword = hasPassword;
        }

        public boolean isHasWillRetain() {
            return hasWillRetain;
        }

        public void setHasWillRetain(boolean hasWillRetain) {
            this.hasWillRetain = hasWillRetain;
        }

        public int getWillQos() {
            return willQos;
        }

        public void setWillQos(int willQos) {
            this.willQos = willQos;
        }

        public boolean isHasWillFlag() {
            return hasWillFlag;
        }

        public void setHasWillFlag(boolean hasWillFlag) {
            this.hasWillFlag = hasWillFlag;
        }

        public boolean isHasCleanSession() {
            return hasCleanSession;
        }

        public void setHasCleanSession(boolean hasCleanSession) {
            this.hasCleanSession = hasCleanSession;
        }

        public int getKeepAliveTime() {
            return KeepAliveTime;
        }

        public void setKeepAliveTime(int keepAliveTime) {
            KeepAliveTime = keepAliveTime;
        }
    }