package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    RequestHandler requestHandler;

    @Autowired
    ParamsMapRepo paramsMapRepo;



    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", new Date(System.currentTimeMillis()));

        requestHandler.checkRequest();



    }


}







