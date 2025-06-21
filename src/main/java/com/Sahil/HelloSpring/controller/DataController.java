package com.Sahil.HelloSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.Sahil.HelloSpring.repository.ProductsRepository;

@Controller
@CrossOrigin(origins = "https://techshop-joaquins-projects-d526bcdf.vercel.app")
public class DataController {

    @Autowired
    private ProductsRepository productRepository;

    @GetMapping("/showproducts")
    public String data(Model model) {
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("product", new Product());
        return "data";
    }

}