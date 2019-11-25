package com.org.simplelab.database.validators;

public class InvalidFieldException extends Exception{
    public InvalidFieldException(String message){
        super(message);
    }
}
