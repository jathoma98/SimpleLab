package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(StudentController.BASE_MAPPING)
public class StudentController extends BaseController{

    public static final String DO_LAB="/dolab/{lab_id}";
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

    @RequestMapping(DO_LAB)
    public String dolab(@PathVariable("lab_id") long lab_id, HttpSession session, Model model){
        Lab lab = labDB.findById(lab_id);
        String username = (String)session.getAttribute("username");
        User user = userDB.findUser(username);
        String home_navig = ((String)session.getAttribute("username")) + "'s Home";
        model.addAttribute("home_navig", home_navig);
        model.addAttribute("user_role",user.getRole());
        model.addAttribute("lab_id", lab.getId());
        model.addAttribute("lab_name", lab.getName());
        model.addAttribute("creator_id", lab.getCreator().getId());
        return "doLab";
    }
}
