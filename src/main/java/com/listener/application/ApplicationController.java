package com.listener.application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;


import java.util.Date;


@Slf4j
@EnableJpaRepositories
@Component
public class ApplicationController implements CommandLineRunner {

    @Autowired
    RepoProvider repoProvider;

    @Autowired
    private MqttConfig mqttConfig;


    private String message = "Testy listenera " + new Date();

    private byte[] payload = message.getBytes();


    @Override
    public void run(String... args) {


        String topic = mqttConfig.getTopic();
        int qos = mqttConfig.getQos();

        MqttController mqttController = new MqttController(repoProvider, mqttConfig);

        try {

            mqttController.publish(topic, qos, payload);

            mqttController.subscribe(topic, qos);

        } catch (MqttException e) {
            log.error(e.toString());
        }


    }


}
