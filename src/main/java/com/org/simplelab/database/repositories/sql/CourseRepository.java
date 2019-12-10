package com.org.simplelab.database.repositories.sql;
import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends BaseRepository<Course> {

    public List<Course> findByName(String name);

    public List<Course> findByCreator_id(long id);

    /**
     * Find all courses that a certain student is enrolled in.
     */
    @Query(value =
            "SELECT * from #{#entityName} \n" +
            "INNER JOIN course_students \n" +
            "ON course.id = course_students.course_id \n" +
            "WHERE course_students.students_id = :student_id", nativeQuery = true)
    public List<Course> findCoursesEnrolledIn(@Param("student_id") long student_id);

    @Modifying
    @Transactional
    @Query(value =
            "DELETE FROM #{#entityName}" +
            " WHERE (creator_id = :user_id" +
            " AND " +
            "course_id = :course_id)", nativeQuery = true)
    public void deleteBycreator_idAndcourse_id(@Param("user_id") long user_id,
                                               @Param("course_id") String course_id);

    @Query(value = "SELECT *\n" +
                    "FROM #{#entityName}\n" +
                    "WHERE course_id = :course_id", nativeQuery = true)
    public List<Course> findByCourse_id(@Param("course_id") String course_id);

    @Query(value = "SELECT *\n" +
                    "FROM #{#entityName}\n" +
                    "WHERE (creator_id = :user_id\n" +
                    "AND\n" +
                    "course_id = :course_id)", nativeQuery = true)
    public List<Course> findBycreator_idAndcourse_id(@Param("user_id") long user_id,
                                                     @Param("course_id") String course_id);

    @Query(value =
            "SELECT * FROM simplelab.course_students WHERE course_id = :cid AND student_id = :uid",
            nativeQuery = true)
    public List<User> findUserInCourse(@Param("uid") long uid,
                                        @Param("cid") long cid);

    @Query(nativeQuery = true, value = "SELECT * FROM #{#entityName} WHERE course_id LIKE %:keyword%")
    public List<Course> searchCourseWithKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT *\n" +
            "FROM #{#entityName}\n" +
            "WHERE name = :checkName\n", nativeQuery = true)
    public Course findInviteCodeByName(@Param("checkName") String keyword);


}
