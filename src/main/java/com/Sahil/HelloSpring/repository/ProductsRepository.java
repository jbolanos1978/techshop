package com.Sahil.HelloSpring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Sahil.HelloSpring.model.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("SELECT p FROM Products p WHERE p.productDescription LIKE %?1%")
    List<Products> getContainingProduct(String word);     
}