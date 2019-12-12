package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.mongodb.BaseDocument;
import com.org.simplelab.database.repositories.mongodb.BaseMongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class MongoDBService<T extends BaseDocument> extends DBService<T, String> {

    public abstract BaseMongoRepository<T> getRepository();

    abstract T getNonexistent();

    @Override
    public T findById(String id){
        Optional<T> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return getNonexistent();
    }

    @Override
    public boolean update(T toUpdate){
        getRepository().save(toUpdate);
        return true;
    }

}
