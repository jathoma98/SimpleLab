package com.org.simplelab.database;

import com.org.simplelab.database.entities.sql.BaseTable;
import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.security.MessageDigest;

public class DBUtils {

    //SQL table and entity names
    public static final String USER_TABLE_NAME = "user";
    public static final String COURSE_TABLE_NAME = "course";
    public static final String LAB_TABLE_NAME = "lab";
    public static final String EQUIPMENT_TABLE_NAME = "equipment";
    public static final String EQUIPMENT_PROPERTY_TABLE_NAME = "equipment_property";
    public static final String STEP_TABLE_NAME = "step";
    public static final String RECIPE_TABLE_NAME = "recipe";
    public static final String IMAGE_FILE_TABLE_NAME = "image_file";

    //MongoDB document names
    public static final String LABINSTANCE_DOCUMENT_NAME = "lab_instance";

    //salt for hashing
    public static final String SALT = "a very salty salt";

    private static ModelMapper MAPPER = null;

    public static ModelMapper getMapper(){
        if (MAPPER == null){
            ModelMapper mm = new ModelMapper();
            mm.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            MAPPER = mm;
        }
        return MAPPER;
    }

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

    /**
     * We need to define custom Serialize and Deserialize wrapper methods because
     * serialized objects have isNew = true by default, which is incorrect if they
     * are being pulled from DB or being saved to DB.
     */

    public static  byte[] serialize(Serializable o){
        //o.setNew(false);
        return SerializationUtils.serialize(o);
    }

    public static Serializable deserialize(byte[] serial){
        Serializable built = SerializationUtils.deserialize(serial);
        //built.setNew(false);
        return built;
    }


}
