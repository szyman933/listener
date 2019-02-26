package com.listener.application;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class ReadingsHandler implements Frame {


    ReadingsRepo readingsRepo;

    UnitInputRepo unitInputRepo;

    private int unitId;
    private int readCount;
    private int input;
    List<RegisterRead> registerReadList;


    ReadingsHandler(RepoProvider repoProvider) {
        this.readingsRepo = repoProvider.getReadingsRepo();
        this.unitInputRepo = repoProvider.getUnitInputRepo();

    }

    @Override
    public void parse(String s, int unit) {
        readCount(s);
        parseReads(s);
        unitId = unit;
    }


    private void readCount(String msg) {

        if (msg != null && !msg.isEmpty()) {

            readCount = Protocol.readCountInFrame(msg.length());

            log.info("There is {} reads in frame", readCount);
        } else {
            throw new IllegalArgumentException("Message can't be NULL or Empty !!!");
        }

    }


    private void parseReads(String msg) {


        if (readCount > 0) {
            registerReadList = new ArrayList<>();

            int cursor = Protocol.HEADER_LENGTH;
            input = parseIntFromString(msg, cursor, Protocol.READ_INPUT_LENGTH);
            cursor = cursor + Protocol.READ_INPUT_LENGTH;


            for (int i = 0; i < readCount; i++) {

                int reg;
                int rd;
                reg = (parseIntFromString(msg, cursor, Protocol.READ_REGISTER_LENGTH));
                cursor += Protocol.READ_REGISTER_LENGTH;
                rd = (parseIntFromString(msg, cursor, Protocol.READ_DATA_LENGTH));
                cursor = Protocol.READ_DATA_LENGTH;
                registerReadList.add(new RegisterRead(reg, rd));
            }

        } else {
            log.info("Reading frame has no reads !!");
        }

    }

    private int parseIntFromString(String msg, int cursor, int length) {
        try {
            return Integer.parseInt(msg.substring(cursor, cursor + length));
        } catch (NumberFormatException e) {
            log.error("{}", e);
        }
        return -1;
    }


    void persist() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Reading> readings = new ArrayList<>();

        List<UnitInput> unitInputList = unitInputRepo.getByUnitAndInput(unitId, input);

        UnitInput unitInput = unitInputList.get(0);

        int unitInputId = (Math.toIntExact(unitInput.getId()));

        if (readCount > 0) {
            for (RegisterRead registerRead : registerReadList) {

                Reading reading = new Reading();

                reading.setUnitId(unitId);

                reading.setParamId(registerRead.getRegister());

                reading.setValue(registerRead.getRead());

                reading.setReadDate(timestamp);

                reading.setUnitInputId(unitInputId);

                readings.add(reading);
            }

            List<Reading> arg = readingsRepo.saveAll(readings);
            log.info("Saving read : {}", arg);

        } else {
            log.info("Nothing to save !!");
        }
    }


    @Getter
    class RegisterRead {
        private int register;
        private int read;

        public RegisterRead() {
        }

        public RegisterRead(int register, int read) {
            this.register = register;
            this.read = read;
        }
    }

}
