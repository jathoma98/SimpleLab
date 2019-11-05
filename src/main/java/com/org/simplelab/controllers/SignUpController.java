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

import java.util.HashMap;
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
    @PostMapping(value = "/userdata", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> signupSubmit(@RequestBody UserValidator userValidator){

        //TODO: In signup.html, check if fields are empty
        //TODO: make the error message in signup.html look better.
        RequestResponse rq = new RequestResponse();

        try{
            userValidator.validate();
        } catch (Validator.InvalidFieldException e){
            rq.setError(e.getMessage());
            return rq.map();
        }

        User user = userValidator.build();

        if (userDB.insertUser(user) == UserDB.UserInsertionStatus.FAILED){
            rq.setError("username taken");
            return rq.map();
        }
        rq.setSuccess(true);
        return rq.map();

    }
}
