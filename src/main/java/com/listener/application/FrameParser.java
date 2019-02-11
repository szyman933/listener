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
    private int readCount;
    private int[] registers;
    private int[] reads;


    public FrameParser(String msg) {
        int cursor = 0;

        if (msg != null) {
            this.setTransmitter(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getTRANSMITER())));
            cursor += Protocol.getTRANSMITER();
            this.setReceiver(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getRECEIVER())));
            cursor += Protocol.getRECEIVER();
            this.setMessageType(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getTYPE())));
        } else {
            throw new IllegalArgumentException("MSG doesnt exist");
        }

    }

    boolean toProcess() {
        return receiver == Protocol.getSEVER();
    }

    int readCount(String msg) {

        if (msg != null && !msg.isEmpty()) {
            int readLoadLength = msg.length() - Protocol.getHEADER();
            readCount = readLoadLength / (Protocol.getReadInput() + Protocol.getReadRegister() + Protocol.getReadData());
        } else {
            throw new IllegalArgumentException("Message can't be NULL or Empty !!!");
        }

        return readCount;
    }


    void parseReads(String msg) {
        int cursor = Protocol.getHEADER();
        input = Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadInput()));
        cursor = cursor + Protocol.getReadInput();
        if (readCount > 0) {


            for (int i = 0; i < readCount; i++) {
                registers[i] = Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadRegister()));
                cursor += Protocol.getReadRegister();
                reads[i] = Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadData()));
                cursor = Protocol.getReadData();

            }


        }


    }

    void parseInstallFrame(String msg) {
        int cursor = Protocol.getHEADER();

        unitType = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getTYPE()));
        cursor += Protocol.getTYPE();
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

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public int[] getReads() {
        return reads;
    }

    public void setReads(int[] reads) {
        this.reads = reads;
    }

    public int getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(int transmitter) {
        this.transmitter = transmitter;
    }
}
