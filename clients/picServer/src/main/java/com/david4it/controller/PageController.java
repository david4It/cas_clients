package com.david4it.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PageController {
    @RequestMapping("/{page}")
    public ModelAndView page(@PathVariable("page") String page) {
        String[] li = page.split(":");
        if (li.length > 1) {
            page = page.replaceAll(":", "/");
        }
        return new ModelAndView(page);
    }
}
