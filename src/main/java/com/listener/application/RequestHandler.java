package com.listener.application;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Component
class RequestHandler {


    @Autowired
    RepoProvider repoProvider;

    @Autowired
    private MqttConfig mqttConfigPublisher;

    private List<UnitRequest> unitRequests;

    boolean checkRequest() {
        boolean flag = false;

        unitRequests = repoProvider.unitRequestRepo.findBySendDateIsNull();

        if (unitRequests.isEmpty()) {

            log.info("Nie ma requestow do wyslania");

        } else {

            log.info("Oczekujace requesty : {}", unitRequests);
            flag = true;
        }

        return flag;
    }


    void sendRequest() {

        mqttConfigPublisher.setClientid("Publisher");

        MqttController mqttControllerPublisher = new MqttController(repoProvider, mqttConfigPublisher);

        for (UnitRequest request : unitRequests) {
            boolean send = false;
            String message = requestBuilder(request);

            try {
                send = mqttControllerPublisher.publish(message);
            } catch (MqttException e) {
                log.error(e.toString());
            }


            if (send) {
                request.setSendDate(new Timestamp(System.currentTimeMillis()));
                repoProvider.unitRequestRepo.save(request);
                repoProvider.unitRequestRepo.flush();

            }

        }

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
                List<ParamsMap> paramsMap = repoProvider.paramsMapRepo.getByActiveAndRead();
                sB.append(String.format(transmiterFormat, Protocol.SEVER));
                sB.append(String.format(receiverFormat, unitRequest.getUnitNetIdent()));
                sB.append(String.format(typeFormat, unitRequest.getRequestType()));
                sB.append(String.format(inputFormat, unitRequest.getUnitInputNumber()));
                sB.append(String.format(plcActiveRegisterCountFormat, paramsMap.size()));
                for (ParamsMap map : paramsMap) {
                    sB.append(String.format(plcActiveRegisterFormat, map.getIndex()));
                }
                message = sB.toString();
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
