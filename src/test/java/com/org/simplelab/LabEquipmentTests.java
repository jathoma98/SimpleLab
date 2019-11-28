package com.org.simplelab;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.EquipmentProperty;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

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
        Equipment e = TestUtils.createJunkEquipment();
        Set<EquipmentProperty> properties = new HashSet<>();
        for (int i = 0; i < 10; i++){
            properties.add(TestUtils.createJunkEquipmentProperty(e));
        }
        e.setProperties(properties);

        equipmentDB.insert(e);



    }



}
