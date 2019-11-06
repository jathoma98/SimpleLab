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
        courseRepository.save(c);
        return true;
    }

    public boolean deleteCourse(Course c){
        courseRepository.delete(c);
        return true;
    }

    public List<Course> getCoursesForTeacher(String id){
        List<Course> found = courseRepository.findForTeacher(id);
        return found;
    }

}
