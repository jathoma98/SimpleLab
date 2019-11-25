package com.org.simplelab.database;

import com.org.simplelab.database.entities.BaseTable;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.database.repositories.EquipmentRepository;
import com.org.simplelab.database.repositories.LabRepository;
import com.org.simplelab.database.repositories.UserRepository;
import com.org.simplelab.restcontrollers.CourseRESTController;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DBService<T extends BaseTable> {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LabRepository labRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    public abstract boolean insert(T toInsert);

    public abstract boolean deleteById(long id);

}
