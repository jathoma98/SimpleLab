package com.org.simplelab.controllers;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
//import com.sun.org.apache.regexp.internal.RE;
import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.database.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path="/signup")
public class SignUpController {

    @Autowired
    UserDB userDB;

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

        //TODO: In signup.html, check if fields are empty
        //TODO: make the error message in signup.html look better.
        RequestResponse response = new RequestResponse();
        try{
            userV.validate();
        }catch(Validator.InvalidFieldException e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return response.map();
        }
        User user = userV.build();


        if (userDB.insertUser(user) == UserDB.UserInsertionStatus.FAILED){
            response.setError("username taken");
            response.setSuccess(false);
            return response.map();
        }

        response.setSuccess(true);
        return response.map();


    }
}
