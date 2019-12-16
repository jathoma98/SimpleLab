package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.Step;
import com.org.simplelab.database.validators.LabValidator;
import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.DBTestUtils;
import com.org.simplelab.utils.JSONBuilder;
import com.org.simplelab.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.*;
import static org.junit.jupiter.api.Assertions.*;

public class LabRESTTests extends RESTTestBaseConfig {

    private RESTRequest labRequest;

    private static final boolean ENABLE_PRINTOUTS = true;

    @Override
    @BeforeEach
    void loadRestRequest () {

        labRequest = new RESTRequest(mvc, LabRESTController.BASE_MAPPING, ENABLE_PRINTOUTS);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddLab() throws Exception{
        LabValidator lv = new LabValidator();
        lv.setName("TEST");
        lv.setDescription("TEST");
        lv.set_metadata(metadata);
        labRequest.sendData(POST, "", JSONBuilder.asJson(lv))
                  .andExpectSuccess(true);
        Lab found = labDB.getRepository().findBy_metadata(metadata).get(0);
        assertEquals(found.getName(), lv.getName());
        assertEquals(found.getDescription(), lv.getDescription());

        //make sure adding invalid lab sends error
        lv.setName(null);
        labRequest.sendData(POST, "", JSONBuilder.asJson(lv))
                  .andExpectError(LabValidator.NO_NAME_ERROR);


    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddEquipment() throws Exception{
        int num_eq = 4;
        List<Equipment> originals = new ArrayList<>();
        long[] eq_ids = new long[num_eq];
        for (int i = 0; i < num_eq; i++){
            Equipment e = TestUtils.createJunkEquipmentWithProperties(3);
            originals.add(e);
            eq_ids[i] = DBTestUtils.insertAndGetId(e, equipmentDB);
        }
        Lab l = TestUtils.createJunkLab();
        long lab_id = DBTestUtils.insertAndGetId(l, labDB);
        String id_path = "/" + Long.toString(lab_id);
        labRequest.sendData(RESTRequest.RequestType.POST, id_path + "/equipment", JSONBuilder.asJson(eq_ids))
                  .andExpectSuccess(true);
        List<Equipment> saved = labDB.getEquipmentOfLabById(lab_id).getAsList();
        assertEquals(num_eq, saved.size());

        //adding the same equipment should not increase size of the list
        labRequest.sendData(RESTRequest.RequestType.POST, id_path+"/equipment", JSONBuilder.asJson(eq_ids))
                  .andExpectSuccess(true);

        saved = labDB.getEquipmentOfLabById(lab_id).getAsList();
        assertEquals(num_eq, saved.size());

        //make sure adding the same equipment to a different lab doesnt cause foreign key/primary key issues
        Lab l2 = TestUtils.createJunkLab();
        long lab_id2 = DBTestUtils.insertAndGetId(l2, labDB);
        id_path = "/" + Long.toString(lab_id2);
        labRequest.sendData(RESTRequest.RequestType.POST, id_path + "/equipment", JSONBuilder.asJson(eq_ids))
                  .andExpectSuccess(true);
        saved = labDB.getEquipmentOfLabById(lab_id2).getAsList();
        assertEquals(num_eq, saved.size());

    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDeleteLab() throws Exception{
        int numLabs = 4;
        long[] ids = new long[numLabs];
        IntStream.range(0, numLabs)
                 .forEach((i) -> {
            try {
                ids[i] = DBTestUtils.insertAndGetId(TestUtils.createJunkLab(), labDB);
            } catch (Exception e) {} });
        DTO.UserLabsDTO dto = new DTO.UserLabsDTO();
        dto.setLids(ids);
        labRequest.sendData(DELETE, LabRESTController.DELETE_MAPPING, JSONBuilder.asJson(dto))
                  .andExpectSuccess(true);

        IntStream.range(0, numLabs)
                .forEach((i) -> {
                    assertEquals(null, labDB.findById(ids[i]));
                });

        //make sure sending another delete request doesnt cause exception
        labRequest.sendData(DELETE, LabRESTController.DELETE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testUpdateLab() throws Exception{
        Lab junkLab = TestUtils.createJunkLab();
        LabValidator lv = new LabValidator();
        long lab_id = DBTestUtils.insertAndGetId(junkLab, labDB);
        String mapping = "/" + Long.toString(lab_id);

        lv.setName("Updated name");
        labRequest.sendData(POST, mapping, JSONBuilder.asJson(lv))
                  .andExpectSuccess(true);
        assertEquals(lv.getName(), labDB.findById(lab_id).getName());

        //make sure invalid updates dont work
        lv.setName("");
        labRequest.sendData(POST, mapping, JSONBuilder.asJson(lv))
                 .andExpectError(LabValidator.NO_NAME_ERROR);

    }

    class TestInfo {
       public String name, createdDate;
    }
    @Test
    @WithMockUser(username = username, password = username)
    void testGetLabList() throws Exception{
        int numLabs = 4;
        TestInfo[] testInfos = new TestInfo[numLabs];
        IntStream.range(0, numLabs)
                 .forEach((i) -> {
                    Lab toInsert = DBTestUtils.createJunkLab();
                    TestInfo info = new TestInfo();
                    info.name = toInsert.getName();
                    info.createdDate = toInsert.getCreatedDate();
                    testInfos[i] = info;
                    try {
                        labRequest.sendData(POST, "", JSONBuilder.asJson(toInsert))
                                .andExpectSuccess(true);
                    } catch (Exception e) {System.out.println("EXCEPTION: " + e.getStackTrace().toString());}
                 });
        labRequest.send(GET, LabRESTController.LOAD_LIST_LAB_MAPPING)
                  .andExpectData(JSONBuilder.asJson(testInfos));

    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddStep() throws Exception{
        DTO.AddStepDTO dto = new DTO.AddStepDTO();
        //need to insert equipment to get its id -- not clear in original code
        Equipment e = TestUtils.createJunkEquipment();
        long equip_id = DBTestUtils.insertAndGetId(e, equipmentDB);
        dto.setTargetEquipmentId(equip_id);
        dto.setStepNum(-1);
        dto.setTargetTemperature("15");
        dto.setTargetVolume("15");
        dto.setTargetWeight("15");
        long id = DBTestUtils.insertAndGetId(TestUtils.createJunkLab(), labDB);
        String mapping = "/" + Long.toString(id) + "/step";
        labRequest.sendData(POST, mapping, JSONBuilder.asJson(dto))
                  .andExpectSuccess(true);

        List<Step> steps = labDB.getStepsOfLabById(id).getAsList();
        assertEquals(1, steps.size());
        assertTrue(steps.get(0).getId() > 0);
        assertEquals(e, steps.get(0).getTargetObject());

        //ensure that inserting a step w/ duplicate stepnum still works
        labRequest.sendData(POST, mapping, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        //ensure target equipment is saved too
        steps = labDB.getStepsOfLabById(id).getAsList();
        assertEquals(2, steps.size());
        assertEquals(2, steps.get(steps.size()-1).getStepNum());
        steps.forEach(step -> assertTrue(step.getId() > 0));

        //ensure that inserting a step out of order doesnt work
        dto.setStepNum(500);
        labRequest.sendData(POST, mapping, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        steps = labDB.getStepsOfLabById(id).getAsList();
        assertEquals(2, steps.size());
        assertEquals(2, steps.get(steps.size()-1).getStepNum());
        steps.forEach( step -> assertFalse(step.getTargetObject().getId() == 0));
        steps.forEach(step -> assertTrue(step.getId() > 0));

        //ensure that inserting a step into another lab doesnt cause foreign key exception
        long id2 = DBTestUtils.insertAndGetId(TestUtils.createJunkLabWithSteps(2), labDB);
        String mapping2 = "/" + Long.toString(id2) + "/step";
        dto.setStepNum(-1);
        labRequest.sendData(POST, mapping2, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        Step foundStep = labDB.getStepsOfLabById(id2).getAsList().get(0);
        assertEquals(1, foundStep.getStepNum());
        assertFalse(foundStep.getTargetObject().getId() == 0);
        assertTrue(foundStep.getId() > 0);

        //delete all steps
        labRequest.send(DELETE, mapping)
                .andExpectSuccess(true);

        steps = labDB.getStepsOfLabById(id).getAsList();
        assertEquals(0, steps.size());
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testDeleteStep() throws Exception{
        Lab l = TestUtils.createJunkLabWithSteps(5);
        long lab_id = DBTestUtils.insertAndGetId(l, labDB);
        int stepNumToDelete = 5;
        String mapping = "/" + Long.toString(lab_id) + "/" + Long.toString(stepNumToDelete);
        labRequest.send(DELETE, mapping)
                  .andExpectSuccess(true);
        List<Step> steps = labDB.getStepsOfLabById(lab_id).getAsList();
        steps.sort(Step::compareTo);
        IntStream.range(1, steps.size()+1)
                 .forEach((i) -> assertEquals(i, steps.get(i-1).getStepNum()));

        //ensure deleting an out of order step properly changes other stepnums
        stepNumToDelete = 2;
        mapping = "/" + Long.toString(lab_id) + "/" + Long.toString(stepNumToDelete);
        labRequest.send(DELETE, mapping)
                 .andExpectSuccess(true);
        List<Step> steps2 = labDB.getStepsOfLabById(lab_id).getAsList();
        steps.sort(Step::compareTo);
        IntStream.range(1, steps2.size()+1)
                 .forEach((i) -> assertEquals(i, steps2.get(i-1).getStepNum()));
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testGetSteps() throws Exception{
        Lab l = TestUtils.createJunkLabWithSteps(3);
        long lab_id = DBTestUtils.insertAndGetId(l, labDB);
        String mapping = "/" + lab_id + "/step";
        labRequest.send(GET, mapping)
                .andExpectSuccess(true);

    }

}
