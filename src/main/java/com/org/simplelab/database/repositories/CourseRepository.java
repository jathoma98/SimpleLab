package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String>{


    public List<Course> findByName(String name);

    @Query(value = "{ 'creator._id': ?0 }", fields = "{ 'createdDate': 1 , 'course_id': 1, 'name': 1, 'description': 1 }")
    public List<Course> findForTeacher(String id);

    @Query(value = "{ _metadata: ?0 }", delete = true)
    public List<Course> deleteByMetadata(String metadata);

    @Query(value = "{ 'creator._id': ?0, 'name': ?1 }", delete = true)
    public List<Course> deleteByNameAndId(String user_id, String name);


}
