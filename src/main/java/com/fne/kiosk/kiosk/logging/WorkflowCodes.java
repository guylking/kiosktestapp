package com.fne.kiosk.kiosk.logging;

public enum WorkflowCodes {
    ITEM_1(0, "");

    private int code;
    private String message;

    WorkflowCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
