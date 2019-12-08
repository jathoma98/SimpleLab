package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Equipment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends BaseRepository<Equipment> {

    List<Equipment> findByName(String name);

    <T> List<T> findByCreator_id(long id, Class<T> projection);
    List<Equipment> findByCreator_id(long id);
}
