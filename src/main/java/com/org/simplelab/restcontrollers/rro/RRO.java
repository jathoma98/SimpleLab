package com.org.simplelab.restcontrollers.rro;
/*
 * Request Response Object
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RRO<T> {

    enum ACTION_TYPE{
        NOTHING,
        REDIRECT,
        LOAD_DATA,
        PRINT_MSG
    }

    String success;
    String action;
    String msg;
    T data;
}
