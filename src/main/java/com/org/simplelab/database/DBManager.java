package com.org.simplelab.database;

import java.security.MessageDigest;

/**
 * Management class for MongoDB entities
 * @author Jacob Thomas
 */
public class DBManager {

    public static final String USER_DOCUMENT_NAME = "user";
    public static final String COURSE_DOCUMENT_NAME = "course";
    public static final String LAB_DOCUMENT_NAME = "lab";
    public static final String EQUIPMENT_DOCUMENT_NAME = "equipment";

    public static final String SALT = "a very salty salt";

    /**
     * Hashes the given string.
     */
    public static byte[] getHash(String entry){
        try {
            StringBuilder sb = new StringBuilder();
            entry = sb.append(entry).append(SALT).toString();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(entry.getBytes());
            return md.digest();
        } catch (Exception e){
            System.out.println("Exception occurred while hashing password.");
            return null;
        }
    }

}
