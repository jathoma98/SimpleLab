package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course/rest")
public class CourseRESTController {

    @Autowired
    CourseDB courseDB;

    @GetMapping("/loadInfo")
    public List<Course> getCourses(HttpSession session){
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        List<Course> courses = courseDB.getCoursesForTeacher(userId);
        return courses;
    }

}
