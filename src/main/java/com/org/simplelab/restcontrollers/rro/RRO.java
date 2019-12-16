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

    public static RRO sendErrorMessage(String errormsg){
        RRO<String> rro = new RRO<>();
        rro.setAction(ACTION_TYPE.PRINT_MSG.name());
        rro.setMsg(errormsg);
        rro.setSuccess(false);
        return rro;
    }

    public RRO withData(T data){
        this.data = data;
        return this;
    }

    public enum ACTION_TYPE {
        NOTHING,
        REDIRECT,
        LOAD_DATA,
        PRINT_MSG,
    }

    public enum LAB_ACTION_TYPE {
        MODIFY_EQUIPMENT,
        ADVANCE_STEP,
        COMPLETE_LAB
    }

    public enum MSG {
        COURSE_NO_FOUND("course not found"),
        ENTITY_UPDATE_ENTITY_NO_FOUND("Failed to update: Entity not found."),
        ENTITY_UPDATE_ERROR("Error while updating entity"),
        RECIPE_NOT_FOUND("Recipe not found"),
        USER_NO_FOUND("user not found"),
        EQUIPMENT_DELETE_ERROR("equipment is already used");

        private final String msg;

        MSG(String msg) {
            this.msg = msg;
        }
        public String getMsg() {
            return msg;
        }

    }
}



