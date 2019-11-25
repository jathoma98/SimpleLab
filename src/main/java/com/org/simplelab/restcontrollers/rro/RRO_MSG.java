package com.org.simplelab.restcontrollers.rro;

public enum  RRO_MSG {
    COURSE_NO_FOUND("course not found");
    private final String msg;

    RRO_MSG (String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

}
