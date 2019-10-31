package com.org.simplelab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/student")
public class StudentController {

    @RequestMapping("")
    public String root() {
        return "studentInfo";
    }

    @RequestMapping("/Course")
    public String course(){
        return "studentCourse";
    }

    @RequestMapping("/lab")
    public String lab(){
        return "doLab";
    }

    @RequestMapping("/createLab")
    public String createLab(){
        return "createLab";
    }

}
