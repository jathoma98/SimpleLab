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
        rro.setAction(RRO_ACTION_TYPE.PRINT_MSG.name());
        rro.setMsg(errormsg);
        rro.setSuccess(false);
        return rro;
    }
}



