package com.org.simplelab.restcontrollers.dto;

import com.org.simplelab.database.entities.sql.AbstractEquipment;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.InstantiatedEquipment;
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

    String name, description, instance_id;
    List<Step> steps;
    Set<AbstractEquipment> equipments;

}
