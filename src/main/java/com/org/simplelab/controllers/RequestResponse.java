package com.org.simplelab.controllers;

import java.util.HashMap;
import java.util.Map;


public class RequestResponse {
    Map<String, String> responseMap;
    public RequestResponse(){
        responseMap = new HashMap<String, String>();
        responseMap.put("success", "false");
        responseMap.put("error", "");

    }
    /**
     * Set responseMap success value
     *
     * @param boolStr -success status boolean as string
     * @return
     */
    public void setSuccess(String boolStr){
        responseMap.put("success", boolStr);
    }

    /**
     * Set responseMap success value
     *
     * @param bool -success status boolean
     * @return
     */
    public void setSuccess(boolean bool){
        responseMap.put("success", bool == true ? "true" : "false");
    }


    /**
     * Set responseMap message
     *
     * @param message -success status boolean
     * @return
     */
    public void setMessage(String message){
        responseMap.put("message", message);
    }

    /**
     * Set responseMap error message
     *
     * @param message -success status boolean
     * @return
     */
    public void setError(String message){
        responseMap.put("error", message);
    }

    /**
     * Set response redirect url
     *
     * @param url -success status boolean
     * @return
     */
    public void setRedirect(String url){
        responseMap.put("redirect", url);
    }

    /**
     * Get responseMap
     *
     * @param
     * @return
     */
    public Map<String, String> map(){
        return this.responseMap;
    }
}
