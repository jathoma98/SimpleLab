package com.org.simplelab.controllers;

import com.org.simplelab.database.services.restservice.*;
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
    protected LabInstanceDB instanceDB;

    @Autowired
    protected RecipeDB recipeDB;

    @Autowired
    protected StepDB stepDB;

    @Autowired
    protected HttpSession session;

    @Autowired
    protected ImageFileDB imageDB;

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
