package com.org.simplelab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class RESTTests extends SpringTestConfig {

    /**
     * id: 5dc22379913bf470ddf11e4e
     * corresponds to user: 12345
     */

    private static final String id = "5dc22379913bf470ddf11e4e";
    private static final String username = "12345";

    @Autowired
    MockMvc mockMvc;

    @Test
    void testCreateGetCourseTeacher(){
        this.mockMvc.perform()

    }
}
