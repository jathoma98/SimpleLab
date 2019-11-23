package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.Lab;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LabRepository extends BaseRepository<Lab> {

    List<Lab> findByName(String name);

    @Transactional
    @Modifying
    void deleteById(long lab_id);

}
