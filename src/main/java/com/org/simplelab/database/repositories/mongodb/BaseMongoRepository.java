package com.org.simplelab.database.repositories.mongodb;

import com.org.simplelab.database.entities.mongodb.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseDocument> extends MongoRepository<T, String> {

    @Transactional
    void deleteBy_metadata(String metadata);

}
