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

            this.setTransmitter(parseIntFromString(msg, cursor, Protocol.TRANSMITER_LENGTH));

            cursor += Protocol.TRANSMITER_LENGTH;

            this.setReceiver(parseIntFromString(msg, cursor, Protocol.RECEIVER_LENGTH));

            cursor += Protocol.RECEIVER_LENGTH;

            this.setMessageType(parseIntFromString(msg, cursor, Protocol.TYPE_LENGTH));

        } else {
            throw new IllegalArgumentException("MSG doesnt exist");
        }

    }

    boolean toProcess() {
        return receiver == Protocol.SEVER;
    }


    private int parseIntFromString(String msg, int cursor, int length) {
        try {
            return Integer.parseInt(msg.substring(cursor, cursor + length));
        } catch (NumberFormatException e) {
            log.error("{}", e);
        }
        return -1;
    }
}
