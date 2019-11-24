package com.org.simplelab.controllers;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.EquipmentDB;
import com.org.simplelab.database.LabDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Equipment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Contains instantiations of all DBs
 */
public abstract class BaseController {

    @Autowired
    protected UserDB userDB;

    @Autowired
    protected CourseDB courseDB;

    @Autowired
    protected LabDB labDB;

    @Autowired
    protected EquipmentDB equipmentDB;


}
