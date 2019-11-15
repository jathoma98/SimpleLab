package com.org.simplelab.database;


import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.repositories.CourseRepository;
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

    public boolean deleteCourse(Course c){
        courseRepository.delete(c);
        return true;
    }

    public List<Course> findByCourseId(String course_id){
        return courseRepository.findByCourse_id(course_id);
    }

    public List<Course> findCourse(String name){
        List<Course> found = courseRepository.findByName(name);
        return found.size() == 0? null: found;
    }

    public void deleteCourseById(String user_id, String course_id){
        courseRepository.deleteByUIDAndCourseID(user_id, course_id);
    }

    public List<Course> getCoursesForTeacher(String id){
        List<Course> found = courseRepository.findForTeacher(id);
        return found;
    }

    public Course findByUserIdAndCourseId(String user_id, String course_id){
        List<Course> found = courseRepository.findByUIDAndCourseID(user_id, course_id);
        if (found.size() != 0) return found.get(0);
        return null;
    }
}
