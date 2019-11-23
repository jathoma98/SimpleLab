package com.org.simplelab.database.repositories;

import com.org.simplelab.database.entities.BaseTable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseTable> extends CrudRepository<T, Long> {


    @Modifying
    @Transactional
    void deleteBy_metadata(String metadata);


}
