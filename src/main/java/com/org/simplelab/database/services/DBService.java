package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.repositories.*;
import com.org.simplelab.database.services.interfaces.SetModificationCondition;
import com.org.simplelab.database.services.projections.Projection;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Getter
@Transactional
public abstract class DBService<T extends BaseTable> {

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

    /**
     * Manager class for handling insertion and deletion of inner lists in entities
     *
     * ex: If you want to add a User to a Course, you would call courseDB.getStudents, which would return
     * a StudentSetManager, which has custom insert and delete methods depending on the behavior
     * defined for insertion and deletion into that collection.
     * @param <T> - The entity type of the list.
     * @param <U> - the entity type which contains the list
     * example: For Course with list of Users, T = User, U = Course
     */
    @Getter
    public static class EntitySetManager<T extends BaseTable, U extends BaseTable>
            implements SetModificationCondition<T>{
        /**
         * Exception to be thrown in case of illegal modification of the entity set.
         */
        public static class EntitySetModificationException extends Exception{
            EntitySetModificationException(String msg){
                super(msg);
            }
        }

        private Collection<T> entitySet;
        private U fullEntity;
        public static final String NOT_FOUND_STRING = "Could not find entity to update. ";
        public EntitySetManager(Collection<T> set, U fullEntity){
            this.entitySet = set;
            this.fullEntity = fullEntity;
        }

        public void insert(T toInsert) throws EntitySetModificationException{
            checkLegalInsertion(toInsert);
            entitySet.add(toInsert);
        }
        public void delete(T toDelete) throws EntitySetModificationException{
            checkLegalDeletion(toDelete);
            entitySet.remove(toDelete);
        }
        public List<T> getAsList(){
            List<T> toList = new ArrayList<>();
            toList.addAll(entitySet);
            return toList;
        }
    }

    public abstract BaseRepository<T> getRepository();

    /**
     * Inserts entity into the DB.
     * @param toInsert - entity of type T to insert.
     * @return - true if insertion was successful
     * @throws EntityDBModificationException - If an error occurred during insertion
     */
    public boolean insert(T toInsert) throws EntityDBModificationException {
        getRepository().save(toInsert);
        return true;
    }

    /**
     * Deletes the entity from the DB.
     * @param id - Id of the Entity to delete.
     * @return - true
     */
    @Transactional
    public boolean deleteById(long id){
        getRepository().deleteById(id);
        return true;
    }

    public boolean update(T toUpdate) throws EntityDBModificationException{
        //cannot call update() on new entities
        if (toUpdate.isNew()){
            throw new EntityDBModificationException
                    (EntityDBModificationException.GENERIC_INVALID_UPDATE_ERROR);
        }
        getRepository().save(toUpdate);
        return true;
    }

    public T findById(long id){
        Optional<T> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return null;
    }

    public <U extends Projection> U findById(long id, Class<U> projection){
        Optional<U> found = getRepository().findById(id, projection);
        if (found.isPresent()){
            return found.get();
        }
        return null;

    }

}
