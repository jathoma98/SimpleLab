package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Lab;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends MongoRepository<Lab, String> {

    public List<Lab> findByName(String name);

    @Query(value = "{ 'id': ?0 }", delete = true)
    public void deleteById(String lab_id);



}
