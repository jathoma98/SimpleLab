package com.org.simplelab;

import com.org.simplelab.database.UserDB;
import com.org.simplelab.database.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing methods relating to Login and Signup functions.
 * Refer to https://spring.io/guides/gs/testing-web/ for info on how to write requests for testing.
 */
@AutoConfigureMockMvc
public class LoginAndSignupTests extends SpringTestConfig{

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorldRest() throws Exception{
        this.mockMvc.perform(get("/rest_test"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"response\": \"Hello World!\"}"));

    }

    @Test
    public void testLogin() throws Exception{
        User user = new User();
        user._metadata = metadata;
        user.setUsername(new StringBuilder().append("Login auth test user -- ").append(metadata).toString());
        user.setPassword("pass");

        userDB.insertUser(user);


        //test right username+pass
        this.mockMvc.perform(post("/login")
                             .param("userName", user.getUsername())
                             .param("password", "pass"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": \"true\"}"));

        //test wrong username
        this.mockMvc.perform(post("/login")
                .param("userName", "probs a wrong username")
                .param("password", "pass"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": \"false\"}"));

        //test wrong password
        this.mockMvc.perform(post("/login")
                .param("userName", user.getUsername())
                .param("password", "passwrong"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\": \"false\"}"));


    }



}
