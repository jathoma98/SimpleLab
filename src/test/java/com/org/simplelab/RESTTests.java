package com.org.simplelab;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.entities.Course;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class RESTTests extends SpringTestConfig {


    private static final String user_id = "5dc3806aabb7582d6f626791";
    private static final String username = "UNIT_TEST";
    private static Map<String, Object> session_atr = new HashMap<>();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CourseDB courseDB;

    @Test
    void testCreateGetCourseTeacher() throws Exception{
        session_atr.put("user_id", user_id);
        session_atr.put("username", username);
        Map<String, String> rawJson = new HashMap<>();
        rawJson.put("name", metadata);
        rawJson.put("description", metadata);
        rawJson.put("course_id", "UNIT_TEST" + metadata);
        JSONObject json = new JSONObject(rawJson);

        this.mockMvc.perform(post("/course/rest")
                            .sessionAttrs(session_atr)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.toString()))
                            //.andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(content().json("{'success': 'true'}"));

        this.mockMvc.perform(get("/course/rest/loadInfo")
                             .sessionAttrs(session_atr))
                            .andDo(print())
                            .andExpect(status().isOk());


        //delete the course afterwards
        List<Course> found = courseDB.findCourse(metadata);
        courseDB.deleteCourse(found.get(0));





    }

}
