package com.org.simplelab.database.services.restservice;


import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.repositories.sql.CourseRepository;
import com.org.simplelab.database.services.SQLService;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.exception.CourseTransactionException;
import com.org.simplelab.exception.EntityDBModificationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Component
@Getter
public class CourseDB extends SQLService<Course> {

    @Autowired
    private CourseRepository repository;

    private class StudentSetManager extends EntitySetManager<User, Course>{
        public StudentSetManager(Set<User> set, Course c) { super(set, c);}
    }


    //Course has additional insertion condition -- needs unique CourseId
    @Override
    protected void checkInsertionCondition(Course c) throws EntityDBModificationException{
        super.checkInsertionCondition(c);
        List<Course> found = findByCourseId(c.getCourse_id());
        if (found != null && found.size() > 0)
            throw new EntityDBModificationException(CourseValidator.DUPLICATE_ID);
    }


    public boolean deleteById(long id) {
        return super.deleteById(id);
    }

    public boolean update(Course toUpdate) throws EntityDBModificationException {
        return super.update(toUpdate);
    }

    public Course findById(long id) {
        return super.findById(id);
    }

    public StudentSetManager getStudentsOfCourseByCourseId(String course_id){
        List<Course> found = findByCourseId(course_id);
        if (found == null || found.size() == 0)
            return null;
        Course c = found.get(0);
        return new StudentSetManager(c.getStudents(), c);
    }

    /**
     * Returns courses that the student is enrolled in.
     */
    public List<Course> getCoursesStudentEnrolledIn(long student_id){
        return repository.findCoursesEnrolledIn(student_id);
    }

    /**
     * Returns list of student usernames in a course.
     * @param course_id - course_id of the course to be found
     * @return - List of Users corresponding to the course -- empty list if no users exist
     * @throws CourseTransactionException - when Course with given course_id does not exist
     */
    public List<User> getStudentsOfCourse(String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        ArrayList<User> students = new ArrayList<>();
        //project only usernames
        c.getStudents().forEach((student) -> {
            User proj = new User();
            proj.setUsername(student.getUsername());
            proj.setFirstname(student.getFirstname());
            proj.setLastname(student.getLastname());
            proj.setEmail(student.getEmail());
            proj.setInstitution(student.getInstitution());
            students.add(proj);
        });
        return students;
    }

    public Set<Lab> getLabsOfCourse(String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        Set<Lab> labs =c.getLabs();
        return labs;
    }


    public EntitySetManager<Lab, Course> getLabsOfCourseByCourseId(String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        Set<Lab> labs = c.getLabs();
        return new EntitySetManager<Lab, Course>(labs, c);
    }

    public List<Course> findByCourseId(String course_id){
        List<Course> found = repository.findByCourse_id(course_id);
        return found;
    }

    public List<Course> findCourseByName(String name){
        List<Course> found = repository.findByName(name);
        return found.size() == 0? null: found;
    }

    @Transactional
    public void deleteCourseByCourseId(String course_id){
        List<Course> found = findByCourseId(course_id);
        if (found != null && found.size() > 0)
            deleteById(found.get(0).getId());
    }

    public List<Course> getCoursesForTeacher(long id){
        List<Course> found = repository.findByCreator_id(id);
        return found;
    }

    public Course findByUserIdAndCourseId(long user_id, String course_id){
        List<Course> found = repository.findBycreator_idAndcourse_id(user_id, course_id);
        if (found.size() != 0) return found.get(0);
        return null;
    }

    public List<Course> searchCourseWithKeyword(String keyword) {
        return repository.searchCourseWithKeyword(keyword);
    }

    public Course findInviteCodeByName(String name){
        return repository.findInviteCodeByName(name);
    }


}
