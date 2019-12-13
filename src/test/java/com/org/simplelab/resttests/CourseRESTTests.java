package com.org.simplelab.resttests;

import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.validators.CourseValidator;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restcontrollers.dto.DTO;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.JSONBuilder;
import com.org.simplelab.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.org.simplelab.restrequest.RESTRequest.RequestType.PATCH;
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

    @Test
    @WithMockUser(username = username, password = username)
    void testUpdateCourse() throws Exception{
        CourseValidator cv = new CourseValidator();
        cv.setName(metadata);
        cv.setDescription(metadata);
        cv.setCourse_id(metadata);
        cv.setInvite_code(metadata);
        courseRequest.sendData(POST, "", JSONBuilder.asJson(cv))
                .andExpectSuccess(true);

        DTO.CourseUpdateDTO dto = new DTO.CourseUpdateDTO();
        dto.setCourse_id_old(metadata);
        CourseValidator update = new CourseValidator();
        update.setName("new");
        update.setCourse_id("NEWNEWNEWNEWNEWNEWNEWNE");
        update.setDescription("new");
        dto.setNewCourseInfo(update);
        courseRequest.sendData(PATCH, CourseRESTController.UPDATE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectSuccess(true);

        Course updated = courseDB.findByCourseId(update.getCourse_id()).get(0);
        assertEquals(updated.getName(), update.getName());
        assertEquals(updated.getDescription(), update.getDescription());

        //TODO: figure out why update doesnt detect duplicate
        //ensure that updating with duplicate courseID throws exception
        CourseValidator cv_duplicate = new CourseValidator();
        cv_duplicate.setName("DUP");
        cv_duplicate.setCourse_id("DUP");
        cv_duplicate.setDescription("DUP");
        Course c = cv.build();
        c.setCreator(TestUtils.createJunkUser());
        courseDB.insert(c);

        dto.setCourse_id_old(update.getCourse_id());
        update.setCourse_id("DUP");
        courseRequest.sendData(PATCH, CourseRESTController.UPDATE_MAPPING, JSONBuilder.asJson(dto))
                .andExpectError(CourseValidator.DUPLICATE_ID);
    }
}
