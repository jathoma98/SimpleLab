package com.org.simplelab;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    public static void login(MockMvc mvc) throws Exception{
        mvc.perform(post("/login")
                .param("userName", "UNIT_TEST")
                .param("password", "UNIT_TEST"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'success': 'true'}"));
    }

    public static Principal getUnitTestPrincipal(){
        return new Principal() {
            @Override
            public String getName() {
                return "UNIT_TEST";
            }
        };
    }

}
