package com.org.simplelab;

import com.org.simplelab.controllers.DoLabController;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.mongodb.InstantiatedEquipment;
import com.org.simplelab.database.entities.sql.*;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class DoLabTests extends SpringTestConfig {
    @Autowired
    DoLabController dlc;

    /**
     * Tests a 3 step lab with target objects that involve the object being heated to 3
     * different temperatures.
     */
    @Test
    void threeStepHeatLabTest() throws Exception{
        String target_type = "container";
        int numSteps = 3;

        Lab l = TestUtils.createJunkLab();

        for (int i = 0; i < numSteps; i++) {
            Equipment target1 = TestUtils.createJunkEquipment();
            target1.setType(target_type);
            EquipmentProperty target1prop = new EquipmentProperty();
            target1prop.setParentEquipment(target1);
            target1prop.setPropertyKey("temperature");
            target1prop.setPropertyValue(Integer.toString((i+1) * 100));
            target1.getProperties().add(target1prop);

            //now make a step and add to lab
            Step s = new Step();
            s.setLab(l);
            s.setTargetObject(target1);
            s.setStepNum(i+1);
            l.getSteps().add(s);
        }

        //save the lab
        labDB.insert(l);
        l = labDB.searchLabWithKeyword(l.getName()).get(0);

        //initialize the workspace
        Workspace ws = (Workspace)dlc.getLabToDo(l.getId()).getData();

        //create 3 DTOs representing 3 user actions -- user will heat a container 3 times.
        Equipment heater = new Equipment();
        heater.setInteraction(Interaction.HEAT);

        Equipment toHeat = TestUtils.createJunkEquipment();
        EquipmentProperty toHeatProp = new EquipmentProperty();
        toHeatProp.setParentEquipment(toHeat);
        toHeatProp.setPropertyKey("temperature");
        toHeatProp.setPropertyValue("0");
        toHeat.setType(target_type);
        toHeat.getProperties().add(toHeatProp);

        for (int i = 0; i < numSteps; i++){
            DTO.EquipmentInteractionDTO dto = new DTO.EquipmentInteractionDTO();
            dto.setObject1(heater);
            dto.setObject2(toHeat);
            dto.setStepNum(i+1);
            dto.setParameter("100");
            String returnMsg = dlc.handleEquipmentInteraction(ws.getInstance_id(), dto).getAction();
            System.out.println(returnMsg);
            if (i == numSteps - 1){
                assertEquals(RRO.LAB_ACTION_TYPE.COMPLETE_LAB.name(), returnMsg);
            } else {
                assertEquals(RRO.LAB_ACTION_TYPE.ADVANCE_STEP.name(), returnMsg);
            }
        }


    }

    @Test
    void testLoadInstance() throws Exception{
        String target_type = "container";
        int numSteps = 3;

        Lab l = TestUtils.createJunkLab();

        for (int i = 0; i < numSteps; i++) {
            Equipment target1 = TestUtils.createJunkEquipment();
            target1.setType(target_type);
            EquipmentProperty target1prop = new EquipmentProperty();
            target1prop.setParentEquipment(target1);
            target1prop.setPropertyKey("temperature");
            target1prop.setPropertyValue(Integer.toString((i+1) * 100));
            target1.getProperties().add(target1prop);

            //now make a step and add to lab
            Step s = new Step();
            s.setLab(l);
            s.setTargetObject(target1);
            s.setStepNum(i+1);
            l.getSteps().add(s);
        }

        //save the lab
        labDB.insert(l);
        l = labDB.searchLabWithKeyword(l.getName()).get(0);

        Workspace ws1 = dlc.getLabToDo(l.getId()).getData();
        assertFalse(ws1.is_continued());

        Workspace ws2 = dlc.getLabToDo(l.getId()).getData();
        assertTrue(ws2.is_continued());
        assertEquals(1, ws2.getStarting_step());
    }

    @Test
    void getWorkspaceTest() throws Exception{
        Lab l = TestUtils.createJunkLabWithSteps(10);
        labDB.insert(l);

        Lab found = labDB.searchLabWithKeyword(l.getName()).get(0);
        RRO<Workspace> rro = dlc.getLabToDo(found.getId());
        assertEquals(true, rro.isSuccess());
        System.out.println("Equipment: " + rro.getData().getEquipments().toString());
        for (AbstractEquipment e: rro.getData().getEquipments()){
            assertTrue(l.getEquipments().contains(e));
        }
        System.out.println("Steps: " + rro.getData().getSteps().toString());
        for (Step s: rro.getData().getSteps()){
            assertTrue(l.getSteps().contains(s));
        }


    }

    @Test
    void testHeatingInteraction(){
        EquipmentProperty temp = new EquipmentProperty();
        String parameter = "100";
        temp.setPropertyKey("temperature");
        temp.setPropertyValue("50");
        Equipment e = TestUtils.createJunkEquipmentWithProperties(5);
        temp.setParentEquipment(e);
        e.getProperties().add(temp);

        Equipment heater = new Equipment();
        heater.setInteraction(Interaction.HEAT);
        heater.getInteraction().interactWith(e, parameter);

        System.out.print(e.getProperties());

        assertEquals("150", e.findProperty("temperature").getPropertyValue());
    }

    @Test
    void testEquipmentEquals(){
        Equipment eq1 = TestUtils.createJunkEquipmentWithProperties(5);
        Equipment eq2 = TestUtils.createJunkEquipmentWithProperties(5);
        assertFalse(eq1.equals(eq2));

        Equipment eq3 = new Equipment();
        eq3.setType(eq1.getType());

        for (EquipmentProperty eqp: eq1.getProperties()){
            EquipmentProperty epcopy = new EquipmentProperty();
            epcopy.setPropertyKey(eqp.getPropertyKey());
            epcopy.setPropertyValue(eqp.getPropertyValue());
            eq3.getProperties().add(epcopy);
        }

        assertTrue(eq1.equals(eq3));

    }

    @Test
    void testInteractionAssignment(){
        //test that equipment with Heat type code gets assigned Heat interaction
        Equipment test1 = TestUtils.createJunkEquipment();
        test1.setType(Interaction.HEAT.getTypeCode());
        test1.loadInteraction();
        assertTrue(Interaction.Heat.class.isInstance(test1.getInteraction()));

        //test that equipment with no valid typecode gets DoNothing interaction
        Equipment test2 = TestUtils.createJunkEquipment();
        test2.setType("not valid");
        test2.loadInteraction();
        assertTrue(Interaction.DoNothing.class.isInstance(test2.getInteraction()));
    }

    @Test
    void testSerializeEquipmentInstance() throws Exception{
        Set<InstantiatedEquipment> ieq_set = new HashSet<>();
        for (int i = 0; i < 10; i++){
            InstantiatedEquipment ieq = new InstantiatedEquipment();
            Equipment e = TestUtils.createJunkEquipmentWithProperties(3);
            DBUtils.getMapper().map(e, ieq);
            ieq.setX(i*10);
            ieq.setY(i*15);
            ieq_set.add(ieq);
        }

        Lab l = TestUtils.createJunkLabWithSteps(5);
        labDB.insert(l);
        long lab_id = labDB.searchLabWithKeyword(l.getName()).get(0).getId();

        String lab_instance_id = dlc.getLabToDo(lab_id).getData().getInstance_id();
        dlc.handleSaveWorkspaceState(ieq_set, lab_instance_id);

        List<byte[]> found_ieq = instanceDB.findById(lab_instance_id).getEquipmentInstances();
        Collection<InstantiatedEquipment> found =           found_ieq.stream()
                                                            .map((serial) -> (InstantiatedEquipment)DBUtils.deserialize(serial))
                                                            .collect(Collectors.toList());

        found.forEach( instance -> {
            assertTrue(ieq_set.contains(instance));
        });




    }


}
