package com.org.simplelab.controllers;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.InvalidFieldException;
import com.org.simplelab.database.validators.UserValidator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//import com.sun.org.apache.regexp.internal.RE;

@Controller
@RequestMapping(SignUpController.BASE_MAPPING)
public class SignUpController extends BaseController {

    public static final String BASE_MAPPING = "/signup";

    @GetMapping("")
    public String infoPage(){
        return "signup";
    }

    /**
     * POST handler for signup user data
     * @return JSON object with params
     *                     success: true   if signup was successful
     *                              false  otherwise
     *
     *         If signup fails, the object will have another field "reason":
     *                     reason: "username taken" if username is invalid
     *                             "password does not match" if password repeat doesn't match original password
     */
    @ResponseBody
    @PostMapping(path="/submit", consumes= MediaType.APPLICATION_JSON_VALUE)
    public  Map<String, String> submission(@RequestBody UserValidator userV) {

        RequestResponse response = new RequestResponse();
        try{
            userV.validate();
        }catch(InvalidFieldException e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return response.map();
        }
        User user = userV.build();
        try {
            userDB.insert(user);
        } catch (UserDB.UserInsertionException e){
            response.setError(e.getMessage());
            response.setSuccess(false);
            return response.map();
        }

        response.setSuccess(true);
        return response.map();


    }
}
