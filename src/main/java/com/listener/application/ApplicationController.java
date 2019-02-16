package com.listener.application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;


@Slf4j
@EnableJpaRepositories
@Component
public class ApplicationController implements CommandLineRunner {


    @Autowired
    UnitRepo unitRepo;

    @Autowired
    UnitTypeRepo unitTypeRepo;

    private String broker = "tcp://192.168.1.112:1883";
    private String clientId = "Listener Mqtt";
    private boolean cleanSession = true;
    String user = null;
    String password = null;
    String topic = "test";
    private int qos = 0;
    String message = "Testy listenera " + new Date();
    private byte[] payload = message.getBytes();


    @Override
    public void run(String... args) {


        List<Units> units = unitRepo.findAll();
        for (int i = 0; i < units.size(); i++) {
            log.info("Szczegoly unitu {}: {}", i, units.get(i));
        }

        List<UnitType> unitTypes = unitTypeRepo.findAll();
        log.info("Wszystkie typy {}", unitTypes);


        MqttController mqttController;
        try {
            mqttController = new MqttController(broker, clientId, cleanSession, user, password);

            mqttController.publish(topic, qos, payload);

            mqttController.subscribe(topic, qos);

        } catch (MqttException e) {
            log.error(e.toString());
        }


    }


}
