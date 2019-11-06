package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.DummyEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="/index")
public class IndexController {

    @RequestMapping("/test")
    public String test(HttpSession sc, Model model) {
        model.addAttribute("course", DummyEntity.getObj().list_course.get(0));
            return "index";
    }

    @ResponseBody
    @GetMapping("rest_test")
    public Map<String, String> rest_test(){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("response", "Hello World!");
        return hashMap;
    }

}
