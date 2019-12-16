package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.repositories.sql.BaseRepository;
import com.org.simplelab.database.services.interfaces.SetModificationCondition;
import com.org.simplelab.exception.EntityDBModificationException;
import com.org.simplelab.exception.EntitySetModificationException;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Transactional
public abstract class SQLService<T extends BaseTable> extends DBService<T, Long> {

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

        private Collection<T> entitySet;
        private U fullEntity;
        public static final String NOT_FOUND_STRING = "Could not find entity to update. ";
        public EntitySetManager(Collection<T> set, U fullEntity){
            this.entitySet = set;
            this.fullEntity = fullEntity;
        }

        public void insert(T toInsert) throws EntitySetModificationException {
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

    @Override
    public abstract BaseRepository<T> getRepository();

    public <U> U findById(long id, Class<U> projection){
        Optional<U> found = getRepository().findById(id, projection);
        if (found.isPresent()){
            return found.get();
        }
        return null;

    }


    protected void checkUpdateCondition(T toUpdate) throws EntityDBModificationException {
        if (toUpdate.isNew()){
            throw new EntityDBModificationException(EntityDBModificationException.GENERIC_INVALID_UPDATE_ERROR);
        }
    }

    protected void checkInsertionCondition(T toInsert) throws EntityDBModificationException{

    }

}
