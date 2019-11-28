package com.org.simplelab;

import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.EquipmentProperty;
import com.org.simplelab.database.entities.User;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.org.simplelab.SpringTestConfig.metadata;

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

    public static User createJunkUser(){
        User u = new User();
        u.setFirstname("UNIT_TEST");
        u.setLastname("UNIT_TEST");
        return u;
    }

    public static Equipment createJunkEquipment(){
        Equipment e = new Equipment();
        e.setDescription(metadata + "DESC");
        e.setName(metadata + "NAME");
        return e;
    }

    public static EquipmentProperty createJunkEquipmentProperty(Equipment parent){
        EquipmentProperty ep = new EquipmentProperty();
        Random rand = new Random();
        ep.setProperty_key(Double.toString(rand.nextDouble()));
        ep.setProperty_value(Double.toString(rand.nextDouble()));
        ep.set_metadata(metadata);
        return ep;
    }

}
