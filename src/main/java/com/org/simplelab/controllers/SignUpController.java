package com.org.simplelab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/signup")
public class SignUpController {
    @GetMapping("")
    public String infoPage(){
        return "signup";
    }
}
