package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(TeacherController.BASE_MAPPING)
public class TeacherController extends BaseController {

    public static final String DO_LAB="/dolab/{lab_id}";
    public static final String BASE_MAPPING = "/teacher";

    @RequestMapping("")
    public String root(HttpSession session, Model model) {
        long user_id = getUserIdFromSession();
        List<Course> list_course = courseDB.getCoursesForTeacher(user_id);
        String home_navig = ((String)session.getAttribute("username")) + "'s Home";
        model.addAttribute("home_navig", home_navig);
        model.addAttribute("list_of_course", list_course);
        return "teacherInfo";
    }

    @RequestMapping("/setuplab/{lab_id}")
    public String setuplab(@PathVariable("lab_id") long lab_id, HttpSession session, Model model){
        Lab lab = labDB.findById(lab_id);
        String home_navig = ((String)session.getAttribute("username")) + "'s Home";
        model.addAttribute("home_navig", home_navig);
        model.addAttribute("lab_id", lab.getId());
        model.addAttribute("lab_name", lab.getName());
        return "createLab";
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
