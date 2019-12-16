package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.restcontrollers.EquipmentRESTController;
import com.org.simplelab.restcontrollers.ImageFileRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.JSONBuilder;
import com.org.simplelab.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.POST;
import static org.junit.jupiter.api.Assertions.*;

public class EquipmentRESTTests extends RESTTestBaseConfig {

    private RESTRequest equipmentRequest;
    private RESTRequest imgRequest;

    @BeforeEach
    void loadRestRequest() {
        equipmentRequest = new RESTRequest(mvc, EquipmentRESTController.BASE_MAPPING);
        imgRequest = new RESTRequest(mvc, ImageFileRESTController.BASE_MAPPING);
    }


    @Test
    @WithMockUser(username = username, password = username)
    void testAddEquipmentWithImage() throws Exception{
        Equipment e = TestUtils.createJunkEquipmentWithProperties(3);
        EquipmentValidator ev = new EquipmentValidator();
        ev.setProperties( new ArrayList<>());
        ev.setName(e.getName());
        ev.setType(e.getType());
        ev.set_metadata(metadata);
        ev.setProperties(e.getProperties());

        equipmentRequest.sendData(POST, "", JSONBuilder.asJson(ev))
                .andExpectSuccess(true);

        Equipment found = equipmentDB.getRepository().findBy_metadata(metadata).get(0);
        assertTrue(found.getId() > 0);
        assertEquals(ev.getName(), found.getName());
        assertEquals(ev.getType(), found.getType());
        assertEquals(ev.getProperties(), e.getProperties());

        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "test.txt", "text/plain", "test data".getBytes());
        String img_mapping = "/" + found.getId();
        mvc.perform(MockMvcRequestBuilders.multipart(ImageFileRESTController.BASE_MAPPING + img_mapping).file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        found =  equipmentDB.getRepository().findBy_metadata(metadata).get(0);
        ImageFile foundImg = found.getImg();
        assertEquals(foundImg.getFileName(), "test.txt");
        assertArrayEquals("test data".getBytes(), foundImg.getData());


    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddEquipWithoutImage() throws Exception{
        Equipment e = TestUtils.createJunkEquipmentWithProperties(3);
        EquipmentValidator ev = new EquipmentValidator();
        ev.setProperties( new ArrayList<>());
        ev.setName(e.getName());
        ev.setType(e.getType());
        ev.set_metadata(metadata);
        ev.setProperties(e.getProperties());

        equipmentRequest.sendData(POST, "", JSONBuilder.asJson(ev))
                .andExpectSuccess(true);

        Equipment found = equipmentDB.getRepository().findBy_metadata(metadata).get(0);
        assertTrue(found.getId() > 0);
        assertEquals(ev.getName(), found.getName());
        assertEquals(ev.getType(), found.getType());
        assertEquals(ev.getProperties(), e.getProperties());
    }


}
