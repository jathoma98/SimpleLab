package com.org.simplelab.controllers;

import com.org.simplelab.database.services.CourseDB;
import com.org.simplelab.database.services.EquipmentDB;
import com.org.simplelab.database.services.LabDB;
import com.org.simplelab.database.services.UserDB;
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

    @Autowired
    protected HttpSession session;

    protected long getUserIdFromSession(){
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
