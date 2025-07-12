package com.Sahil.HelloSpring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    @Column(name = "email")
        public String email;

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }   

    @Column(name = "password")
        public String password;

    public Boolean getadmin() {
        return admin;
    }

    public void setadmin(Boolean admin) {
        this.admin = admin;
    } 

    @Column(name = "admin")
        public Boolean admin;

    public Long getfailedattempts() {
        return failedattempts;
    }

    public void setfailedattempts(Long failedattempts) {
        this.failedattempts = failedattempts;
    } 

    @Column(name = "failedattempts")
        public Long failedattempts;

    @Transient
    public String newpassword;

    @Transient
    public String confirmpassword;

}