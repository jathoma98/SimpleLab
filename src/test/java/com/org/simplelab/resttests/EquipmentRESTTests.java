package com.org.simplelab.resttests;

import com.org.simplelab.restcontrollers.EquipmentRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import org.junit.jupiter.api.BeforeEach;

public class EquipmentRESTTests extends RESTTestBaseConfig {

    private RESTRequest equipmentRequest;

    @BeforeEach
    void loadRestRequest() {
        equipmentRequest = new RESTRequest(mvc, EquipmentRESTController.BASE_MAPPING);
    }


}
