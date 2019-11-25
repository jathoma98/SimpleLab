package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.EquipmentRepository;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class DBService<T extends BaseTable> {

    public static class EntityInsertionException extends Exception{
        EntityInsertionException(String msg){
            super(msg);
        }
    }

    /**
     * Manager class for handling insertion and deletion of inner lists in entities
     * ex: If you want to add a User to a Course, you would call getStudents, which would return
     * a StudentSetManager, which has custom insert and delete methods depending on the behavior
     * defined for insertion and deletion into that collection.
     * @param <T> - The entity type of the list.
     */
    @Getter
    @Setter
    public static abstract class EntitySetManager<T extends BaseTable>{
        /**
         * Exception to be thrown in case of illegal modification of the entity set.
         */
        public static class EntitySetModificationException extends Exception{
            EntitySetModificationException(String msg){
                super(msg);
            }
        }

        private Set<T> entitySet;
        public EntitySetManager(Set<T> set){
            this.entitySet = set;
        }

        public void insert(T toInsert) throws EntitySetModificationException{
            entitySet.add(toInsert);
        }
        public void delete(T toDelete) throws EntitySetModificationException{
            entitySet.remove(toDelete);
        }
        public List<T> getAsList(){
            T[] array = (T[])entitySet.toArray();
            return Arrays.asList(array);
        }
    }

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LabRepository labRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    public abstract boolean insert(T toInsert) throws EntityInsertionException;

    public abstract boolean deleteById(long id);

    public abstract boolean update(T toUpdate);

    public abstract T findById(long id);

}
