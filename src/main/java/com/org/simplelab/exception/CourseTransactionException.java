package com.org.simplelab.exception;

public class CourseTransactionException extends EntityDBModificationException {
    public static final String NO_COURSE_FOUND = "The requested course could not be found.";
    public CourseTransactionException(String message){
        super(message);
    }
}
