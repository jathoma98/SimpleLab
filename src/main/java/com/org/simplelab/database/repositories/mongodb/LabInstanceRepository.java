package com.org.simplelab.database.repositories.mongodb;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LabInstanceRepository extends BaseMongoRepository<LabInstance> {

    List<LabInstance> findByLabId(long id);

}
