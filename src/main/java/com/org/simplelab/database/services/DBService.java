package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.interfaces.HasEntitySets;
import com.org.simplelab.database.repositories.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Transactional
public abstract class DBService<T extends BaseTable> {

    private BaseRepository<T> repository;

    /**
     * Exception to be thrown when insertion into a DB violates some constraint.
     * The message should include the contraint being violated.
     */
    public static class EntityInsertionException extends Exception{
        protected static String GENERIC_MODIFICATION_ERROR = "An error occurred while modifying this collection";
        EntityInsertionException(){super(GENERIC_MODIFICATION_ERROR);}
        EntityInsertionException(String msg){
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
    public static class EntitySetManager<T extends BaseTable, U extends BaseTable>{
        /**
         * Exception to be thrown in case of illegal modification of the entity set.
         */
        public static class EntitySetModificationException extends Exception{
            EntitySetModificationException(String msg){
                super(msg);
            }
        }

        private Set<T> entitySet;
        private U fullEntity;
        public static final String NOT_FOUND_STRING = "Could not find entity to update. ";
        public EntitySetManager(Set<T> set, U fullEntity){
            this.entitySet = set;
            this.fullEntity = fullEntity;
        }

        public void insert(T toInsert) throws EntitySetModificationException{
            entitySet.add(toInsert);
        }
        public void delete(T toDelete) throws EntitySetModificationException{
            entitySet.remove(toDelete);
        }
        public List<T> getAsList(){
            List<T> toList = new ArrayList<>();
            toList.addAll(entitySet);
            return toList;
        }
    }

    /**
     * Inserts entity into the DB.
     * @param toInsert - entity of type T to insert.
     * @return - true if insertion was successful
     * @throws EntityInsertionException - If an error occurred during insertion
     */
    public boolean insert(T toInsert) throws EntityInsertionException{
        getRepository().save(toInsert);
        return true;
    }

    /**
     * Deletes the entity from the DB.
     * Important: Entities which manage EntitySets
     * must have their Set field set to null before deletion,
     * so entities which implement the HasEntitySets will have
     * those sets nullified prior to deletion.
     * @param id - Id of the Entity to delete.
     * @return - true
     */
    @Transactional
    public boolean deleteById(long id){
        if (HasEntitySets.class.isInstance(this)){
            T found = findById(id);
            if (found != null){
                HasEntitySets clearSets = (HasEntitySets)found;
                clearSets.nullifyEntitySets();
                repository.delete(found);
            }
        } else {
            getRepository().deleteById(id);
        }
        return true;
    }

    public boolean update(T toUpdate){
        getRepository().save(toUpdate);
        return true;
    }

    public T findById(long id){
        Optional<T> found = getRepository().findById(id);
        if (found.isPresent())
            return found.get();
        return null;
    }

}
