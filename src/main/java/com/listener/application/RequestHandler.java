package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class RequestHandler {

    @Autowired
    UnitRequestRepo unitRequestRepo;

    private List<UnitRequest> unitRequests;

    boolean checkRequest() {
        boolean flag = false;

        unitRequests = unitRequestRepo.findBySendDateIsNull();
        if (unitRequests.isEmpty()) {

            log.info("Nie ma requestow do wyslania");

        } else {

            log.info("Oczekujace requesty : {}", unitRequests);
            flag = true;
        }

        return flag;
    }


    void sendRequest() {

        String message = "00";

        int messageCount = unitRequests.size();


        //  UnitRequest unitRequest = unitRequests.remove();


    }


    String requestBuilder(UnitRequest unitRequest) {


        String message = "";
        String transmiterFormat = "%0" + Protocol.TRANSMITER_LENGTH + "d";
        String receiverFormat = "%0" + Protocol.RECEIVER_LENGTH + "d";
        String typeFormat = "%0" + Protocol.TYPE_LENGTH + "d";
        String inputFormat = "%0" + Protocol.REQUEST_INPUT_LENGTH + "d";

        StringBuilder sB = new StringBuilder(message);

        switch (unitRequest.getRequestType()) {


            case 0:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                message = sB.toString();
                break;


            case 1:
                break;


        }


        return message;
    }

}
