package com.org.simplelab.database.validators;

/**
 * Validator classes are used to ensure that data that the user
 * sends to the server is properly formatted.
 * @author Jacob Thomas
 */
public abstract class Validator {

    /**
     * Exception to be thrown, which should contain the error message
     * corresponding to how field data is incorrect.
     */
    public class InvalidFieldException extends Exception{
        public InvalidFieldException(String message){
            super(message);
        }
    }


    /**
     * Method which checks each field that the user sends, to ensure they are properly formatted.
     * @throws InvalidFieldException - contains a String detailing all errors in user data.
     */
    public abstract void validate() throws InvalidFieldException;

    /**
     * Builds the object corresponding to the validator.
     * @return the object to be built
     */
    public abstract Object build();
}
