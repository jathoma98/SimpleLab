package com.org.simplelab.controllers;

import com.org.simplelab.database.entities.DummyEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/")
public class IndexController {
    /**
     * Web index page request
     * @return
     */
    @RequestMapping("")
    public String root() {
        return "redirect:/login";
    }

    @RequestMapping("test")
    public String test(HttpSession sc, Model model) {
        model.addAttribute("course", DummyEntity.getObj().list_course.get(0));
            return "index";
    }

}
