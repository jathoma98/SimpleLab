package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.EquipmentRepository;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class DBService<T extends BaseTable> {

    public static class EntityInsertionException extends Exception{
        EntityInsertionException(String msg){
            super(msg);
        }
    }

    @Getter
    @Setter
    public static abstract class EntitySetManager<T extends BaseTable>{
        public static class EntitySetModificationException extends Exception{
            EntitySetModificationException(String msg){
                super(msg);
            }
        }

        private Set<T> entitySet;
        public EntitySetManager(Set<T> set){
            this.entitySet = set;
        }

        public abstract void insert(T toInsert) throws EntitySetModificationException;
        public abstract void delete(T toDelete) throws EntitySetModificationException;

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
