package com.org.simplelab.controllers;

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
@RequestMapping(path="/login")
public class LoginController{

//    @Autowired
//    UserDB userDB;

    @GetMapping("")
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
     * @return JSON hashmap for success/failure verification in login.html javascript
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam("userName") String userName,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        return new HashMap<>();
    }



}