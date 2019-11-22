package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {

}
