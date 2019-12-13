package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.JSONBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseRESTTests extends RESTTestBaseConfig {

    private RESTRequest courseRequest;

    @BeforeEach
    void loadRestRequest() {
        this.courseRequest = new RESTRequest(mvc, CourseRESTController.BASE_MAPPING);
    }

    @Test
    @WithMockUser(username = username, password = username)
    void testAddCourse() throws Exception{
        CourseValidator cv = new CourseValidator();
        cv.setName(metadata);
        cv.setDescription(metadata);
        cv.setCourse_id(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                     .andExpectSuccess(true);

        List<Course> found = courseDB.findByCourseId(cv.getCourse_id());
        assertEquals(1, found.size());
        assertEquals(cv.getName(), found.get(0).getName());
        assertEquals(cv.getDescription(), found.get(0).getDescription());
        assertEquals(cv.getDescription(), found.get(0).getCourse_id());

        //test invalid field
        cv.setName(null);
        cv.setCourse_id(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                .andExpectError(CourseValidator.EMPTY_FIELD);

        //test duplicate courseid;
        cv.setName(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                .andExpectError(CourseValidator.DUPLICATE_ID);


    }
}
