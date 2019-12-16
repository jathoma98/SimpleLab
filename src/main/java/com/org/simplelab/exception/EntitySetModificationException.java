package com.org.simplelab.exception;

/**
 * Exception to be thrown in case of illegal modification of the entity set.
 */
public class EntitySetModificationException extends Exception{
    public EntitySetModificationException(String msg){
        super(msg);
    }
}
