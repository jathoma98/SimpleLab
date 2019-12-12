package com.org.simplelab.controllers;

import com.org.simplelab.restcontrollers.rro.RRO;
import com.org.simplelab.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path="")
public class IndexController extends BaseController {

    @GetMapping
    public String toHome(){
        return "redirect:/role";
    }

    @GetMapping("/testRRO")
    @ResponseBody
    public RRO rro_test() {
        RRO rro = new RRO();
        rro.setSuccess(true);
        return rro;
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
