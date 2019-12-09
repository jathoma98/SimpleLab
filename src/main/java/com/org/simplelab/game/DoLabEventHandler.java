package com.org.simplelab.game;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.restcontrollers.dto.Workspace;
import org.springframework.stereotype.Component;

/**
 * Handles transformations and events in doLab.
 */
@Component
public class DoLabEventHandler {

    public Workspace buildWorkspace(Lab l){
        return DBUtils.getMapper().map(l, Workspace.class);
    }

}
