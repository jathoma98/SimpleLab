package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends BaseRepository<Equipment> {

    List<Equipment> findByName(String name);

    <T> List<T> findByCreator_id(long id, Class<T> projection);
    List<Equipment> findByCreator_id(long id);

    @Query(value =
            "SELECT lab_id from lab_equipments \n" +
                    "WHERE lab_equipments.equipments_id = :equip_id", nativeQuery = true)
    public List<String> checkUsingEquipment(@Param("equip_id") long equip_id);
}
