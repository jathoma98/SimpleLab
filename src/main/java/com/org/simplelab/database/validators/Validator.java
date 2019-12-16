package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.exception.InvalidFieldException;
import com.org.simplelab.restcontrollers.dto.DTO;

/**
 * Validator classes are used to ensure that data that the user
 * sends to the server is properly formatted.
 * @author Jacob Thomas
 */

public abstract class Validator<T extends BaseTable> extends DTO {

    public static final String EMPTY_FIELD = "Fields cannot be empty. \n";

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

