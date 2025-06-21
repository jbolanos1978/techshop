package com.Sahil.HelloSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "https://techshop-joaquins-projects-d526bcdf.vercel.app")
public class testController {

    @GetMapping("/cscorner")
    public String hello() {
        return "test";
    }

}