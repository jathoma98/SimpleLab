package com.org.simplelab;

import com.org.simplelab.controllers.DoLabController;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.Lab;
import com.org.simplelab.database.entities.Step;
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

}
