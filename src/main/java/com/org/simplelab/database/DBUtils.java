package com.org.simplelab.database;

import org.modelmapper.ModelMapper;

import java.security.MessageDigest;

/**
 * Management class for MongoDB entities
 * @author Jacob Thomas
 */
public class DBUtils {

    public static final String USER_TABLE_NAME = "user";
    public static final String COURSE_TABLE_NAME = "course";
    public static final String LAB_TABLE_NAME = "lab";
    public static final String EQUIPMENT_TABLE_NAME = "equipment";

    public static final String SALT = "a very salty salt";

    public static final String METADATA_DELETE_QUERY = "DELETE FROM #{#entityName} WHERE _metadata = :metadata";

    public static final ModelMapper MAPPER = new ModelMapper();

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
