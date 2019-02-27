package com.listener.application;


public class Protocol {


    static final int SEVER = 0;
    static final int HEADER_LENGTH = 6;
    static final int TRANSMITER_LENGTH = 2;
    static final int RECEIVER_LENGTH = 2;
    static final int TYPE_LENGTH = 2;

    static final int READ_INPUT_LENGTH = 2;
    static final int READ_REGISTER_LENGTH = 4;
    static final int READ_DATA_LENGTH = 5;

    static final int INSTALL_UNIT_TYPE_LENGTH = 2;
    static final int INSTALL_INPUT_COUNT_LENGTH = 2;
    static final int INSTALL_INPUT_NUMBER_LENGTH = 2;
    static final int INSTALL_INPUT_TYPE_LENGTH = 2;

    static final int REQUEST_INPUT_LENGTH = 2;
    static final int REQUEST_REGISTER_LENGTH = 4;
    static final int REQUEST_DATA_LENGTH = 5;

    static final int SCHEDULE_INTERVAL_LENGTH = 4;

    static final int PLC_SAVE_VALUE_LENGTH = 4;

    static final int ACTIVE_PARAM_REGISTER_COUNT_LENGTH = 3;
    static final int ACTIVE_PARAM_REGISTER_LENGTH = 3;
















    Protocol() {
    }


    public static int readCountInFrame(int length){

        int readLoadLength = length - HEADER_LENGTH;
        return readLoadLength / (READ_INPUT_LENGTH + READ_REGISTER_LENGTH + READ_DATA_LENGTH);

    }

}
