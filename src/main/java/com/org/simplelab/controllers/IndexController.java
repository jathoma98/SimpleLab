package com.org.simplelab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String test() {
        return "index";
    }

}
