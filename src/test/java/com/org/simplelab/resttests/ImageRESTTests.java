package com.org.simplelab.resttests;


import com.org.simplelab.database.entities.sql.files.ImageFile;
import com.org.simplelab.database.validators.ImageFileValidator;
import com.org.simplelab.restcontrollers.ImageFileRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.DBTestUtils;
import com.org.simplelab.utils.JSONBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.GET;
import static com.org.simplelab.restrequest.RESTRequest.RequestType.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageRESTTests extends RESTTestBaseConfig {

    private RESTRequest imageRequest;

    @BeforeEach
    void loadRestRequest() {
        imageRequest = new RESTRequest(mvc, ImageFileRESTController.BASE_MAPPING);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testFileUpload() throws Exception{
        ImageFileValidator v = new ImageFileValidator();
        v.setFileName("testfile");
        v.setFileType(".png");
        v.setData("test data".getBytes());
        v.set_metadata(metadata);

        imageRequest.sendData(POST, "/upload", JSONBuilder.asJson(v))
                .andExpectSuccess(true);

        ImageFile found = imageDB.getRepository().findBy_metadata(metadata).get(0);
        assertTrue(found.getId() > 0);
        assertEquals(found.getFileName(), v.getFileName());
        assertEquals(found.getFileType(), v.getFileType());
        assertTrue(Arrays.equals(found.getData(), v.getData()));
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testFileGet() throws Exception{
        ImageFile file = new ImageFile();
        file.setFileType("text");
        file.setFileName("test");
        file.setData("test data".getBytes());
        long file_id = DBTestUtils.insertAndGetId(file, imageDB);

        imageRequest.send(GET, "/" + Long.toString(file_id))
            .andExpectData(JSONBuilder.asJson(file));

    }


}
