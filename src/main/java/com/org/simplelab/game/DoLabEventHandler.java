package com.org.simplelab.game;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.restcontrollers.dto.Workspace;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles transformations and events in doLab.
 */
@Component
public class DoLabEventHandler {

    @Transactional
    public Workspace buildWorkspace(Lab l, long user_id){
        //build lab record
        LabInstance li = new LabInstance();
        li.setSerialized_lab(SerializationUtils.serialize(l));
        li.setLabId(l.getId());
        li.setUserId(user_id);
        //serialize equipment
        return DBUtils.getMapper().map(l, Workspace.class);
    }

}
