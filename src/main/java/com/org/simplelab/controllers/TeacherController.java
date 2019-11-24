package com.org.simplelab.controllers;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.entities.Course;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(TeacherController.BASE_MAPPING)
public class TeacherController {

    public static final String BASE_MAPPING = "/teacher";

    @Autowired
    CourseDB cdb;

    @RequestMapping("")
    public String root(HttpSession session, Model model) {
        List<Course> list_course = cdb.getCoursesForTeacher((long)session.getAttribute("user_id"));
        String home_navig = ((String)session.getAttribute("username")) + "'s Home";
        model.addAttribute("home_navig", home_navig);
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

    @RequestMapping("/createlab")
    public String createLab(){
        return "createLab";
    }

    @RequestMapping("/teacherSearch")
    public String teacherSearch(){
        return "teacherSearch";
    }
}
