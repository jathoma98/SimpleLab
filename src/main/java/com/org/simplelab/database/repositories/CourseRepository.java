package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {


    public List<Course> findByName(String name);

    @Query(value = "{ 'creator._id': ?0 }", fields = "{ 'createdDate': 1 , 'course_id': 1, 'name': 1 }")
    public List<Course> findForTeacher(String id);

    public List<Course> deleteBy_metadata(String metadata);

    @Query(value = "{ 'creator._id': ?0, 'course_id': ?1 }", delete = true)
    public List<Course> deleteByUIDAndCourseID(String user_id, String course_id);

    @Query(value = "{ 'course_id': ?0 }")
    public List<Course> findByCourse_id(String course_id);

    @Query(value = "{ 'creator._id': ?0, 'course_id': ?1 }")
    public List<Course> findByUIDAndCourseID(String user_id, String course_id);


}
