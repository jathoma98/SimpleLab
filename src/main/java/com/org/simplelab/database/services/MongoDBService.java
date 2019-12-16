package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.mongodb.BaseDocument;
import com.org.simplelab.database.repositories.mongodb.BaseMongoRepository;
import com.org.simplelab.exception.EntityDBModificationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class MongoDBService<T extends BaseDocument> extends DBService<T, String> {

    public static class MongoModificationException extends EntityDBModificationException {
        public static final String UPDATE_ERROR = "Error occured while trying to modify this document.";
        MongoModificationException(String msg){
            super(msg);
        }
    }

    public abstract BaseMongoRepository<T> getRepository();

    protected abstract T getNonexistent();

    @Override
    public T findById(String id){
        Optional<T> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return getNonexistent();
    }

    @Override
    protected void checkInsertionCondition(T toInsert) throws EntityDBModificationException{

    }

    @Override
    protected void checkUpdateCondition(T toUpdate) throws EntityDBModificationException{

    }



}
