package com.daicy.crawler.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FreeMakerController {

    @RequestMapping("/test")
    public String testFreemarker(ModelMap modelMap) {
        modelMap.addAttribute("msg", "Hello dalaoyang , this is freemarker");
        return "freemarker";
    }

}