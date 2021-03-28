package com.capstone.kimbaro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/view")
public class Page_Contorller {
    @GetMapping(value = "/screen")
    public String monitoring_page(Model model) {
        return "/group_screen/index";
    }

}
