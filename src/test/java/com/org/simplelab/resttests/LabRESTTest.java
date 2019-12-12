package com.org.simplelab.resttests;

import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LabRESTTest extends RESTTestBaseConfig {

    private RESTRequest labRequest;

    @Override
    @BeforeEach
    void loadRestRequest () {
        labRequest = new RESTRequest(mvc, LabRESTController.BASE_MAPPING);
    }



    @Test
    void t1(){

    }

    @Test
    void t2(){

    }

}
