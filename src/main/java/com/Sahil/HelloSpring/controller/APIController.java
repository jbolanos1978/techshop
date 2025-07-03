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
import com.Sahil.HelloSpring.model.Customers;
import com.Sahil.HelloSpring.repository.ProductsRepository;
import com.Sahil.HelloSpring.repository.CustomersRepository;

@Controller

@RequestMapping("/api")
public class APIController {

    @Autowired
    private CustomersRepository customerRepository;
    @Autowired
    private ProductsRepository productRepository;

    @GetMapping("/products")
    public String getProducts (@RequestParam("search") Optional<String> searchParam, Model model){
        List<Products> vsearch = searchParam.map( param->productRepository.getContainingProduct(param) )
                .orElse(productRepository.findAll());
        model.addAttribute("products",vsearch);
        //model.addAttribute("product", new Product());
        return "data";
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<String> readProduct (@PathVariable("productId") Long productId) {
        return ResponseEntity.of(productRepository.findById(productId).map(Products::getproductDescription));
    }

    @PostMapping("/products")
    public String addProduct (@ModelAttribute Products product, Model model) {
        Products producttoinsert = new Products();
        producttoinsert.setproductId(product.productId);
        producttoinsert.setproductDescription(product.productDescription);
        producttoinsert.setproductImage(product.productImage);
        productRepository.save(producttoinsert);
        model.addAttribute("products",productRepository.findAll());
        //model.addAttribute("product", new Product());
        return "data";
    }

    @PostMapping("/products/{productId}")
    public String updateProduct (@PathVariable(value = "productId") Long productId, @ModelAttribute Products product, Model model) {
        Optional<Products> optionalproduct = productRepository.findById(productId);
        if (optionalproduct.isPresent()) {
            Products producttoupdate = optionalproduct.get();
            producttoupdate.setproductDescription(product.productDescription);
            producttoupdate.setproductImage(product.productImage);
            productRepository.save(producttoupdate);
        }
        model.addAttribute("products",productRepository.findAll());
        //model.addAttribute("product", new Product());
        return "data";
    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct (@PathVariable(value = "productId") Long productId, Model model) {
        productRepository.deleteById(productId);
        model.addAttribute("products",productRepository.findAll());
        //model.addAttribute("product", new Product());
        return "data";
    }

    @GetMapping("/customers")
    public String getCustomers (@RequestParam("search") Optional<String> searchParam, Model model){
        List<Customers> vsearch = searchParam.map( param->customerRepository.getContainingCustomer(param) )
                .orElse(customerRepository.findAll());
        model.addAttribute("customers",vsearch);
        return "data";
    }

    @GetMapping("/customers/{Id}")
    public ResponseEntity<String> readCustomer (@PathVariable(value = "Id") String Id) {
        return ResponseEntity.of(customerRepository.findById(Id).map(Customers::getnombre));
    }

    @PostMapping("/customers/{Id}")
    public String updateCustomer (@PathVariable(value = "Id") String Id, @ModelAttribute Customers customer, Model model) {
        Optional<Customers> optionalcustomer = customerRepository.findById(Id);
        if (optionalcustomer.isPresent()) {
            Customers customertoupdate = optionalcustomer.get();
            customertoupdate.setnombre(customer.nombre);
            customertoupdate.setimageUrl(customer.imageUrl);
            customerRepository.save(customertoupdate);
            System.out.println("SI Paso por Save...");
            System.out.println("Nombre:" + customer.nombre);
            System.out.println("imageUrl:" + customer.imageUrl);
        }
        else {
            System.out.println("NO Paso por Save..." + Id);
        }
        model.addAttribute("customers",customerRepository.findAll());
        return "customers";
    }

    @DeleteMapping("/customers/{Id}")
    public String deleteCustomer (@PathVariable(value = "Id") String Id, Model model) {
        customerRepository.deleteById(Id);
        model.addAttribute("customers",customerRepository.findAll());
        return "customers";
    }
}