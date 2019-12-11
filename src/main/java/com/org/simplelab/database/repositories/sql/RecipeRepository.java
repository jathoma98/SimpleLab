package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RecipeRepository extends BaseRepository<Recipe> {

    List<Recipe> findByCreator_id(long id);

    @Query(value = "SELECT *\n" +
            "FROM #{#entityName}\n" +
            "WHERE " +
            "(equipment_one = :one AND equipment_two = :two AND result_id = :three)",
            nativeQuery = true)
    List<Recipe> findByEquipment_one_idAndEquipment_two_idAndResult(
            @Param("one")long a, @Param("two")long b, @Param("three")long c
    );

}
