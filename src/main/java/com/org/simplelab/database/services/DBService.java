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
    public boolean insert(BaseEntityType toInsert) throws SQLService.EntityDBModificationException {
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

    public abstract boolean update(BaseEntityType toUpdate) throws Exception;

    public BaseEntityType findById(IDType id){
        Optional<BaseEntityType> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return null;
    }






}
