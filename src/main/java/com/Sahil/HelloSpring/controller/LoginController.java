package com.Sahil.HelloSpring.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;


@Controller

public class LoginController {

    @PostMapping("/login")
    public String loginpagepost(@RequestParam("loginmessage") Optional<String> optionalloginmessage, @ModelAttribute LoginCustomer customer, Model model) {
        if (optionalloginmessage.isPresent()) { 
            String loginmessage = optionalloginmessage.get();
            System.out.println(customer.forgotpassword);
            System.out.println(customer.password);
            model.addAttribute("message",loginmessage);
            model.addAttribute("temporarypassword", customer.password);
            model.addAttribute("email", customer.forgotpassword);
            System.out.println("Se fue por POST");
        }
        model.addAttribute("logincustomer", new LoginCustomer());
        return "login";
    }

    @GetMapping("/login")
    public String loginpageget(@RequestParam("loginmessage") Optional<String> optionalloginmessage, @ModelAttribute("password") String password, @ModelAttribute("forgotpassword") String forgotpassword, @ModelAttribute("test") String vtest2, Model model, HttpServletRequest request) {
        if (optionalloginmessage.isPresent()) { 
            String loginmessage = optionalloginmessage.get();
            System.out.println(forgotpassword);
            System.out.println(password);
            String vtest = (String) model.asMap().get("test");
            System.out.println(vtest);
            System.out.println(vtest2);
            model.addAttribute("message",loginmessage);
            model.addAttribute("temporarypassword", password);
            model.addAttribute("email", forgotpassword);
            System.out.println("Se fue por GET");
        }
        model.addAttribute("logincustomer", new LoginCustomer());
        return "login";
    }

}