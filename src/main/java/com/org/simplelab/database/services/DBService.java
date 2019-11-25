package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.EquipmentRepository;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DBService<T extends BaseTable> {

    public static class EntityInsertionException extends Exception{
        EntityInsertionException(String msg){
            super(msg);
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
