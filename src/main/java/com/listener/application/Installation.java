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


    Installation(RepoProvider repoProvider) {
        this.unitRepo = repoProvider.getUnitRepo();
        this.unitTypeRepo = repoProvider.getUnitTypeRepo();
        this.unitInputRepo = repoProvider.unitInputRepo;

    }


    @Override
    public void parse(String msg, int unit) {
        int cursor = Protocol.HEADER_LENGTH;
        unitId = unit;
        unitType = parseIntFromString(msg, cursor, Protocol.TYPE_LENGTH);
        cursor += Protocol.TYPE_LENGTH;
        inputCount = parseIntFromString(msg, cursor, Protocol.INSTALL_INPUT_COUNT_LENGTH);
        cursor += Protocol.INSTALL_INPUT_COUNT_LENGTH;

        inputType = new int[inputCount];

        for (int i = 0; i < inputCount; i++) {
            cursor += Protocol.INSTALL_INPUT_NUMBER_LENGTH;
            inputType[i] = parseIntFromString(msg, cursor, Protocol.INSTALL_INPUT_TYPE_LENGTH);
            cursor += Protocol.INSTALL_INPUT_TYPE_LENGTH;

        }
    }

    private int parseIntFromString(String msg, int cursor, int type) {

        return Integer.parseInt(msg.substring(cursor, cursor + type));
    }


    void persist() {




        boolean typeFlag = typeValidator(unitType);

        boolean unitFlag = unitValidator(unitId);


        if (unitFlag && !typeFlag) {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Unit unit = new Unit();
            unit.setNetIdent(unitId);
            unit.setUnitType(unitType);
            unit.setRegDate(timestamp);
            log.info("Installing new Unit: {}", unitId);
            unitRepo.save(unit);

            List<UnitInput> unitInput = new ArrayList<>();

            for (int i = 0; i < inputCount; i++) {
                UnitInput unitInput1 = new UnitInput();
                unitInput1.setUnitNetIdent(unitId);
                unitInput1.setInputNumber(i);
                unitInput1.setInputTypeId(inputType[i]);
                unitInput.add(unitInput1);
            }
            List<UnitInput> unitInputs = unitInputRepo.saveAll(unitInput);
            log.info("Installing inputs {}", unitInputs);


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

        List<Unit> units = unitRepo.getByUnit(unit);
        return units.isEmpty();

    }


}
