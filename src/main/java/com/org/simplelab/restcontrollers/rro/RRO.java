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
}



