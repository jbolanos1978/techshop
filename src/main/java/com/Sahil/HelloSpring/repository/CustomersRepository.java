package com.Sahil.HelloSpring.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.Sahil.HelloSpring.model.Customers;

public interface CustomersRepository extends MongoRepository<Customers, String> {
    @Query("SELECT c FROM Customers c WHERE c.cedula LIKE %?1%")
    List<Customers> getContainingCustomer(String word);     
}