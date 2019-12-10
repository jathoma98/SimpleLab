package com.org.simplelab.game;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.mongodb.LabInstance;
import com.org.simplelab.database.entities.mongodb.StepAction;
import com.org.simplelab.database.entities.mongodb.StepRecord;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.services.LabInstanceDB;
import com.org.simplelab.restcontrollers.dto.Workspace;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Workspace buildNewWorkspaceFromLab(Lab l, long user_id){

        //build lab record
        LabInstance li = new LabInstance();
        li.setSerialized_lab(SerializationUtils.serialize(l));
        li.setLabId(l.getId());
        li.setUserId(user_id);

        //build first step record
        StepRecord sr = new StepRecord();
        sr.setStepNum(1);
        li.getStepRecords().add(sr);

        instanceDB.insert(li);
        LabInstance current = instanceDB.findByLabIdAndUserId(l.getId(), user_id).get(0);

        Workspace ws = DBUtils.getMapper().map(l, Workspace.class);
        ws.setInstance_id(current.get_id());
        return ws;
    }

    @Transactional
    public Workspace buildWorkspaceFromLabInstance(LabInstance li, long user_id){
        Workspace ws = new Workspace();
        return null;
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
        instanceDB.update(instance);

    }

    public void advanceStep(LabInstance instance){
        StepRecord lastStep = instance.getStepRecords().get(instance.getStepRecords().size()-1);
        StepRecord newStep = new StepRecord();
        newStep.setStepNum(lastStep.getStepNum()+1);
        instance.getStepRecords().add(newStep);
        instanceDB.update(instance);
    }

    public void finalizeInstance(LabInstance instance){
        instance.setGrade("complete");
        instance.setFinished(true);
        instanceDB.update(instance);
    }

}
