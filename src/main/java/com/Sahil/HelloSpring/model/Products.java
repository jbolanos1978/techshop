package com.Sahil.HelloSpring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "products")
public class Products {

    public Long getproductId() {
        return productId;
    }

    public void setproductId(Long productId) {
        this.productId = productId;
    }

    @Id
    @Column(name = "productId")
        public Long productId;

    public String getproductDescription() {
        return productDescription;
    }

    public void setproductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Column(name = "productDescription")
        public String productDescription;

    public String getproductImage() {
        return productImage;
    }

    public void setproductImage(String productImage) {
        this.productImage = productImage;
    }

    @Column(name = "productImage")
        public String productImage;

}