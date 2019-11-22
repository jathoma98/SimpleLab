package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.database.validators.Validator;
import com.org.simplelab.restcontrollers.dto.DTO;
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
    public static final String ADD_STUDENT_MAPPING = "/addStudent";
    public static final String GET_STUDENTS_MAPPING = "/getStudents";
    public static final String REMOVE_STUDENTS_MAPPING = "/removeStudents";


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
     *         InvalidFieldException message if user data is formatted improperly
     *         Duplicate coursecode error message if the new course code is already taken
     *
     */
    @PatchMapping(UPDATE_MAPPING)
    public Map<String, String> updateCourse(@RequestBody DTO.CourseUpdateDTO dto, HttpSession session){
        RequestResponse rsp = new RequestResponse();
        String uid = (String)session.getAttribute("user_id");
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
            if (!found.getCreator().get_id().equals(uid)){
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
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            response.setError(e.toString());
            return response.map();
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
        String userId = "";
        try{
            userId = (String)session.getAttribute("user_id");
        } catch (Exception e){
            //redirect to login
        }
        List<Course> courses = courseDB.getCoursesForTeacher(userId);
        return courses;
    }

    @PostMapping(LOAD_COURSE_INFO_MAPPING)
    public Course getCourseInfo( @RequestBody Course course,
                                        HttpSession session){
        String uid = (String)session.getAttribute("user_id");
        Course r = courseDB.findByUserIdAndCourseId(uid, course.getCourse_id());
        return r;
    }

    /*
     *  Add a user to to current user's course
     *  @Param course - use to get course id
     *  @Param user - use to get username
     *  @Param session - use to check is post request user login
     *
     *  Return Map
     */
    @PostMapping(ADD_STUDENT_MAPPING)
    public Map addStudentToCourse (@RequestBody Course course,
                                    HttpSession session){
        RequestResponse r = new RequestResponse();
        r.setSuccess(false);

        String user_id = (String)session.getAttribute("user_id");
        if ( user_id == null){
            r.setError("Not Login");
            return r.map();
        }

        Course target_course = courseDB.findByUserIdAndCourseId(user_id, course.getCourse_id());
        if (target_course == null) {
            r.setError("Course Not Found");
            return r.map();
        }

        course.getUsers().forEach((user)->{
            User u = userDB.findUser(user.getUsername());
            if (u == null) return;
            for(int i = 0; i < target_course.getUsers().size(); i++){
                if(target_course.getUsers().get(i).getUsername().equals(u.getUsername())){
                    return;
                }
            }
            target_course.getUsers().add(u);
        });


        if (target_course.getUsers().size() <= 0){
            r.setError("Users Not Found");
            return r.map();
        }
        courseDB.updateCourse(target_course);
        r.setSuccess(true);
        return r.map();
    }

    @PostMapping(GET_STUDENTS_MAPPING)
    public List<User> getStudentList (@RequestBody Course course,
                                   HttpSession session){
        List<Course> c = courseDB.findByCourseId(course.getCourse_id());
        return c.get(0).getUsers();
    }

    @PostMapping(REMOVE_STUDENTS_MAPPING)
    public List<User> removeStudentList (@RequestBody Course course,
                                      HttpSession session){
        List<Course> c = courseDB.findByCourseId(course.getCourse_id());
        String user_id = (String)session.getAttribute("user_id");
        if ( user_id == null){
            return null;
        }
        Course target_course = courseDB.findByUserIdAndCourseId((String)session.getAttribute("user_id"), course.getCourse_id());
        int len = course.getUsers().size();
        for(int i = 0; i < len; i++){
            User u = course.getUsers().get(i);
            for(int j = 0; j <  target_course.getUsers().size(); j++){
                User tu = target_course.getUsers().get(j);
                if (tu.getUsername().equals(u.getUsername())){
                    target_course.getUsers().remove(tu);
                    break;
                }
            }
        }
        return c.get(0).getUsers();
    }

}
