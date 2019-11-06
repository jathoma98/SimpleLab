package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>{

    public List<Course> findByName(String name);


}
