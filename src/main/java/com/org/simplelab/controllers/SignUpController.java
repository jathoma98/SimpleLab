package com.org.simplelab.controllers;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
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
     * POST handler for signup data
     * @return JSON object with params
     *                     success: true   if signup was successful
     *                              false  otherwise
     *
     *         If signup fails, the object will have another field "reason":
     *                     reason: "username taken" if username is invalid
     *                             "password does not match" if password repeat doesn't match original password
     */
    @PostMapping("")
    @ResponseBody
    public Map<String, String> signupSubmit(@RequestParam("userName") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam("sp_password") String password,
                                            @RequestParam("sp_re_password") String password_repeat,
                                            @RequestParam("question") String question,
                                            @RequestParam("answer") String answer,
                                            @RequestParam("identity") String identity){

        Map<String, String> hashMap = new HashMap<>();
        if (!password.equals(password_repeat)){
            hashMap.put("success", "false");
            hashMap.put("reason", "password does not match");
            return hashMap;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setQuestion(question);
        user.setAnswer(answer);
        user.setRole(identity);

        if (userDB.insertUser(user) == UserDB.UserInsertionStatus.FAILED){
            hashMap.put("success", "false");
            hashMap.put("reason", "username taken");
            return hashMap;
        }

        hashMap.put("success", "true");
        return hashMap;

    }
}
