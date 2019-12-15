package com.org.simplelab.resttests;


import com.org.simplelab.restcontrollers.ImageFileRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;

public class ImageRESTTests extends RESTTestBaseConfig {

    private RESTRequest imageRequest;

    @BeforeEach
    void loadRestRequest() {
        imageRequest = new RESTRequest(mvc, ImageFileRESTController.BASE_MAPPING);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testFileUpload() throws Exception{
        String fileName = "test.txt";
        File file = new File(fileName);
        file.delete();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", fileName, "text/plain", "test data".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart(ImageFileRESTController.BASE_MAPPING + "/upload").file(mockMultipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


}
