package com.listener.application;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
class FrameParser {

    private int transmitter;
    private int receiver;
    private int messageType;
    private int unitType;
    private int input;
    private int inputCount;
    private int[] inputType;
    private int[] registers;


    public FrameParser(String msg) {
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

    void parseInstallFrame(String msg) {
        int cursor = Protocol.getHeader();

        unitType = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getType()));
        cursor += Protocol.getType();
        inputCount = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getInstallInputCount()));
        cursor += Protocol.getInstallInputCount();

        for (int i = 0; i < inputCount; i++) {
            cursor += Protocol.getInstallInputNumber();
            inputType[i] = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getInstallInputType()));
            cursor += Protocol.getInstallInputType();

        }

    }


    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int[] getInputType() {
        return inputType;
    }

    public void setInputType(int[] inputType) {
        this.inputType = inputType;
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public int getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(int transmitter) {
        this.transmitter = transmitter;
    }
}
