package com.Sahil.HelloSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("/cscorner")
    public String hello() {
        return "test";
    }

}