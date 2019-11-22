package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

//TODO: secure rest endpoints with authentication
@RestController
@RequestMapping("/course/rest")
    public class CourseRESTController {

    @Autowired
    CourseDB courseDB;

    @Autowired
    UserDB userDB;

    public static final String DELETE_MAPPING = "/deleteCourse";
    public static final String UPDATE_MAPPING = "/updateCourse";
    public static final String LOAD_LIST_COURSE_MAPPING = "/loadCourseList";
    public static final String LOAD_COURSE_INFO_MAPPING = "/loadCourseInfo";


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addCourse(@RequestBody CourseValidator courseValidator,
                                         HttpSession session){
        long userId = -1;
        try{
            userId = (long)session.getAttribute("user_id");
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
     *         InvalidFieldException message if user data is formatted improperly
     *         Duplicate coursecode error message if the new course code is already taken
     *
     */
    @PatchMapping(UPDATE_MAPPING)
    public Map<String, String> updateCourse(@RequestBody DTO.CourseUpdateDTO dto, HttpSession session){
        RequestResponse rsp = new RequestResponse();
        long uid = (long)session.getAttribute("user_id");
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
            //ensure the found course belongs to the current user -- exception if not (the new course code is a duplicate)
            if (found.getCreator().getId() != uid){
                rsp.setError(CourseValidator.DUPLICATE_ID);
                return rsp.map();
            }
            found.setCourse_id(cv.getCourse_id());
            found.setName(cv.getName());
            found.setDescription(cv.getDescription());
            courseDB.updateCourse(found);
        } else {
            rsp.setError("No course with this ID was found. ");
            return rsp.map();
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
        long userId = -1;
        try{
            userId = (long)session.getAttribute("user_id");
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

    @GetMapping(LOAD_LIST_COURSE_MAPPING)
    public List<Course> getListOfCourse(HttpSession session){
        long userId = -1;
        try{
            userId = (long)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        List<Course> courses = courseDB.getCoursesForTeacher(userId);
        return courses;
    }

    @PostMapping(LOAD_COURSE_INFO_MAPPING)
    public Course getCourseInfo( @RequestBody Course course,
                                        HttpSession session){
        long uid = (long)session.getAttribute("user_id");
        Course r = courseDB.findByUserIdAndCourseId(uid, course.getCourse_id());
        return r;
    }





}
