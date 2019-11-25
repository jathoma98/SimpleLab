package com.org.simplelab.restcontrollers;

import com.org.simplelab.controllers.RequestResponse;
import com.org.simplelab.database.services.CourseDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: secure rest endpoints with authentication
@RestController
@RequestMapping(CourseRESTController.BASE_MAPPING)
public class CourseRESTController extends BaseRESTController<Course> {

    public static final String BASE_MAPPING = "/course/rest";

    public static final String DELETE_MAPPING = "/deleteCourse";
    public static final String UPDATE_MAPPING = "/updateCourse";
    public static final String LOAD_LIST_COURSE_MAPPING = "/loadCourseList";
    public static final String LOAD_COURSE_INFO_MAPPING = "/loadCourseInfo";
    public static final String ADD_STUDENT_MAPPING = "/addStudent";
    public static final String GET_STUDENTS_MAPPING = "/getStudents";
    public static final String DELETE_STUDENTS_MAPPING = "/deleteStudents";


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> addCourse(@RequestBody CourseValidator courseValidator,
                                         HttpSession session) {
        return super.addEntity(courseValidator, courseDB);
    }

    /**
     * Endpoint for updating an existing course.
     *
     * @param dto - Should be formatted as:
     *            {
     *            course_id_old: The old Course ID before update, to find in DB.
     *            ` newCourseInfo: A Course object which has the new course info
     *            }
     *            example:
     *            {
     *            course_id_old: "CSE308",
     *            newCourseInfo: {
     *               name: "Software Dev",
     *               course_id: "CSE316",
     *                description: "Revamped Software class for Spring 2020"
     *            }
     *            }
     * @return success:true on success
     * success:false on failure with possible errors:
     * InvalidFieldException message if user data is formatted improperly
     * Duplicate coursecode error message if the new course code is already taken
     */
    @PatchMapping(UPDATE_MAPPING)
    public Map<String, String> updateCourse(@RequestBody DTO.CourseUpdateDTO dto, HttpSession session) {
        System.out.println(dto.toString());
        RequestResponse rsp = new RequestResponse();
        long uid = getUserIdFromSession(session);
        Course toUpdate;
        List<Course> foundcourses = courseDB.findByCourseId(dto.getCourse_id_old());
        if (foundcourses.size() > 0){
            toUpdate = foundcourses.get(0);
        } else {
            rsp.setError("No course found.");
            return rsp.map();
        }
        if (toUpdate.getCreator().getId() != uid){
            rsp.setError(CourseValidator.DUPLICATE_ID);
            return rsp.map();
        }
        return super.updateEntity(toUpdate.getId(), dto.getNewCourseInfo(), courseDB);
    }

    /**
     * Takes a JSON object with required parameter "course_id", which is the course id of the course to delete
     * Deletes the course with this id.
     */
    //TODO: this can probably be refactored better.
    @DeleteMapping(DELETE_MAPPING)
    public Map<String, String> deleteCourse(@RequestBody CourseValidator[] toDelete,
                                            HttpSession session) {
        RequestResponse response = new RequestResponse();
        long userId =  getUserIdFromSession(session);
        for (CourseValidator c : toDelete) {
            String course_id = c.getCourse_id();
            courseDB.deleteCourseById(userId, course_id);
        }
        response.setSuccess(true);
        return response.map();
    }

    @GetMapping(LOAD_LIST_COURSE_MAPPING)
    public List<Course> getListOfCourse(HttpSession session) {
        long userId = getUserIdFromSession(session);
        List<Course> courses = courseDB.getCoursesForTeacher(userId);
        return courses;
    }

    @PostMapping(LOAD_COURSE_INFO_MAPPING)
    public Course getCourseInfo(@RequestBody Course course,
                                HttpSession session) {
        long uid = getUserIdFromSession(session);
        Course r = courseDB.findByUserIdAndCourseId(uid, course.getCourse_id());
        return r;
    }

    /*
     *  Add a user to to current user's course
     *  @Param course - use to get course id and student list for add.
     *  @Param session - use to check is post request user login
     *
     *  Return Map
     */

    @PostMapping(ADD_STUDENT_MAPPING)
    public Map addStudentToCourse(@RequestBody DTO.CourseUpdateStudentListDTO  course,
                                  HttpSession session) {
        RequestResponse r = new RequestResponse();
        r.setSuccess(false);

        long own_id = -1;
        own_id = getUserIdFromSession(session);
        String own_username = (String) session.getAttribute("username");

        String errorMsg = "";
        String course_id = course.getCourse_id();
        List<String> usernameList = course.getUsernameList();
        for(int i = 0; i < usernameList.size(); i++ ){
            if(own_username.equals(usernameList.get(i))) continue;
            User u = userDB.findUser(usernameList.get(i));
            try {
                courseDB.addStudentToCourse(course_id, u);
            } catch (CourseDB.CourseTransactionException e) {
                errorMsg += e.getMessage() + "\n";
            }
        }
        r.setError(errorMsg);
        r.setSuccess(true);
        return r.map();
    }


    @PostMapping(GET_STUDENTS_MAPPING)
    public List<User> getStudentList(@RequestBody DTO.CourseUpdateStudentListDTO course,
                                     HttpSession session) {
        String course_id = course.getCourse_id();
        List<User> students;
        try {
            students = courseDB.getStudentsOfCourse(course_id);
        } catch (CourseDB.CourseTransactionException e) {
            return new ArrayList<>();
        }
        return students;
    }


    @DeleteMapping(DELETE_STUDENTS_MAPPING)
    public Map deleteStudentList(@RequestBody DTO.CourseUpdateStudentListDTO course,
                                        HttpSession session) {
        RequestResponse r = new RequestResponse();
        r.setSuccess(false);

        long own_id = -1;
        own_id = getUserIdFromSession(session);
        String errorMsg = "";
        List<String> usernameList = course.getUsernameList();
        for(int i = 0; i < usernameList.size(); i++ ){
            User u = userDB.findUser(usernameList.get(i));
            try {
                courseDB.removeStudentFromCourse(u, course.getCourse_id());
            } catch (CourseDB.CourseTransactionException e) {
                errorMsg += e.getMessage() + "\n";
            }
        }
        r.setError(errorMsg);
        r.setSuccess(true);
        return r.map();
    }

}
