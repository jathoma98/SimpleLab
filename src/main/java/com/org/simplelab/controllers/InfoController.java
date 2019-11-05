package com.org.simplelab.controllers;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/info")
public class InfoController {
    @Autowired
    UserDB userDB;

    @GetMapping(path="/get_username")
    public User get_username(String username)throws Exception{
        User user1 = userDB.findUser(username);
        return user1;
    }

}
