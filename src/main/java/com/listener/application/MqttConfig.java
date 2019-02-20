package com.listener.application;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties("mqttconfig")
class MqttConfig {

    private String broker;

    private String clientid;

    private boolean cleanSession;

    private String topic;

    private int qos;

    private String user;
    
    private String password;

}
