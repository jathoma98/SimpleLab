package com.org.simplelab.utils;

public class RROResponse {
    public static final String SUCCESS = initSuccess();

    private static String initSuccess(){
        JSONBuilder js = new JSONBuilder();
        js.put("success", "true");
        return js.toString();
    }
}
