package com.Sahil.HelloSpring.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "customers")
public class Customers {

    @Id
    @Column(name = "_id")
        public String id;


    public String getcedula() {
        return cedula;
    }

    public void setcedula(String cedula) {
        this.cedula = cedula;
    }

    @Column(name = "cedula")
        public String cedula;

    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "nombre")
        public String nombre;

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "imageUrl")
        public String imageUrl;

    @Column(name = "email")
        public String email;

    @Column(name = "password")
        public String password;

    @Column(name = "admin")
        public Boolean admin;

    @Column(name = "failedattempts")
        public Long failedattempts;

}