package com.org.simplelab.database;

import com.org.simplelab.SimpleLabApplication;

import java.security.MessageDigest;

/**
 * Management class for MongoDB entities
 * @author Jacob Thomas
 */
public class DBManager {

    public static final String USER_DOCUMENT_NAME = "user";

    /**
     * Hashes the given string.
     */

    public static byte[] getHash(String entry){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(entry.getBytes());
            return md.digest();
        } catch (Exception e){
            System.out.println("Exception occurred while hashing password.");
            return null;
        }
    }

}
