package com.org.simplelab.controllers;

import com.org.simplelab.database.validators.UserValidator;
import com.org.simplelab.restcontrollers.UserRESTController;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

//import com.sun.org.apache.regexp.internal.RE;

@Controller
@RequestMapping(SignUpController.BASE_MAPPING)
public class SignUpController extends BaseController {

    public static final String BASE_MAPPING = "/signup";

    @Autowired
    UserRESTController userRegistry;

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
    public RRO<String> submission(@RequestBody UserValidator userV,
                                  HttpSession session) {
        return userRegistry.registerUser(userV, session);
    }
}
