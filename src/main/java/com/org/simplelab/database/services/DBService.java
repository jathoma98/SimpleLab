package com.org.simplelab.database.services;

import com.org.simplelab.exception.EntityDBModificationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public abstract class DBService<BaseEntityType, IDType> {

    public abstract CrudRepository<BaseEntityType, IDType> getRepository();

    /**
     * Inserts entity into the DB.
     * @param toInsert - entity of type T to insert.
     * @return - true if insertion was successful
     * @throws EntityDBModificationException - If an error occurred during insertion
     */
    public BaseEntityType insert(BaseEntityType toInsert) throws EntityDBModificationException {
        checkInsertionCondition(toInsert);
        return getRepository().save(toInsert);
    }

    /**
     * Deletes the entity from the DB.
     * @param id - Id of the Entity to delete.
     * @return - true
     */
    @Transactional
    public boolean deleteById(IDType id){
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e){
            //no issue if we try to delete same object, should just do nothing.
        }
        return true;
    }

    public boolean update(BaseEntityType toUpdate) throws EntityDBModificationException{
        checkUpdateCondition(toUpdate);
        getRepository().save(toUpdate);
        return true;
    }

    public BaseEntityType findById(IDType id){
        Optional<BaseEntityType> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return null;
    }


    protected abstract void checkInsertionCondition(BaseEntityType toInsert) throws EntityDBModificationException;

    protected abstract void checkUpdateCondition(BaseEntityType toUpdate) throws EntityDBModificationException;


}
