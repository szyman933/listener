package com.listener.application;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



import java.util.List;


@Slf4j
@Component
 public class ApplicationController implements CommandLineRunner {




    @Autowired
    UnitRepo unitRepo;

    public List<Units> getUnit(int netIdent) {

        return unitRepo.getByUnit(netIdent);
    }

    @Override
    public void run(String... args) {

        List<Units> units = unitRepo.findAll();
        for(int i=0; i< units.size();i++){
            log.info("Szczegoly unitu {}: {}",i,units.get(i));
        }


    }











}
