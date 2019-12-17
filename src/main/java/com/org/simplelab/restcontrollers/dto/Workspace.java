package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.entities.mongodb.StepRecordDTO;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Recipe;
import com.org.simplelab.database.entities.sql.Step;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Object to represent a Workspace for a student to perform a lab in.
 * If we want to save student's progress, we should have methods and fields here to get/set progress.
 */

@Data
public class Workspace extends DTO {
    public static final Workspace NO_WORKSPACE = GEN_NO_WORKSPACE();

    String name, description, instance_id;
    List<Step> steps;
    List<Recipe> recipes;
    Set<Equipment> equipments;
    boolean isContinued = false;

    //if isContinued = true:
    List<StepRecordDTO> step;
    List<Object> equipment_instances;

    private static Workspace GEN_NO_WORKSPACE(){
        Workspace ws = new Workspace();
        ws.setName("IF YOURE SEEING THIS, THIS IS AN ERROR!");
        return ws;
    }

}
