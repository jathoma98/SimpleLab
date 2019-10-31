package com.org.simplelab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/teacher")
public class TeacherController {
    @RequestMapping("")
    public String root() {
        return "teacherInfo";
    }

    @RequestMapping("/teacherCourse")
    public String course(){
        return "teacherCourse";
    }

    @RequestMapping("/equipment")
    public String equipment(){
        return "teacherEquipment";
    }

    @RequestMapping("/lab")
    public String lab(){
        return "teacherLab";
    }

    @RequestMapping("/createCourse")
    public String createCourse(){
        return "AddAndEditCourse";
    }

    @RequestMapping("/createEquipment")
    public String createEquipment(){
        return "createEquipment";
    }

    @RequestMapping("/createLab")
    public String createLab(){
        return "createLab";
    }
}
