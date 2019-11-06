package com.org.simplelab;

import com.org.simplelab.database.CourseDB;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class RESTTests extends SpringTestConfig {

    /**
     * id: 5dc22379913bf470ddf11e4e
     * corresponds to user: 12345
     */

    private static final String user_id = "5dc22379913bf470ddf11e4e";
    private static final String username = "12345";
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
        rawJson.put("course_id", "TEST123");
        JSONObject json = new JSONObject(rawJson);

        this.mockMvc.perform(post("/course/rest")
                            .sessionAttrs(session_atr)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.toString()))
                            .andExpect(status().isOk())
                            .andExpect(content().json("{'success': 'true'}"));

        this.mockMvc.perform(get("/course/rest/loadInfo")
                             .sessionAttrs(session_atr))
                            .andExpect(status().isOk());



    }
}
