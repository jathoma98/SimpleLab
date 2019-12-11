package com.org.simplelab.database.repositories.mongodb;

import com.org.simplelab.database.entities.mongodb.LabInstance;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LabInstanceRepository extends BaseMongoRepository<LabInstance> {

    List<LabInstance> findByLabIdAndUserId(long lab_id, long user_id);

    @Query("{ 'labId' : ?0 , 'userId': ?1 , 'finished': false }")
    List<LabInstance> findActiveInstanceOfLab(long lab_id, long user_id);


    @Query("{ 'creatorId' : ?0 , 'finished': true }")
    List<LabInstance> findFinishedLabsForTeacher(long teacher_id);

    List<LabInstance> findByUserId(long userId);

    List<LabInstance> findByCreatorId(long creatorId);

    List<LabInstance> findByLabId(long labId);
}
