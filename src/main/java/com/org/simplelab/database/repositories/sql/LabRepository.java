package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
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

//    @Modifying
//    @Transactional
//    @Query(value =
//            "DELETE FROM #{#entityName}" +
//                    " WHERE (creator_id = :user_id" +
//                    " AND " +
//                    "id = :id)", nativeQuery = true)
//    public void deleteBycreator_idAndlab_id(@Param("user_id") long user_id,
//                                               @Param("id") long lab_id);

    @Query(value = "SELECT *\n" +
            "FROM #{#entityName}\n" +
            "WHERE (creator_id = :user_id\n" +
            "AND\n" +
            "id = :lab_id)", nativeQuery = true)
    public List<Course> findBycreator_idAndLab_id(@Param("user_id") long user_id,
                                                     @Param("lab_id") long lab_id);

    @Query(nativeQuery = true, value = "SELECT * FROM #{#entityName} WHERE  name " +
            " LIKE %:keyword% OR description LIKE %:keyword%")
    public List<Lab> searchLabWithKeyword(@Param("keyword") String keyword);
}
