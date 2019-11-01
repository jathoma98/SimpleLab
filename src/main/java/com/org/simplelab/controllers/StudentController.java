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

    @RequestMapping("/course")
    public String course(){
        return "studentCourse";
    }

    @RequestMapping("/course/lab")
    public String lab(){
        return "doLab";
    }


    @RequestMapping("/search")
    public String search(){
        return "search";
    }
}
