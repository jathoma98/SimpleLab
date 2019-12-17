package com.org.simplelab;

import com.org.simplelab.controllers.DoLabController;
import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.mongodb.InstantiatedEquipment;
import com.org.simplelab.database.entities.sql.*;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.utils.TestUtils;
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
        DTO.LabSaveStateDTO dto = new DTO.LabSaveStateDTO();
        dto.setEquipments(ieq_set);
        dto.setLabFinished(false);
        dto.setStep(null);
//        dlc.handleSaveWorkspaceState(dto, lab_instance_id);

        List<byte[]> found_ieq = instanceDB.findById(lab_instance_id).getEquipmentInstances();
        Collection<InstantiatedEquipment> found =           found_ieq.stream()
                                                            .map((serial) -> (InstantiatedEquipment)DBUtils.deserialize(serial))
                                                            .collect(Collectors.toList());

        found.forEach( instance -> {
            assertTrue(ieq_set.contains(instance));
        });
    }
}
