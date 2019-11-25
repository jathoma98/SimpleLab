package com.org.simplelab.database;


import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Component
public class CourseDB extends DBService<Course> {
    public static class CourseTransactionException extends Exception{
        public static final String NO_COURSE_FOUND = "The requested course could not be found.";
        CourseTransactionException(String message){
            super(message);
        }
    }

    @Override
    public boolean insert(Course c){
        List<Course> found = findByCourseId(c.getCourse_id());
        if (found != null && found.size() > 0)
            return false;
        courseRepository.save(c);
        return true;
    }

    @Override
    public boolean deleteById(long id){
        courseRepository.deleteById(id);
        return true;
    }

    @Override
    public Course findById(long id) {
        Optional<Course> found = courseRepository.findById(id);
        return found.isPresent() ? found.get() : null;
    }

    /**
     * Adds a student to a Course.
     * @param course_id - course_id of the course to be modified
     * @param u - User object representing the student to be added.
     * @throws CourseTransactionException - when Course with given course_id does not exist
     */
    public void addStudentToCourse(String course_id , User u) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);

        System.out.println("Found target course: " + c.toString());
        c.getStudents().add(u);
        System.out.println("Modified course: " + c.toString());
        updateCourse(c);
    }

    /**
     * Returns list of students in a course.
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
        students.addAll(c.getStudents());
        return students;
    }

    /**
     * Removes a student from a course
     * @param student - User object representing student to be removed.
     * @param course_id - Id of the course to remove student from.
     * @return List of students with the given student removed.
     * @throws CourseTransactionException - if the provided course does not exist.
     */
    public List<User> removeStudentFromCourse(User student, String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        Set<User> students = c.getStudents();

        //remove() returns true when the element exists
        if (students.remove(student)){
            c.setStudents(students);
            updateCourse(c);
        }

        ArrayList<User> toList = new ArrayList<>();
        toList.addAll(students);
        return toList;
    }

    public List<Lab> getListOfLabs(String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        Set<Lab> labs = c.getLabs();

        ArrayList<Lab> toList = new ArrayList<>();
        toList.addAll(labs);
        return toList;
    }

    public boolean updateCourse(Course c){
        courseRepository.save(c);
        return true;
    }

    public boolean deleteCourse(Course c){
        courseRepository.delete(c);
        return true;
    }

    public List<Course> findByCourseId(String course_id){
        List<Course> found = courseRepository.findByCourse_id(course_id);
        return found;
    }

    public List<Course> findCourseByName(String name){
        List<Course> found = courseRepository.findByName(name);
        return found.size() == 0? null: found;
    }

    @Transactional
    public void deleteCourseById(long user_id, String course_id){
        courseRepository.deleteBycreator_idAndcourse_id(user_id, course_id);
    }

    public List<Course> getCoursesForTeacher(long id){
        List<Course> found = courseRepository.findByCreator_id(id);
        return found;
    }

    public Course findByUserIdAndCourseId(long user_id, String course_id){
        List<Course> found = courseRepository.findBycreator_idAndcourse_id(user_id, course_id);
        if (found.size() != 0) return found.get(0);
        return null;
    }

    public boolean isUserInCourse(Long uid, long cid){
        List<User> user = courseRepository.findUserInCourse(uid, cid);
        if(user != null && user.size() == 0)
            return true;
        return false;
    }

    @Autowired
    UserRepository userRepository;

}
