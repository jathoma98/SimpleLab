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

    public static final String DELETE_MAPPING = "/deleteCourse";
    public static final String LOAD_INFO_MAPPING = "/loadInfo";

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
        if (courseDB.insertCourse(c)) {
            response.setSuccess(true);
            return response.map();
        } else { //return error on duplicate ID
            response.setSuccess(false);
            response.setError(CourseValidator.DUPLICATE_ID);
            return response.map();
        }
    }

    /**
     * Takes a JSON object with required parameter "course_id", which is the course id of the course to delete
     * Deletes the course with this id.
     */
    @DeleteMapping(DELETE_MAPPING)
    public Map<String, String> deleteCourse(@RequestBody CourseValidator[] toDelete,
                                            HttpSession session){
        RequestResponse response = new RequestResponse();
        //String course_id = courseValidator.getCourse_id();
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        for (CourseValidator c: toDelete){
            String course_id = c.getCourse_id();
            courseDB.deleteCourseById(userId, course_id);
        }
        response.setSuccess(true);
        return response.map();
    }

    @GetMapping(LOAD_INFO_MAPPING)
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
