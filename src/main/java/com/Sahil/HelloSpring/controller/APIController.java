package com.Sahil.HelloSpring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Sahil.HelloSpring.model.Products;
import com.Sahil.HelloSpring.repository.ProductsRepository;

@Controller
@RequestMapping("/api")
public class APIController {

    @Autowired
    private ProductsRepository productRepository;

    @GetMapping("/products")
    public List<Products> getProducts(@RequestParam("search") Optional<String> searchParam){
        return searchParam.map( param->productRepository.getContainingProduct(param) )
                .orElse(productRepository.findAll());
    }

    @GetMapping("/products/{productId}" )
    public ResponseEntity<String> readProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.of(productRepository.findById(productId).map(Products::getproductDescription));
    }

    @PostMapping("/products")
    public String addProduct (@ModelAttribute Products product, Model model) {
        Products p = new Products();
        p.setproductId(product.productId);
        p.setproductDescription(product.productDescription);
        p.setproductImage(product.productImage);
        productRepository.save(p);
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("product", new Product());
        return "data";
    }

    @PostMapping("/products/{productId}")
    public String updateProduct (@PathVariable("productId") Long productId, @ModelAttribute Products product, Model model) {
        Optional<Products> optionalproduct = productRepository.findById(productId);
        if (optionalproduct.isPresent()) {
            Products producttoupdate = optionalproduct.get();
            producttoupdate.setproductDescription(product.productDescription);
            producttoupdate.setproductImage(product.productImage);
            productRepository.save(producttoupdate);
        }
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("product", new Product());
        return "data";
    }

    @DeleteMapping("/products/{productId}")
    public String deleteQuote(@PathVariable(value = "productId") Long productId, Model model) {
        productRepository.deleteById(productId);
        model.addAttribute("products",productRepository.findAll());
        model.addAttribute("product", new Product());
        return "data";
    }
}