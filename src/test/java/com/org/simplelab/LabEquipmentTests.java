package com.org.simplelab;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.EquipmentProperty;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.Step;
import com.org.simplelab.database.services.DBService;
import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class LabEquipmentTests extends SpringTestConfig {

    @Test
    void testInsertEquipment() throws Exception{
        Equipment e = TestUtils.createJunkEquipment();
        equipmentDB.insert(e);

        Iterable<Equipment> found = equipmentDB.getRepository().findAll();
        boolean isPresent = false;
        for (Equipment foundEq: found){
            if (foundEq.getName().equals(e.getName())){
                isPresent = true;
                break;
            }
        }
        assertTrue(isPresent);
    }

    @Test
    void testInsertEqWithProperties() throws Exception{
        int NUM_PROPERTIES = 10;
        Equipment e = TestUtils.createJunkEquipmentWithProperties(NUM_PROPERTIES);
        equipmentDB.insert(e);

        List<Equipment> foundList = equipmentDB.getRepository().findByName(e.getName());
        Equipment found = foundList.get(0);

        DBService.EntitySetManager<EquipmentProperty, Equipment> set = equipmentDB.getPropertiesOfEquipment(found.getId());
        System.out.println("Set properties: " + set.getEntitySet().toString());
        assertEquals(NUM_PROPERTIES, set.getEntitySet().size());
    }

    @Test
    void testDeleteEqWithProperties() throws Exception{
        int NUM_PROPERTIES = 10;
        Equipment e = TestUtils.createJunkEquipmentWithProperties(NUM_PROPERTIES);
        equipmentDB.insert(e);

        List<Equipment> foundList = equipmentDB.getRepository().findByName(e.getName());
        Equipment found = foundList.get(0);

        equipmentDB.deleteById(found.getId());
        foundList = equipmentDB.getRepository().findByName(e.getName());

        assertEquals(0, foundList.size());
    }

    @Test
    void testUpdateEqAndProperties() throws Exception{
        int NUM_PROPERTIES = 20;
        Equipment e = TestUtils.createJunkEquipmentWithProperties(NUM_PROPERTIES);
        equipmentDB.insert(e);

        List<Equipment> foundList = equipmentDB.getRepository().findByName(e.getName());
        Equipment found = foundList.get(0);

        String UPDATED_NAME = found.getName() + "UPDATED";
        String UPDATED_VAL = "UPDATED_VAL";
        DBService.EntitySetManager<EquipmentProperty, Equipment> set = equipmentDB.getPropertiesOfEquipment(found.getId());
        for (EquipmentProperty ep: set.getEntitySet()){
            ep.setProperty_value(UPDATED_VAL);
        }
        set.getFullEntity().setName(UPDATED_NAME);
        equipmentDB.update(set.getFullEntity());

        foundList = equipmentDB.getRepository().findByName(e.getName());
        found = foundList.get(0);
        set = equipmentDB.getPropertiesManager(found);
        assertEquals(UPDATED_NAME, found.getName());
        for (EquipmentProperty ep: set.getEntitySet()){
            assertEquals(UPDATED_VAL, ep.getProperty_value());
        }

    }

    @Test
    void testAddLabWithSteps() throws Exception{
        int numSteps = 10;
        Lab l = TestUtils.createJunkLabWithSteps(numSteps);
        labDB.insert(l);

        List<Lab> found = labDB.getRepository().findByName(l.getName());
        Lab foundLab = found.get(0);

        assertEquals(l.getName(), foundLab.getName());
        System.out.println(foundLab.getSteps().toString());
    }

    @Test
    void deleteLabWithSteps() throws Exception{
        int numSteps = 10;
        Lab l = TestUtils.createJunkLabWithSteps(numSteps);

        labDB.insert(l);

        List<Lab> found = labDB.getRepository().findByName(l.getName());
        Lab foundLab = found.get(0);

        labDB.deleteById(foundLab.getId());
        found = labDB.getRepository().findByName(l.getName());

        assertEquals(0, found.size());
    }

    @Test
    void updateLabWithSteps() throws Exception{
        int numSteps = 10;
        Lab l = TestUtils.createJunkLabWithSteps(numSteps);

        labDB.insert(l);

        List<Lab> found = labDB.getRepository().findByName(l.getName());
        Lab foundLab = found.get(0);

        String UPDATED_NAME = foundLab.getName() + "UPDATED";
        String UPDATED_STEP_METADATA = "METADATA";
        foundLab.setName(UPDATED_NAME);
        for (Step s: foundLab.getSteps()){
            s.set_metadata(UPDATED_STEP_METADATA);
        }

        labDB.update(foundLab);

        foundLab = labDB.findById(foundLab.getId());
        assertEquals(UPDATED_NAME, foundLab.getName());
        for (Step s: foundLab.getSteps()){
            System.out.println(s.toString());
            assertEquals(UPDATED_STEP_METADATA, s.get_metadata());
        }

    }

    @Autowired
    LabRESTController lrc;

    @Test
    @Transactional
    void testAddStepEndpoint() throws Exception{
        Lab l = TestUtils.createJunkLab();
        labDB.insert(l);

        List<Lab> found = labDB.getRepository().findByName(l.getName());
        Lab foundLab = found.get(0);

        long lab_id = foundLab.getId();
        int numProperties = 5;
        DTO.LabAddStepDTO dto = new DTO.LabAddStepDTO();
        dto.setStepNum(1);
        dto.setTargetObject(TestUtils.createJunkEquipmentWithProperties(5));

        System.out.println(dto.getStepNum());
        System.out.println(dto.getTargetObject());

        lrc.addStepToLab(lab_id, dto);

        DBService.EntitySetManager<Step, Lab> set = labDB.getStepsOfLabById(lab_id);

        assertEquals(1, set.getEntitySet().size());
        for (Step s: set.getEntitySet()){
            System.out.println(s.toString());
            assertEquals(dto.getTargetObject().getProperties().size(), s.getTargetObject().getProperties().size());
            EquipmentProperty[] ep1 = new EquipmentProperty[dto.getTargetObject().getProperties().size()];
            EquipmentProperty[] ep2 = new EquipmentProperty[ep1.length];
            dto.getTargetObject().getProperties().toArray(ep1);
            s.getTargetObject().getProperties().toArray(ep2);
            for (int i = 0; i < ep1.length; i++){
                assertEquals(ep1[i].getProperty_key(), ep2[i].getProperty_key());
                assertEquals(ep1[i].getProperty_value(), ep2[i].getProperty_value());
            }
        }

        dto = new DTO.LabAddStepDTO();
        dto.setStepNum(2);
        dto.setTargetObject(TestUtils.createJunkEquipmentWithProperties(5));

    }



}
