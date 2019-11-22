package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Lab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends BaseRepository<Lab> {

    List<Lab> findByName(String name);

    void deleteById(long lab_id);

}
