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

    @Autowired
    ParamsMapRepo paramsMapRepo;

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


    }


    String requestBuilder(UnitRequest unitRequest) {


        String message = "";
        String transmiterFormat = "%0" + Protocol.TRANSMITER_LENGTH + "d";
        String receiverFormat = "%0" + Protocol.RECEIVER_LENGTH + "d";
        String typeFormat = "%0" + Protocol.TYPE_LENGTH + "d";
        String inputFormat = "%0" + Protocol.REQUEST_INPUT_LENGTH + "d";
        String scheduleFormat = "%0" + Protocol.SCHEDULE_INTERVAL_LENGTH + "d";
        String requestRegisterFormat = "%0" + Protocol.REQUEST_REGISTER_LENGTH + "d";
        String plcSaveValueFormat = "%0" + Protocol.PLC_SAVE_VALUE_LENGTH + "d";
        String plcActiveRegisterCountFormat = "%0" + Protocol.ACTIVE_PARAM_REGISTER_COUNT_LENGTH + "d";
        String plcActiveRegisterFormat = "%0" + Protocol.ACTIVE_PARAM_REGISTER_LENGTH + "d";


        StringBuilder sB = new StringBuilder(message);

        switch (unitRequest.getRequestType()) {

            case 1:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                sB.append(String.format(scheduleFormat, unitRequest.getValue()));
                message = sB.toString();
                break;

            case 2:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                sB.append(String.format(requestRegisterFormat, unitRequest.getValue()));
                message = sB.toString();

                break;

            case 3:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                message = sB.toString();
                break;

            case 5:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                sB.append(String.format(requestRegisterFormat, unitRequest.getRegister()));
                sB.append(String.format(plcSaveValueFormat, unitRequest.getValue()));
                message = sB.toString();
                break;

            case 6:
                List<ParamsMap> paramsMap = paramsMapRepo.getByActiveAndRead();

                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                //TODO dokończyć skłądanie requesta z aktywnymi parametrami


                break;

            default:
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                message = sB.toString();
                break;


        }


        return message;
    }

}
