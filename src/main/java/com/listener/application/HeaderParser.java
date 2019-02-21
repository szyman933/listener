package com.listener.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
class HeaderParser {

    private int transmitter;
    private int receiver;
    private int messageType;


    HeaderParser(String msg) {
        int cursor = 0;

        if (msg != null) {
            this.setTransmitter(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getTransmiter())));
            cursor += Protocol.getTransmiter();
            this.setReceiver(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getReceiver())));
            cursor += Protocol.getReceiver();
            this.setMessageType(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getType())));
        } else {
            throw new IllegalArgumentException("MSG doesnt exist");
        }

    }

    boolean toProcess() {
        return receiver == Protocol.getSEVER();
    }

}
