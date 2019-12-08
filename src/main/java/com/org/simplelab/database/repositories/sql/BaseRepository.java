package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.services.projections.Projection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseTable> extends CrudRepository<T, Long> {


    @Modifying
    @Transactional
    void deleteBy_metadata(String metadata);

    <U extends Projection> Optional<U> findById(Long id, Class<U> projection);
}
