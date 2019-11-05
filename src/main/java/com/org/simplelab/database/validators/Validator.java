package com.org.simplelab.database.validators;

public abstract class Validator {

    public class InvalidFieldException extends Exception{
        public InvalidFieldException(String message){
            super(message);
        }
    }


    protected boolean validated = false;

    public abstract void validate() throws InvalidFieldException;

    public abstract Object build();
}
