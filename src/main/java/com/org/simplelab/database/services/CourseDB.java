package com.org.simplelab.database.services;


import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Component
public class CourseDB extends DBService<Course> {

    public static class CourseTransactionException extends DBService.EntityInsertionException{
        public static final String NO_COURSE_FOUND = "The requested course could not be found.";
        CourseTransactionException(String message){
            super(message);
        }
    }

    private class StudentSetManager extends EntitySetManager<User, Course>{
        public StudentSetManager(Set<User> set, Course c) { super(set, c);}
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
    @Transactional
    public boolean deleteById(long id){
        Course found = findById(id);
        if (found != null){
            found.setStudents(null);
            courseRepository.delete(found);
        }
        return true;
    }

    @Override
    public Course findById(long id) {
        Optional<Course> found = courseRepository.findById(id);
        return found.isPresent() ? found.get() : null;
    }

    public StudentSetManager getStudentsOfCourseById(long id){
        Course found = findById(id);
        if (found == null)
            return null;
        return new StudentSetManager(found.getStudents(), found);
    }

    public StudentSetManager getStudentsOfCourseByCourseId(String course_id){
        List<Course> found = findByCourseId(course_id);
        if (found == null)
            return null;
        Course c = found.get(0);
        return new StudentSetManager(c.getStudents(), c);
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
        update(c);
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
            update(c);
        }

        ArrayList<User> toList = new ArrayList<>();
        toList.addAll(students);
        return toList;
    }

    public EntitySetManager<Lab, Course> getLabsOfCourseByCourseId(String course_id) throws CourseTransactionException{
        List<Course> found = findByCourseId(course_id);
        if (found.size() == 0)
            throw new CourseTransactionException(CourseTransactionException.NO_COURSE_FOUND);
        Course c = found.get(0);
        Set<Lab> labs = c.getLabs();
        return new EntitySetManager<Lab, Course>(labs, c);
    }

    @Override
    public boolean update(Course c){
        courseRepository.save(c);
        return true;
    }

    @Transactional
    public boolean deleteCourse(Course c){
        return deleteById(c.getId());
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
       List<Course> found = findByCourseId(course_id);
       if (found != null && found.size() > 0)
           deleteById(found.get(0).getId());
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
}