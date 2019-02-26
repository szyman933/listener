package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    RequestHandler requestHandler;



    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", new Date(System.currentTimeMillis()));
        requestHandler.checkRequest();
        UnitRequest unitRequest = new UnitRequest();
        unitRequest.setId(1);
        unitRequest.setRequestType(0);
        unitRequest.setUnitNetIdent(5);
        unitRequest.setUnitInputNumber(24);

        requestHandler.requestBuilder(unitRequest);

    }


}







