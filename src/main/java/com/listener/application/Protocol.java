package com.listener.application;


public class Protocol {


    private static final int SEVER = 0;
    private static final int HEADER = 6;
    private static final int TRANSMITER = 2;
    private static final int RECEIVER = 2;
    private static final int TYPE = 2;

    private static final int READ_INPUT = 2;
    private static final int READ_REGISTER = 4;
    private static final int READ_DATA = 5;

    private static final int INSTALL_UNIT_TYPE = 2;
    private static final int INSTALL_INPUT_COUNT = 2;
    private static final int INSTALL_INPUT_NUMBER = 2;
    private static final int INSTALL_INPUT_TYPE = 2;

    private static final int REQUEST_INPUT = 2;
    private static final int REQUEST_REGISTER = 4;
    private static final int REQUEST_DATA = 5;

    private static final int ACTIVE_PARAM_INPUT = 2;
    private static final int ACTIVE_PARAM_REGISTER_COUNT = 3;
    private static final int ACTIVE_PARAM_REGISTER = 3;


    Protocol() {
    }

    public static int getSEVER() {
        return SEVER;
    }

    public static int getHEADER() {
        return HEADER;
    }

    public static int getTRANSMITER() {
        return TRANSMITER;
    }

    public static int getRECEIVER() {
        return RECEIVER;
    }

    public static int getTYPE() {
        return TYPE;
    }

    public static int getReadInput() {
        return READ_INPUT;
    }

    public static int getReadRegister() {
        return READ_REGISTER;
    }

    public static int getReadData() {
        return READ_DATA;
    }

    public static int getInstallUnitType() {
        return INSTALL_UNIT_TYPE;
    }

    public static int getInstallInputCount() {
        return INSTALL_INPUT_COUNT;
    }

    public static int getInstallInputNumber() {
        return INSTALL_INPUT_NUMBER;
    }

    public static int getInstallInputType() {
        return INSTALL_INPUT_TYPE;
    }

    public static int getRequestInput() {
        return REQUEST_INPUT;
    }

    public static int getRequestRegister() {
        return REQUEST_REGISTER;
    }

    public static int getRequestData() {
        return REQUEST_DATA;
    }

    public static int getActiveParamInput() {
        return ACTIVE_PARAM_INPUT;
    }

    public static int getActiveParamRegisterCount() {
        return ACTIVE_PARAM_REGISTER_COUNT;
    }

    public static int getActiveParamRegister() {
        return ACTIVE_PARAM_REGISTER;
    }

}
