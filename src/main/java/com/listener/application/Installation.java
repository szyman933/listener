package com.listener.application;


import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Slf4j
class Installation implements Frame {


    private int unitId;
    private int unitType;
    private int inputCount;
    private int[] inputType;


    UnitRepo unitRepo;

    UnitInputRepo unitInputRepo;

    UnitTypeRepo unitTypeRepo;


    Installation(UnitRepo ur, UnitTypeRepo utr, UnitInputRepo uir) {
        this.unitRepo = ur;
        this.unitTypeRepo = utr;
        this.unitInputRepo = uir;

    }


    @Override
    public void parse(String msg, int unit) {
        int cursor = Protocol.getHeader();
        unitId = unit;
        unitType = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getType()));
        cursor += Protocol.getType();
        inputCount = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getInstallInputCount()));
        cursor += Protocol.getInstallInputCount();

        inputType = new int[inputCount];

        for (int i = 0; i < inputCount; i++) {
            cursor += Protocol.getInstallInputNumber();
            inputType[i] = Integer.parseInt(msg.substring(cursor, cursor + Protocol.getInstallInputType()));
            cursor += Protocol.getInstallInputType();

        }
    }


    void persist() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        boolean typeFlag = typeValidator(unitType);

        boolean unitFlag = unitValidator(unitId);


        if (unitFlag && !typeFlag) {

            Units unit = new Units();
            unit.setNetIdent(unitId);
            unit.setUnitType(unitType);
            unit.setRegDate(timestamp);
            log.info("Installing new Unit: {}",unitId);
            unitRepo.save(unit);

            List<UnitInput> unitInput = new ArrayList<>();

            for (int i = 0; i < inputCount; i++) {
                UnitInput unitInput1 = new UnitInput();
                unitInput1.setUnitNetIdent(unitId);
                unitInput1.setInputNumber(i);
                unitInput1.setInputTypeId(inputType[i]);
                unitInput.add(unitInput1);
            }

            log.info("Installing inputs {}", unitInputRepo.saveAll(unitInput));


        } else {

            if (!unitFlag) {
                log.info("Unit {} already installed", unitId);
            }


            if (typeFlag) {
                log.info("Unit type undefined, unknown module");
            }

        }


    }

    private boolean typeValidator(int type) {

        List<UnitType> unitTypes = unitTypeRepo.getByType(type);
        return unitTypes.isEmpty();

    }


    private boolean unitValidator(int unit) {

        List<Units> units = unitRepo.getByUnit(unit);
        return units.isEmpty();

    }


}
