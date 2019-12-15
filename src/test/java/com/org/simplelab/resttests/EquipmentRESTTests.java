package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.validators.EquipmentValidator;
import com.org.simplelab.database.validators.ImageFileValidator;
import com.org.simplelab.restcontrollers.EquipmentRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.JSONBuilder;
import com.org.simplelab.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EquipmentRESTTests extends RESTTestBaseConfig {

    private RESTRequest equipmentRequest;

    @BeforeEach
    void loadRestRequest() {
        equipmentRequest = new RESTRequest(mvc, EquipmentRESTController.BASE_MAPPING);
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

        ImageFileValidator file = new ImageFileValidator();
        file.setFileName("name");
        file.setFileType("type");
        file.setData("data");

        ev.setImg(file);
        equipmentRequest.sendData(POST, "", JSONBuilder.asJson(ev))
                .andExpectSuccess(true);

        Equipment found = equipmentDB.getRepository().findBy_metadata(metadata).get(0);
        assertTrue(found.getId() > 0);
        assertTrue(found.getImg().getId() > 0);
        assertEquals(ev.getName(), found.getName());
        assertEquals(ev.getType(), found.getType());
        assertEquals(ev.getProperties(), e.getProperties());


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
//        assertNull(ev.getImg());
    }


}
