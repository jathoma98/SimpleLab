package com.org.simplelab;

import com.org.simplelab.controllers.DoLabController;
import com.org.simplelab.database.entities.interfaces.Interaction;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.EquipmentProperty;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.restcontrollers.dto.Workspace;
import com.org.simplelab.restcontrollers.rro.RRO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        for (Equipment e: rro.getData().getEquipments()){
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
        temp.setPropertyKey("temperature");
        temp.setPropertyValue("50");
        Equipment e = TestUtils.createJunkEquipmentWithProperties(5);
        temp.setParentEquipment(e);
        e.getProperties().add(temp);

        Equipment heater = new Equipment();
        heater.setInteraction(Interaction.HEAT);
        heater.getInteraction().interactWith(e);

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

}
