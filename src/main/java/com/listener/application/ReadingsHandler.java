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
    private int[] register;
    private int[] read;


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
            int readLoadLength = msg.length() - Protocol.getHeader();
            readCount = readLoadLength / (Protocol.getReadInput() + Protocol.getReadRegister() + Protocol.getReadData());
            log.info("There is {} reads in frame", readCount);
        } else {
            throw new IllegalArgumentException("Message can't be NULL or Empty !!!");
        }

    }


    private void parseReads(String msg) {
        int cursor = Protocol.getHeader();
        input = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getReadInput()));
        cursor = cursor + Protocol.getReadInput();

        register=new int[readCount];
        read=new int[readCount];

        if (readCount > 0) {

            for (int i = 0; i < readCount; i++) {
                register[i]=(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getReadRegister())));
                cursor += Protocol.getReadRegister();
                read[i]=(Integer.parseInt(msg.substring(cursor, cursor + Protocol.getReadData())));
                cursor = Protocol.getReadData();
            }

        } else {
            log.info("Readings frame has no reads !!");
        }

    }


    void persist() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Readings> readings = new ArrayList<>();
        if (readCount > 0) {
            for (int i = 0; i < readCount; i++) {
                Readings readings1 = new Readings();

                readings1.setUnitId(unitId);

                readings1.setParamId(register[i]);

                readings1.setValue(read[i]);

                readings1.setReadDate(timestamp);

                List<UnitInput> unitInputList = unitInputRepo.getByUnitAndInput(unitId, input);

                UnitInput unitInput = unitInputList.get(0);

                readings1.setUnitInputId(Math.toIntExact(unitInput.getId()));

                readings.add(readings1);
            }

            log.info("Saving read : {}", readingsRepo.saveAll(readings));

        } else {
            log.info("Nothing to save !!");
        }
    }


}
