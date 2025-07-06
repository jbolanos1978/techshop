package com.Sahil.HelloSpring.controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Sahil.HelloSpring.repository.CustomersRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Controller

public class CustomersController {

    @Autowired
    private CustomersRepository customerRepository;

    @Value("${JWT_SECRET}")
    private String jwtsecret;
    private Key getSigningKey() {
        byte[] keyBytes = jwtsecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @GetMapping("/showcustomers")
    public String showcustomers(@RequestParam("customercedula") Optional<String> optionalcustomercedula, Model model, @CookieValue(value = "techshopsitetoken", required = false) String cookieValue) {
        if (cookieValue != null) {
            Key vkey = getSigningKey(); 
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(vkey)
                .build()
                .parseClaimsJws(cookieValue);
            Claims claims = jwsClaims.getBody(); 
            if (optionalcustomercedula.isPresent()) { 
                String customercedula = optionalcustomercedula.get();
                if (claims.get("userId").toString().equals(customercedula)) { 
                    model.addAttribute("customers",customerRepository.findAll());
                    model.addAttribute("temporarypassword","");
                    return "customers";
                }
                else {
                    return "login";
                }
            }
            else {
                return "login";
            }
        }
        else {
            return "login";
        }
    }

}