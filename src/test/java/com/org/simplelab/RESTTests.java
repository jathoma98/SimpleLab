package com.org.simplelab;

import com.org.simplelab.controllers.BaseController;
import com.org.simplelab.database.entities.sql.Course;
import com.org.simplelab.database.entities.sql.Lab;
import com.org.simplelab.database.entities.sql.User;
import com.org.simplelab.database.services.CourseDB;
import com.org.simplelab.database.services.LabDB;
import com.org.simplelab.restcontrollers.CourseRESTController;
import com.org.simplelab.restcontrollers.LabRESTController;
import com.org.simplelab.restrequest.RESTRequest;
import com.org.simplelab.utils.TestUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.org.simplelab.restrequest.RESTRequest.RequestType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RESTTests extends SpringMockMVCTestConfig {


    public static final Map<String, Object> session_atr = initSessionAttr();

    private static Map initSessionAttr(){
        Map<String, Object> attr = new HashMap<>();
        attr.put(BaseController.USER_ID_KEY, user_id);
        attr.put("username", username);
        return attr;
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CourseDB courseDB;


    private void sendCourseToPOSTEndpoint(JSONObject json, String path) throws Exception{
        this.mockMvc.perform(post(CourseRESTController.BASE_MAPPING + path)
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': true}"));
    }

    private void sendLabToPOSTEndpoint(JSONObject json, String path) throws Exception{
        this.mockMvc.perform(post(LabRESTController.BASE_MAPPING + path)
                    .sessionAttrs(session_atr)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.toString()))
                   // .andDo(print())
                    .andExpect(status().isOk());
    }




    @Test
    @WithMockUser(username = username, password = username)
    void testCreateGetCourseTeacher() throws Exception{
        Map<String, String> rawJson = new HashMap<>();
        rawJson.put("name", metadata);
        rawJson.put("description", metadata);
        rawJson.put("course_id", "UNIT_TEST" + metadata);
        rawJson.put("_metadata", metadata);
        JSONObject json = new JSONObject(rawJson);

        sendCourseToPOSTEndpoint(json, "");

        this.mockMvc.perform(get("/course/rest" + CourseRESTController.LOAD_LIST_COURSE_MAPPING)
                             .principal(TestUtils.getUnitTestPrincipal())
                             .sessionAttrs(session_atr))
                            //.andDo(print())
                            .andExpect(status().isOk());

        /**
         * @Test: update course
         */
        Map<String, Object> newDTO = new HashMap<>();
        rawJson.put("description", metadata + "UPDATED");
        newDTO.put("course_id_old", "UNIT_TEST" + metadata);
        newDTO.put("newCourseInfo", new JSONObject(rawJson));
        json = new JSONObject(newDTO);
        this.mockMvc.perform(patch(CourseRESTController.BASE_MAPPING + CourseRESTController.UPDATE_MAPPING)
                            .sessionAttrs(session_atr)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.toString()))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().json("{'success': true}"));

        //make sure the course in the DB has the new info
        Course updated = courseDB.findByCourseId("UNIT_TEST" + metadata).get(0);
        assertEquals(updated.getDescription(), metadata + "UPDATED");


        //delete the course afterwards
        List<Course> found = courseDB.findCourseByName(metadata);
        courseDB.deleteById(found.get(0).getId());


    }

    @Test
    @WithMockUser(username = username, password = username)
    void deleteMappingTest() throws Exception {
        session_atr.put("user_id", user_id);
        session_atr.put("username", username);
        JSONObject[] objs = new JSONObject[3];

        for (int i = 0; i < objs.length; i++){
            Map<String, String> rawJson = new HashMap<>();
            rawJson.put("name", metadata + i);
            rawJson.put("description", metadata);
            rawJson.put("course_id", "UNIT_TEST" + metadata + i);
            rawJson.put("_metadata", metadata);
            JSONObject json = new JSONObject(rawJson);
            objs[i] = json;
        }

        for (JSONObject json: objs){
            sendCourseToPOSTEndpoint(json, "");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (JSONObject json: objs){
            sb.append(json.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");

        this.mockMvc.perform(delete("/course/rest/" + CourseRESTController.DELETE_MAPPING)
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sb.toString()))
               // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': true}"));

        for (JSONObject json: objs){
            assertEquals(courseDB.findByCourseId((String)json.get("course_id")).size(), 0);
            courseDB.deleteCourseByCourseId((String)json.get("course_id"));
        }

    }


    //TODO: remake this
    //@Test
    @WithMockUser(username = username, password = username)
    void addGetDeleteStudentFromCourseTests() throws Exception{
        String cid = "UNIT_TEST" + metadata;
        session_atr.put("user_id", user_id);
        session_atr.put("username", username);

        Map<String, String> rawJson = new HashMap<>();
        rawJson.put("name", metadata);
        rawJson.put("description", metadata);
        rawJson.put("course_id", cid);
        rawJson.put("_metadata", metadata);
        JSONObject json = new JSONObject(rawJson);

        //add course first
        sendCourseToPOSTEndpoint(json, "");

        //create DTO to add student
        rawJson = new HashMap<>();
        rawJson.put("course_id", cid);
        rawJson.put("usernameList", "[4321]");
        json = new JSONObject(rawJson);

        sendCourseToPOSTEndpoint(json, CourseRESTController.ADD_STUDENT_MAPPING);

        boolean found = false;
        List<User> students = courseDB.getStudentsOfCourse(cid);
        System.out.println("Students list: " + students.toString());

        assertTrue(!students.isEmpty());
        assertEquals("4321", students.get(0).getUsername());

        //check if getStudent endpoint works
        System.out.println("Gettings students from added course...");

        this.mockMvc.perform(post(CourseRESTController.BASE_MAPPING + CourseRESTController.GET_STUDENTS_MAPPING)
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        System.out.println("Deleting student from course");

        //delete the student
        this.mockMvc.perform(post(CourseRESTController.BASE_MAPPING + CourseRESTController.DELETE_STUDENTS_MAPPING)
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));


    }

    @Autowired
    LabDB labDB;

    @Test
    @WithMockUser(username = username, password = username)
    void addGetUpdateDeleteLabTests() throws Exception {
        session_atr.put("user_id", user_id);
        session_atr.put("username", username);

        /**
         * @Test: POST to /lab/rest to create a lab
         */
        Map<String, String> rawJson = new HashMap<>();
        rawJson.put("name", metadata);
        rawJson.put("_metadata", metadata);

        JSONObject json = new JSONObject(rawJson);
        sendLabToPOSTEndpoint(json, "");

        List<Lab> found = labDB.getLabsByCreatorId(user_id);
        assertEquals(1, found.size());
        assertEquals(found.get(0).getName(), metadata);

        //check invalid lab name
        rawJson.put("name", "");
        json = new JSONObject(rawJson);
        mockMvc.perform(post(LabRESTController.BASE_MAPPING)
                        .sessionAttrs(session_atr)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                        //.andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().json("{'success': false}"));

        long lab_id = found.get(0).getId();

        /**
         * @Test: POST to /lab/rest/{lab_id} to update a lab
         */
        String updatedName = metadata + "updated";
        rawJson = new HashMap<>();
        rawJson.put("name", updatedName);
        json = new JSONObject(rawJson);

        sendLabToPOSTEndpoint(json, "/" + lab_id);
        Lab updated = labDB.findById(lab_id);
        assertEquals(updated.getName(), updatedName);
        assertEquals(updated.get_metadata(), metadata);

        //check invalid lab id
        mockMvc.perform(post(LabRESTController.BASE_MAPPING + "/-1")
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': false}"));

        /**
         * @Test: GET to /lab/rest/{lab_id} to get the lab we created

        //get the lab we just created and check that it is the same JSON string
        mockMvc.perform(get(LabRESTController.BASE_MAPPING + "/" + lab_id)
                        .sessionAttrs(session_atr))
                        //.andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().json("{'data': " + json.toString() + "}")); */

        /**
         * @Test: Delete the lab we just created with DELETE to /lab/rest/{id}
         */

        mockMvc.perform(delete(LabRESTController.BASE_MAPPING + "/" + lab_id)
                        .sessionAttrs(session_atr))
                        .andExpect(status().isOk());

        Lab found_lab = labDB.findById(lab_id);
        assertNull(found_lab);


    }

    @Test
    @WithMockUser(username = username, password = username)
    void testNewClass() throws Exception{
        RESTRequest req = new RESTRequest(mockMvc, "");
        req.send(RequestType.GET, "/testRRO")
                .andExpectStatus(true);
    }



}
