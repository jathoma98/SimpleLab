package com.org.simplelab.controllers;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.EquipmentDB;
import com.org.simplelab.database.LabDB;
import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.Equipment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

/**
 * Contains instantiations of all DBs
 */
public abstract class BaseController {

    public static final String USER_ID_KEY = "user_id";

    @Autowired
    protected UserDB userDB;

    @Autowired
    protected CourseDB courseDB;

    @Autowired
    protected LabDB labDB;

    @Autowired
    protected EquipmentDB equipmentDB;

    protected long getUserIdFromSession(HttpSession session){
        long userId = -1;
        try {
            userId = (long) session.getAttribute(USER_ID_KEY);
        } catch (Exception e) {
            //TODO: put something here
            //redirect to login?
        }
        return userId;
    }


}
