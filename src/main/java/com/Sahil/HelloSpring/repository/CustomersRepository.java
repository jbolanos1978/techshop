package com.Sahil.HelloSpring.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.Sahil.HelloSpring.model.Customers;

public interface CustomersRepository extends MongoRepository<Customers, String> {
    @Query("{ 'nombre' : { $regex: /?0/i } }")
    List<Customers> getContainingCustomer(String nombre); 
    @Query("{ 'cedula' : ?0 }")
    Customers findByCedula(String cedula);   
    
}