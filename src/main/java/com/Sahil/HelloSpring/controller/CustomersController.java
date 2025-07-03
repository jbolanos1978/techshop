package com.Sahil.HelloSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Sahil.HelloSpring.repository.CustomersRepository;

@Controller

public class CustomersController {

    @Autowired
    private CustomersRepository customerRepository;

    @GetMapping("/showcustomers")
    public String data(Model model) {
        model.addAttribute("customers",customerRepository.findAll());
        return "customers";
    }

}