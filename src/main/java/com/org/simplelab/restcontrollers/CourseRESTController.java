package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.dto.CourseUpdateDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
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
    public static final String UPDATE_MAPPING = "/updateCourse";
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
     *  Endpoint for updating an existing course.
     * @param dto - Should be formatted as:
     *            {
     *              course_id_old: The old Course ID before update, to find in DB.
     *            ` newCourseInfo: A Course object which has the new course info
     *            }
     *            example:
     *            {
     *              course_id_old: "CSE308",
     *              newCourseInfo: {
     *                  name: "Software Dev",
     *                  course_id: "CSE316",
     *                  description: "Revamped Software class for Spring 2020"
     *              }
     *            }
     * @return success:true on success
     *         success:false on failure with possible errors:
     *
     */
    @PatchMapping(UPDATE_MAPPING)
    public Map<String, String> updateCourse(@RequestBody CourseUpdateDTO dto, HttpSession session){
        RequestResponse rsp = new RequestResponse();
        System.out.println(dto.getCourse_id_old());
        System.out.println(dto.getNewCourseInfo());
        List<Course> courses = courseDB.findByCourseId(dto.getCourse_id_old());
        if (courses.size() > 0){
            CourseValidator cv = dto.getNewCourseInfo();
            try{
                cv.validate();
            } catch (Validator.InvalidFieldException e){
                rsp.setError(e.getMessage());
                return rsp.map();
            }
            //TODO: refactor with modelmapper?
            Course found = courses.get(0);
            found.setCourse_id(cv.getCourse_id());
            found.setName(cv.getName());
            found.setDescription(cv.getDescription());
            if (!courseDB.updateCourse(found)){
                rsp.setError(CourseValidator.DUPLICATE_ID);
                return rsp.map();
            }
        }

        rsp.setSuccess(true);
        return rsp.map();
    }

    /**
     * Takes a JSON object with required parameter "course_id", which is the course id of the course to delete
     * Deletes the course with this id.
     */
    @DeleteMapping(DELETE_MAPPING)
    public Map<String, String> deleteCourse(@RequestBody CourseValidator[] toDelete,
                                            HttpSession session){
        RequestResponse response = new RequestResponse();
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
