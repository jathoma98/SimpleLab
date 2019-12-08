package com.org.simplelab;

import com.org.simplelab.database.entities.sql.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing methods relating to Login and Signup functions.
 * Refer to https://spring.io/guides/gs/testing-web/ for info on how to write requests for testing.
 */
@Transactional
public class LoginAndSignupTests extends SpringMockMVCTestConfig{

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testLogin() throws Exception{
        User user = new User();
        String password = "password";
        user._metadata = metadata;
        user.setUsername(new StringBuilder().append("log").append(metadata).toString());
        user.setPassword(password);

        userDB.insert(user);


        //test right username+pass
        this.mockMvc.perform(post("/login")
                             .param("userName", user.getUsername())
                             .param("password", password))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": true}"));

        //test wrong username
        this.mockMvc.perform(post("/login")
                .param("userName", "probs a wrong username")
                .param("password", password))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": false}"));

        //test wrong password
        this.mockMvc.perform(post("/login")
                .param("userName", user.getUsername())
                .param("password", "passwrong"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": false}"));

        User del = userDB.findUser(user.getUsername());
        userDB.deleteUser(del);

    }

    @Test
    public void testSignup() throws Exception{
        String username = metadata;
        String password = "password";
        Map<String, String> rawJson = new HashMap<>();
        rawJson.put("userName", username);
        rawJson.put("institution", "test school");
        rawJson.put("sp_password", password);
        rawJson.put("email", "mail@mail.com");
        rawJson.put("sp_re_password", password);
        rawJson.put("question", "this si a question");
        rawJson.put("answer", "this is the answer");
        rawJson.put("identity", "teacher");

        JSONObject json = new JSONObject(rawJson);
        System.out.println(json.toString());


        //test valid signup
        this.mockMvc.perform(post("/signup/submit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': true}"));

        userDB.deleteUser(username);


    }




}
