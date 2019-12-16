package com.org.simplelab.database.repositories.sql;

import com.org.simplelab.database.entities.sql.BaseTable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseTable> extends CrudRepository<T, Long> {


    @Modifying
    @Transactional
    void deleteBy_metadata(String metadata);

    <U> Optional<U> findById(Long id, Class<U> projection);

    List<T> findBy_metadata(String metadata);
}
