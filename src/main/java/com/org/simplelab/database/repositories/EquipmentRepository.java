package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Equipment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends BaseRepository<Equipment> {

    List<Equipment> findByName(String name);

    <T> List<T> findByCreator_id(long id, Class<T> projection);

}
