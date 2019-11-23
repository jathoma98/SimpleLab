package com.org.simplelab;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.entities.Course;
import com.org.simplelab.database.entities.User;
import com.org.simplelab.database.repositories.CourseRepository;
import com.org.simplelab.restcontrollers.CourseRESTController;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class RESTTests extends SpringTestConfig {


    private static final long user_id = 90;
    private static final String username = "12345";
    private static Map<String, Object> session_atr = new HashMap<>();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CourseDB courseDB;


    private void sendCourseToPOSTEndpoint(JSONObject json, String path) throws Exception{
        this.mockMvc.perform(post("/course/rest" + path)
                .sessionAttrs(session_atr)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': 'true'}"));
    }



    @Test
    @WithMockUser(username = username, password = username)
    void testCreateGetCourseTeacher() throws Exception{

        TestUtils.login(mockMvc);

        session_atr.put("user_id", user_id);
        session_atr.put("username", username);
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
                            .andDo(print())
                            .andExpect(status().isOk());


        //delete the course afterwards
        List<Course> found = courseDB.findCourseByName(metadata);
        courseDB.deleteCourse(found.get(0));


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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': 'true'}"));

        for (JSONObject json: objs){
            assertEquals(courseDB.findByCourseId((String)json.get("course_id")).size(), 0);
            courseDB.deleteCourseById(user_id, (String)json.get("course_id"));
        }

    }


    @Autowired
    CourseRepository cr;

    @Test
    @WithMockUser(username = username, password = username)
    void addStudentToCourseTest() throws Exception{
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

        sendCourseToPOSTEndpoint(json, CourseRESTController.ADD_STUDENT_MAPPING);

        boolean found = false;
        List<User> students = courseDB.getStudentsOfCourse(cid);
        System.out.println("Students list: " + students.toString());
        assertTrue(!students.isEmpty());

    }


}
