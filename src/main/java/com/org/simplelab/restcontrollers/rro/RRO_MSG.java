package com.org.simplelab.restcontrollers.rro;

public enum  RRO_MSG {
    COURSE_NO_FOUND("course not found"),
    ENTITY_UPDATE_ENTITY_NO_FOUND("Failed to update: Entity not found."),
    ENTITY_UPDATE_ERROR("Error while updating entity");
    private final String msg;

    RRO_MSG (String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

}
