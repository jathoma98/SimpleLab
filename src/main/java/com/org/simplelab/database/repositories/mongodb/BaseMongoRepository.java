package com.org.simplelab.database.repositories.mongodb;

import com.org.simplelab.database.entities.mongodb.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseDocument> extends MongoRepository<T, String> {


}
