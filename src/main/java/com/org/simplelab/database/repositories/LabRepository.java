package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Lab;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LabRepository extends BaseRepository<Lab> {

    List<Lab> findByName(String name);

    <T> List<T> findByCreator_id(long id, Class<T> projection);

    @Transactional
    @Modifying
    void deleteById(long lab_id);

    @Query(value = "SELECT *\n" +
            "FROM #{#entityName}\n" +
            "WHERE (creator_id = :user_id\n" +
            "AND\n" +
            "id = :lab_id)", nativeQuery = true)
    public List<Course> findBycreator_idAndLab_id(@Param("user_id") long user_id,
                                                     @Param("lab_id") long lab_id);



}
