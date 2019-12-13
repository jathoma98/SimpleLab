package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.Lab;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LabRESTTest extends RESTTestBaseConfig {

    private RESTRequest labRequest;

    @Override
    @BeforeEach
    void loadRestRequest () {
        labRequest = new RESTRequest(mvc, LabRESTController.BASE_MAPPING);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddLab() throws Exception{
        LabValidator lv = new LabValidator();
        lv.setName("TEST");
        lv.setDescription("TEST");
        lv.set_metadata(metadata);
        labRequest.sendData(RESTRequest.RequestType.POST, "", JSONBuilder.asJson(lv))
                  .andExpectSuccess(true);
        Lab found = labDB.getRepository().findBy_metadata(metadata).get(0);
        assertEquals(found.getName(), lv.getName());
        assertEquals(found.getDescription(), lv.getDescription());

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
        labRequest.sendData(RESTRequest.RequestType.DELETE, LabRESTController.DELETE_MAPPING, JSONBuilder.asJson(dto))
                  .andExpectSuccess(true);

        IntStream.range(0, numLabs)
                .forEach((i) -> {
                    assertEquals(null, labDB.findById(ids[i]));
                });

        //make sure sending another delete request doesnt cause exception
        labRequest.sendData(RESTRequest.RequestType.DELETE, LabRESTController.DELETE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testUpdateLab() throws Exception{

    }

}
