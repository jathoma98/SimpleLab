package com.org.simplelab.exception;

/**
 * Exception to be thrown when modification of a DB violates some constraint.
 * The message should include the contraint being violated.
 */
public class EntityDBModificationException extends Exception{
    public static String GENERIC_INVALID_UPDATE_ERROR = "Attempted to call update() on a new entity. " +
            "update() should only be called on entities which already exist in the DB.";
    public static String GENERIC_MODIFICATION_ERROR = "An error occurred while modifying this collection";
    EntityDBModificationException(){super(GENERIC_MODIFICATION_ERROR);}
    public EntityDBModificationException(String msg){
        super(msg);
    }
}
