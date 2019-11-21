package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.DummyEntity;
import com.org.simplelab.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="")
public class IndexController {

    @GetMapping
    public String toHome(){
        return "redirect:/role";
    }

    @RequestMapping("/test")
    public String test(HttpSession sc, Model model) {
        model.addAttribute("course", DummyEntity.getObj().list_course.get(0));
            return "index";
    }

    @ResponseBody
    @GetMapping("/rest_test")
    public Map<String, String> rest_test(){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("response", "Hello World!");
        return hashMap;
    }

    @GetMapping("/role2")
    @ResponseBody
    @PreAuthorize(SecurityUtils.HAS_TEACHER_AUTHORITY)
    public Object rle2(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @GetMapping("/role1")
    @ResponseBody
    @PreAuthorize(SecurityUtils.HAS_STUDENT_AUTHORITY)
    public Object rle1(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

}
