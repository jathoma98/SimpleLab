package com.org.simplelab.restcontrollers;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.database.services.projections.Projections;
import com.org.simplelab.database.services.restservice.CourseDB;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.exception.CourseTransactionException;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.exception.EntitySetModificationException;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.security.SecurityUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

//TODO: secure rest endpoints with authentication
@RestController
@Getter
@RequestMapping(CourseRESTController.BASE_MAPPING)
public class CourseRESTController extends BaseRESTController<Course> {

    @Autowired
    private CourseDB db;

    public static final String BASE_MAPPING = "/course/rest";
    public static final String DELETE_MAPPING = "/deleteCourse";
    public static final String UPDATE_MAPPING = "/updateCourse";
    public static final String LOAD_LIST_COURSE_MAPPING = "/loadCourseList";
    public static final String LOAD_COURSE_INFO_MAPPING = "/loadCourseInfo";
    public static final String ADD_STUDENT_MAPPING = "/addStudent";
    public static final String GET_STUDENTS_MAPPING = "/getStudents";
    public static final String GET_LABS_MAPPING = "/getLabs";
    public static final String DELETE_STUDENTS_MAPPING = "/deleteStudents";
    public static final String DELETE_LABS_MAPPING = "/deleteLab";
    public static final String ADD_LABS_TO_COURSE_MAPPING = "/addLab";
    public static final String SEARCH_COURSE_MAPPING = "/searchCourse";
    public static final String CHECK_INVITE_MAPPING = "/checkInviteCode";

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RRO<String> addCourse(@RequestBody CourseValidator courseValidator) {
        return super.addEntity(courseValidator);
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
    @Transactional
    @PatchMapping(UPDATE_MAPPING)
    public RRO<String> updateCourse(@RequestBody DTO.CourseUpdateDTO dto, HttpSession session) {
        RRO<String> rro = new RRO();
        long uid = getUserIdFromSession();
        Course toUpdate;
        List<Course> foundcourses = courseDB.findByCourseId(dto.getCourse_id_old());
        if (foundcourses.size() > 0){
            toUpdate = foundcourses.get(0);
        } else {
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(RRO.MSG.COURSE_NO_FOUND.getMsg());
            rro.setSuccess(false);
            return rro;
        }
        User creator = toUpdate.getCreator();
        if (creator.getId() != uid){
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            rro.setMsg(CourseValidator.DUPLICATE_ID);
            rro.setSuccess(false);
            return rro;
        }
        return super.updateEntity(toUpdate.getId(), dto.getNewCourseInfo());
    }

    /**
     * Takes a JSON object with required parameter "course_id", which is the course id of the course to delete
     * Deletes the course with this id.
     */
    //TODO: this can probably be refactored better.
    @DeleteMapping(DELETE_MAPPING)
    public RRO<String> deleteCourse(@RequestBody CourseValidator[] toDelete,
                                            HttpSession session) {
        RRO<String> rro = new RRO();
        long userId =  getUserIdFromSession();
        for (CourseValidator c : toDelete) {
            String course_id = c.getCourse_id();
            courseDB.deleteCourseByCourseId(course_id);
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.NOTHING.  name());
        return rro;
    }

    @GetMapping(LOAD_LIST_COURSE_MAPPING)
    public RRO getListOfCourse(HttpSession session) {

        long userId = getUserIdFromSession();

        User user = userDB.findById(userId);
        List<Course> courses = null;
        if (user.getRole().equals("teacher")) {
            courses = courseDB.getCoursesForTeacher(userId);
        }
        else if (user.getRole().equals("student")){
            courses = courseDB.getCoursesStudentEnrolledIn(userId);
        }



        //we cant use SQL projections on Course because course_id has an underscore in it
        //which isnt allowed in JPA, so we have to manually copy the fields.
        RRO rro = new RRO();
        List<Projections.TeacherCourseInfo> returnInfo = new ArrayList<>();

        courses.forEach((course) -> {
            course.setCreator(null);
            course.setInvite_code(null);
            course.setDescription(null);
        });

        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(courses);
        return rro;
    }



    @PostMapping(LOAD_COURSE_INFO_MAPPING)
    public RRO<Course> getCourseInfo(@RequestBody Course course,
                                HttpSession session) {
        RRO<Course> rro = new RRO();

        long uid = getUserIdFromSession();
        Course c = courseDB.findByUserIdAndCourseId(uid, course.getCourse_id());
        if (c == null){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.PRINT_MSG.name());
            //hard code string for dev.
            rro.setMsg(RRO.MSG.COURSE_NO_FOUND.getMsg() + " " + c.getName());
            rro.setData(null);
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(c);
        return rro;
    }

    /*
     *  Add a user to to current user's course
     *  @Param course - use to get course id and student list for add.
     *  @Param session - use to check is post request user login
     *
     *  Return Map
     */

    @Transactional
    @PostMapping(ADD_STUDENT_MAPPING)
    public RRO<String> addStudentToCourse(@RequestBody DTO.CourseUpdateStudentListDTO course) {
        String own_username = SecurityUtils.getAuthenticatedUsername();
        List<User> toAdd = new ArrayList<>();
        String course_id = course.getCourse_id();
        List<Course> c = courseDB.findByCourseId(course_id);
        if (own_username.equals(c.get(0).getCreator().getUsername())){
            List<String> usernameList = course.getUsernameList();
            for (String username: usernameList){
                if (!own_username.equals(username)){
                    toAdd.add(userDB.findUser(username));
                }
            }
            SQLService.EntitySetManager<User, Course> toUpdate = courseDB.getStudentsOfCourseByCourseId(course_id);
            return super.addEntitiesToEntityList(toUpdate, toAdd);
        }else if(c.get(0).getInvite_code() == null || course.getInvite_code().equals(c.get(0).getInvite_code())){
            toAdd.add(userDB.findUser((String)session.getAttribute("username")));
            SQLService.EntitySetManager<User, Course> toUpdate = courseDB.getStudentsOfCourseByCourseId(course_id);
            return super.addEntitiesToEntityList(toUpdate, toAdd);
        }

        return RRO.sendErrorMessage("Invite Code not match");
    }

    @Transactional
    @PostMapping(ADD_LABS_TO_COURSE_MAPPING)
    public RRO<String> addLabsToCourse(@RequestBody DTO.CourseAddLabsDTO dto){
        long[] ids = dto.getLab_ids();
        List<Lab> toAdd = new ArrayList<>();
        for (long id: ids){
            Lab found = labDB.findById(id);
            if (found != null)
                toAdd.add(found);
        }
        SQLService.EntitySetManager<Lab, Course> toUpdate;
        try {
            toUpdate = courseDB.getLabsOfCourseByCourseId(dto.getCourse_id());
        } catch (CourseTransactionException e){
            RRO<String> rro = new RRO();
            rro.setMsg(e.getMessage());
            return rro;
        }
        return super.addEntitiesToEntityList(toUpdate, toAdd);
    }

    @Transactional
    @PostMapping(DELETE_LABS_MAPPING)
    public RRO<String> deleteLabList(@RequestBody DTO.CourseUpdateLabListDTO course) throws CourseTransactionException {
        long[] labidList = course.getLab_ids();
        List<Lab> toDelete = new ArrayList<>();
        for (Long labid: labidList){
            Lab u = labDB.findById(labid);
            if (u != null){
                toDelete.add(u);
            }
        }
        SQLService.EntitySetManager<Lab, Course> toUpdate;
        try {
            toUpdate = courseDB.getLabsOfCourseByCourseId(course.getCourse_id());
        } catch (CourseTransactionException e){
            RRO<String> rro = new RRO();
            rro.setMsg(e.getMessage());
            return rro;
        }
//        SQLService.EntitySetManager<Lab, Course> toUpdate = courseDB.getLabsOfCourseByCourseId(course.getCourse_id());
        return super.removeEntitiesFromEntityList(toUpdate, toDelete);
    }

    @Transactional
    @PostMapping(GET_STUDENTS_MAPPING)
    public RRO<List<User>> getStudentList(@RequestBody DTO.CourseUpdateStudentListDTO course) {
        String course_id = course.getCourse_id();
        List<User> students;
        RRO<List<User>> rro = new RRO();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        try {
            students = courseDB.getStudentsOfCourse(course_id);
            rro.setData(students);
        } catch (CourseTransactionException e) {
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        }
        return rro;
    }

    @Transactional
    @PostMapping(GET_LABS_MAPPING)
    public  RRO<List<Lab>> getLabList(@RequestBody DTO.LoadLabListDTO course) throws CourseTransactionException {
        String courseid=course.getCourse_id();
        List<Lab> labs=courseDB.getLabsOfCourseByCourseId(courseid).getAsList();
        RRO<List<Lab>> rro = new RRO();
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(labs);
        return rro;
    }


    @Transactional
    @DeleteMapping(DELETE_STUDENTS_MAPPING)
    public RRO<String> deleteStudentList(@RequestBody DTO.CourseUpdateStudentListDTO course) {
        List<String> usernameList = course.getUsernameList();
        SQLService.EntitySetManager<User, Course> toUpdate = courseDB.getStudentsOfCourseByCourseId(course.getCourse_id());
        for (String username: usernameList){
            User u = userDB.findUser(username);
            if (u != null){
                try {
                    toUpdate.delete(u);
                } catch (EntitySetModificationException e){
                    return RRO.sendErrorMessage(e.getMessage());
                }
            }
        }
        try {
            courseDB.update(toUpdate.getFullEntity());
        } catch (EntityDBModificationException e){
            return RRO.sendErrorMessage(e.getMessage());
        }
        RRO rro = new RRO();
        rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
        rro.setSuccess(true);
        return rro;
        //SQLService.EntitySetManager<User, Course> toUpdate = courseDB.getStudentsOfCourseByCourseId(course.getCourse_id());
        //return super.removeEntitiesFromEntityList(toUpdate, toDelete);
    }



    @PostMapping(SEARCH_COURSE_MAPPING)
    public RRO<List<Course>> searchCourse(@RequestBody DTO.UserSearchDTO toSearch){
        RRO<List<Course>> rro = new RRO();
        String courseRegex = toSearch.getRegex();
        //dont allow empty searches
        if (courseRegex == null || courseRegex.equals("")){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(courseDB.searchCourseWithKeyword(courseRegex));
        return rro;
    }

    @PostMapping(CHECK_INVITE_MAPPING)
    public RRO<Course> checkCourse(@RequestBody DTO.CourseSearchDTO toMatch){
        RRO<Course> rro = new RRO();
        String matchRegex = toMatch.getRegex();
        //dont allow empty searches
        if (matchRegex == null || matchRegex.equals("")){
            rro.setSuccess(false);
            rro.setAction(RRO.ACTION_TYPE.NOTHING.name());
            return rro;
        }
        rro.setSuccess(true);
        rro.setAction(RRO.ACTION_TYPE.LOAD_DATA.name());
        rro.setData(courseDB.findInviteCodeByName(matchRegex));
        return rro;
    }
}
