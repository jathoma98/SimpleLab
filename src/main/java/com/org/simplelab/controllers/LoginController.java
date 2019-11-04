package com.org.simplelab.controllers;

import com.org.simplelab.database.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Controllers for login and signup pages
 */

@Controller
public class LoginController{

    @Autowired
    UserDB userDB;

    @GetMapping("/login")
    public String loginGet(){
        return "login";
    }

    /**
     * Processes user data submission in /login and adds
     * user data to current session.
     *
     * @param userName -username from form submission in login.html
     * @param password -password from form submission in login.html
     * @param session -current user session
     * @return JSON response with format:
     *          { success: "true" } if authentication is successful
     *          { success: "false" } otherwise
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam("userName") String userName,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (userDB.authenticate(userName, password) == UserDB.UserAuthenticationStatus.SUCCESSFUL) {
            response.put("success", "true");
            return response;
        }
        else{
            response.put("success", "false");
            return response;
        }
    }



}