package com.org.simplelab.controllers;

import com.org.simplelab.database.Entities.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(path="/teacher")
public class TeacherController {
    @RequestMapping("")
    public String root(HttpSession sc, Model model) {
        List<Course> list_course = new LinkedList<>();
        // set dummy data
        for(int i = 0; i< 10; i++){
            list_course.add(new Course(Integer.toString(i), "course_"+ i, i + "-" + i + "-" + "2019"));
        }
        model.addAttribute("list_of_course", list_course);
        return "teacherInfo";
    }

    @RequestMapping("/course")
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
        return "addAndEditEquipment";
    }

    @RequestMapping("/createLab")
    public String createLab(){
        return "createLab";
    }

    @RequestMapping("/teacherSearch")
    public String teacherSearch(){
        return "teacherSearch";
    }
}
