package com.org.simplelab.restcontrollers.rro;
/*
 * Request Response Object
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RRO<T> {
    boolean success;
    String action;
    String msg;
    T data;

    public static RRO<String> sendErrorMessage(String errormsg){
        RRO<String> rro = new RRO<>();
        rro.setAction(ACTION_TYPE.PRINT_MSG.name());
        rro.setMsg(errormsg);
        rro.setSuccess(false);
        return rro;
    }

    public static enum ACTION_TYPE {
        NOTHING,
        REDIRECT,
        LOAD_DATA,
        PRINT_MSG,
    }

    public static enum LAB_ACTION_TYPE {
        MODIFY_EQUIPMENT,
        ADVANCE_STEP
    }

    public static enum MSG {
        COURSE_NO_FOUND("course not found"),
        ENTITY_UPDATE_ENTITY_NO_FOUND("Failed to update: Entity not found."),
        ENTITY_UPDATE_ERROR("Error while updating entity"),
        USER_NO_FOUND("user not found");
        private final String msg;

        MSG(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

    }
}



