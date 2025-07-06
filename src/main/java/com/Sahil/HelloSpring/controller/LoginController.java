package com.Sahil.HelloSpring.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {

    @GetMapping("/login")
    public String logincustomer(@RequestParam("failed") Optional<String> optionalfailedmessage, Model model) {
        if (optionalfailedmessage.isPresent()) { 
            String failedmessage = optionalfailedmessage.get();
            model.addAttribute("message",failedmessage);
        }
        return "login";
    }

}