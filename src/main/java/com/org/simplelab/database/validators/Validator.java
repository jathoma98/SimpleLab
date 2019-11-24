package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.BaseTable;

/**
 * Validator classes are used to ensure that data that the user
 * sends to the server is properly formatted.
 * @author Jacob Thomas
 */

public abstract class Validator<T extends BaseTable> {

    public static final String EMPTY_FIELD = "Fields cannot be empty. \n";

    /**
     * Exception to be thrown, which should contain the error message
     * corresponding to how field data is incorrect.
     */

    /**
     * Method which checks each field that the user sends, to ensure they are properly formatted.
     * @throws InvalidFieldException - contains a String detailing all errors in user data.
     */
    public abstract void validate() throws InvalidFieldException;

    /**
     * Builds the object corresponding to the validator.
     * @return the object to be built
     */
    public abstract T build();
}

