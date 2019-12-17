package com.org.simplelab.game;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.mongodb.InstantiatedEquipment;
import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.mongodb.StepAction;
import com.org.simplelab.database.entities.mongodb.StepRecord;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.services.restservice.LabDB;
import com.org.simplelab.database.services.restservice.LabInstanceDB;
import com.org.simplelab.database.services.restservice.RecipeDB;
import com.org.simplelab.restcontrollers.dto.Workspace;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles transformations and events in doLab.
 */
@Component
public class DoLabEventHandler {

    @Data
    public static class InteractionObjects {
        private Equipment eq1, eq2, result;
        private Interaction interaction;
        private String parameter;
    }

    @Autowired
    LabInstanceDB instanceDB;

    @Autowired
    LabDB labDB;

    @Autowired
    RecipeDB recipeDB;

    @Transactional
    public Workspace buildNewWorkspaceFromLab(Lab l, long user_id){

        //build lab record
        LabInstance li = new LabInstance();
        li.setLabName(l.getName());
        li.setLabDescription(l.getDescription());
        li.setSerialized_lab(DBUtils.serialize(l));
        li.setCreatorId(l.getCreator().getId());
        li.setLabId(l.getId());
        li.setUserId(user_id);

        try {
            instanceDB.insert(li);
        } catch (Exception e){}
        LabInstance current = instanceDB.findByLabIdAndUserId(l.getId(), user_id).get(0);

        Workspace ws = DBUtils.getMapper().map(l, Workspace.class);
        ws.setInstance_id(current.get_id());
        ws.setRecipes(recipeDB.getRecipeByCreateId(l.getCreator().getId()));
        return ws;
    }

    @Transactional
    public Workspace buildWorkspaceFromLabInstance(LabInstance li, long user_id){
        Workspace ws = new Workspace();
        //TODO: should probably reference serialized lab
        Lab originalLab = labDB.findById(li.getLabId());
        ws.setInstance_id(li.get_id());
        ws.setName(li.getLabName());
        ws.setDescription(li.getLabDescription());
        ws.setSteps(originalLab.getSteps());
        ws.setEquipments(originalLab.getEquipments());
        ws.setContinued(true);

        List<StepRecord> steps = li.getStepRecords();
        ws.setStarting_step(steps.size() + 1);

        /**
        List<InstantiatedEquipment> desEquipment = li.getEquipmentInstances()
                                                        .stream()
                                                        .map(serial -> (InstantiatedEquipment)SerializationUtils.deserialize(serial))
                                                        .collect(Collectors.toList());**/

        List<InstantiatedEquipment> restoredEquipment = new ArrayList<>();
        for (byte[] serial: li.getEquipmentInstances()){
            InstantiatedEquipment deserial = SerializationUtils.deserialize(serial);
            restoredEquipment.add(deserial);
        }

        ws.setEquipment_instances(restoredEquipment);
        ws.setRecipes(recipeDB.getRecipeByCreateId(li.getCreatorId()));
        return ws;
    }

    public void addInteractionToHistory(LabInstance instance, int stepNum, InteractionObjects interactionInfo){

        StepAction action = new StepAction();
        action.setEquipment1ToString(interactionInfo.getEq1().toString());
        action.setEquipment2ToString(interactionInfo.getEq2().toString());
        action.setInteraction(interactionInfo.getEq1().getInteraction().getTypeCode());
        action.setParameter(interactionInfo.getParameter());
        action.setResult(interactionInfo.result.toString());

        StepRecord currentStep = instance.getStepRecords().get(stepNum-1);
        currentStep.getUserActions().add(action);
        try {
            instanceDB.update(instance);
        } catch (Exception e) { /* dont update history on exception */ }

    }

    public void advanceStep(LabInstance instance){
        StepRecord lastStep = instance.getStepRecords().get(instance.getStepRecords().size()-1);
        StepRecord newStep = new StepRecord();
        newStep.setStepNum(lastStep.getStepNum()+1);
        instance.getStepRecords().add(newStep);
        try {
            instanceDB.update(instance);
        } catch (Exception e) { /* dont update history on exception */ }
    }

    public void finalizeInstance(LabInstance instance){
        instance.setGrade("complete");
        instance.setFinished(true);
        try {
            instanceDB.update(instance);
        } catch (Exception e) { /* dont update history on exception */ }
    }

}
