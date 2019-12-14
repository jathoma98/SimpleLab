package com.org.simplelab.database.services;

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
     * @throws SQLService.EntityDBModificationException - If an error occurred during insertion
     */
    public boolean insert(BaseEntityType toInsert) throws EntityDBModificationException {
        checkInsertionCondition(toInsert);
        getRepository().save(toInsert);
        return true;
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


    /**
     * Exception to be thrown when modification of a DB violates some constraint.
     * The message should include the contraint being violated.
     */
    public static class EntityDBModificationException extends Exception{
        protected static String GENERIC_INVALID_UPDATE_ERROR = "Attempted to call update() on a new entity. " +
                "update() should only be called on entities which already exist in the DB.";
        protected static String GENERIC_MODIFICATION_ERROR = "An error occurred while modifying this collection";
        EntityDBModificationException(){super(GENERIC_MODIFICATION_ERROR);}
        EntityDBModificationException(String msg){
            super(msg);
        }
    }
}
