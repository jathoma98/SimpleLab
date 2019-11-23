package com.org.simplelab.database;


import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class CourseDB {

    @Autowired
    CourseRepository courseRepository;

    public boolean insertCourse(Course c){
        List<Course> found = findByCourseId(c.getCourse_id());
        if (found.size() > 0)
            return false;
        courseRepository.save(c);
        return true;
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
//        return courseRepository.findByCourse_id(course_id);
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

    @Autowired
    UserRepository userRepository;

    public void _TEST(){
        String metadata = "TEST";
        User u = userRepository.findByUsername("UNIT_TEST").get(0);
        Course c = new Course();
        c.set_metadata(metadata);
        c.setName("UNIT TEST course");
        c.getStudents().add(u);
        courseRepository.save(c);
        c = courseRepository.findByName("UNIT TEST course").get(0);
        System.out.println(c.toString());

        courseRepository.deleteBy_metadata(metadata);
    }
}
