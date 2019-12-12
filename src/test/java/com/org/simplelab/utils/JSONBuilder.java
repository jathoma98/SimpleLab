package com.org.simplelab.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JSONBuilder {

    private static ObjectMapper o = init();
    private String jsonString;
    private Map<String, Object> map;

    private static ObjectMapper init(){
        return new ObjectMapper();
    }

    public JSONBuilder(){
        this.o = new ObjectMapper();
        this.map = new HashMap<>();
    }

    public JSONBuilder put(String key, Object value){
        map.put(key, value);
        return this;
    }

    public static String asJson(Object obj){
        try {
            return o.writeValueAsString(obj);
        } catch (Exception e) { System.out.println("Exception while processing JSON: " + e.getMessage() + " " + e.getStackTrace().toString());}
        return null;
    }

    @Override
    public String toString(){
        try {
            return o.writeValueAsString(map);
        } catch (Exception e) { System.out.println("Exception while processing JSON: " + e.getMessage() + " " + e.getStackTrace().toString());}
        return null;
    }

}
