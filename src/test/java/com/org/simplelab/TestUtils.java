package com.org.simplelab;

import com.org.simplelab.database.entities.sql.*;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.org.simplelab.SpringTestConfig.metadata;

@Component
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
        u.setUsername(metadata);
        u.setPassword(metadata);
        u.setInstitution(metadata);
        u.setRole("teacher");
        u.setEmail(metadata);
        u.setQuestion(metadata);
        u.setAnswer(metadata);
        u.setFirstname("UNIT_TEST");
        u.setLastname("UNIT_TEST");
        return u;
    }

    public static Equipment createJunkEquipment(){
        Equipment e = new Equipment();
        e.setType(metadata + "DESC");
        e.setName(metadata + "NAME");
        e.setCreator(createJunkUser());
        return e;
    }

    public static EquipmentProperty createJunkEquipmentProperty(Equipment parent){
        EquipmentProperty ep = new EquipmentProperty();
        Random rand = new Random();
        ep.setPropertyKey(Double.toString(rand.nextDouble()));
        ep.setPropertyValue(Double.toString(rand.nextDouble()));
        ep.set_metadata(metadata);
        ep.setParentEquipment(parent);
        return ep;
    }


    public static Equipment createJunkEquipmentWithProperties(int numProperties){
        Equipment e = createJunkEquipment();
        e.setCreator(createJunkUser());
        for (int i = 0; i < numProperties; i++){
            e.getProperties().add(createJunkEquipmentProperty(e));
        }
        return e;
    }

    public static Lab createJunkLab(){
        Lab l = new Lab();
        Random r = new Random();
        String random  = Double.toString(r.nextDouble());
        l.setDescription(metadata + random);
        l.set_metadata(metadata + random);
        l.setName(metadata + "JUNK LAB" + random);
        l.setCreator(createJunkUser());
        return l;
    }

    public static List<Step> createJunkSteps(int numSteps, Lab source){
        List<Step> steps = new ArrayList<>();
        User c = createJunkUser();
        Equipment tar = createJunkEquipment();
        for (int i = 0; i < numSteps; i++){
            Step step = new Step();
            step.setTargetObject(tar);
            step.setLab(source);
            step.setStepNum(i+1);
            //step.setCreator(c);
            steps.add(step);
        }
        return steps;
    }

    public static Lab createJunkLabWithSteps(int numSteps){
        Lab l = createJunkLab();
        l.getEquipments().add(createJunkEquipmentWithProperties(numSteps));
        l.setSteps(createJunkSteps(numSteps, l));
        return l;
    }
}
