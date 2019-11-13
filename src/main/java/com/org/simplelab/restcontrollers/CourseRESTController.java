package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course/rest")
public class CourseRESTController {

    @Autowired
    CourseDB courseDB;

    @Autowired
    UserDB userDB;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addCourse(@RequestBody CourseValidator courseValidator,
                                         HttpSession session){
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        RequestResponse response = new RequestResponse();
        try{
            courseValidator.validate();
        }catch(Validator.InvalidFieldException e){
            response.setSuccess(false);
            response.setError(e.getMessage());
            return response.map();
        }
        User user = userDB.findUserById(userId);
        Course c = courseValidator.build();
        c.setCreator(user);
        courseDB.insertCourse(c);
        response.setSuccess(true);
        return response.map();
    }

    /**
     * Takes a JSON object with required parameter "name", which is the name of the course to delete
     * Deletes the course with this name.
     */
    @DeleteMapping("")
    public Map<String, String> deleteCourse(@RequestBody CourseValidator courseValidator,
                                            HttpSession session){
        RequestResponse response = new RequestResponse();
        String coursename = courseValidator.getName();
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        courseDB.deleteCourseByName(userId, coursename);
        response.setSuccess(true);
        return response.map();
    }

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
