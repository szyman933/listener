package com.listener.application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



import java.util.List;


@Slf4j
@Component
 public class ApplicationController implements CommandLineRunner {




    @Autowired
    UnitRepo unitRepo;

    private String broker = "tcp://192.168.1.112:1883";
    private String clientId = "Listener Mqtt";
    private boolean cleanSession = true;
    private boolean quietMode = false;
    String user = null;
    String password = null;
    String topic		= "test";
    private int qos = 0;
    String message = "Testy listenera Mqtt wersja 1";
    private byte[] payload = message.getBytes();


    public List<Units> getUnit(int netIdent) {

        return unitRepo.getByUnit(netIdent);
    }

    @Override
    public void run(String... args) {

        List<Units> units = unitRepo.findAll();
        for(int i=0; i< units.size();i++){
            log.info("Szczegoly unitu {}: {}",i,units.get(i));
        }


        MqttController mqttController= null;
        try {
            mqttController = new MqttController(broker,clientId,cleanSession,quietMode,user,password);

            mqttController.publish(topic,qos,payload);

            mqttController.subscribe(topic,qos);

        } catch (MqttException e) {
            e.printStackTrace();
        }








    }











}
