package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.services.projections.Projection;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StepRepository extends BaseRepository<Step>{

     List<Step> findByLab_id(Long id);


}
