package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {


    public List<Course> findByName(String name);

    @Query(value = "SELECT createdDate, course_id, name\n" +
                    "FROM course\n" +
                    "WHERE creator_id = ?1", nativeQuery = true)
    public List<Course> findForTeacher(long id);

    public List<Course> deleteBy_metadata(String metadata);

    @Modifying
    @Query(value = "DELETE FROM course WHERE (creator_id = ?1 AND course_id = ?2)", nativeQuery = true)
    public void deleteBycreator_idAndcourse_id(long user_id, String course_id);

    @Query(value = "SELECT *\n" +
                    "FROM course\n" +
                    "WHERE course_id = ?1", nativeQuery = true)
    public List<Course> findByCourse_id(String course_id);

    @Query(value = "SELECT *\n" +
                    "FROM course\n" +
                    "WHERE (creator_id = ?1\n" +
                    "AND\n" +
                    "course_id = ?2)", nativeQuery = true)
    public List<Course> findBycreator_idAndcourse_id(long user_id, String course_id);


}
