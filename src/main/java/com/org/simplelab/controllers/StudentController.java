package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(StudentController.BASE_MAPPING)
public class StudentController {

    public static final String BASE_MAPPING = "/student";

    @RequestMapping("")
    public String root(HttpSession session, Model model) {
        List<Course> list_course = new LinkedList<>();
        String home_navig = ((String)session.getAttribute("username")) + "'s Home";
        model.addAttribute("home_navig", home_navig);
        model.addAttribute("list_of_course", list_course);
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
