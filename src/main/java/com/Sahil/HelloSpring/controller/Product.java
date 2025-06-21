package com.Sahil.HelloSpring.controller;

public class Product {

  private long productId;
  private String productDescription;
  private String productImage;

  public long getproductId() {
    return productId;
  }

  public void setproductId(long productId) {
    this.productId = productId;
  }

  public String getproductDescription() {
    return productDescription;
  }

  public void setproductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public String getproductImage() {
    return productImage;
  }

  public void setproductImage(String productImage) {
    this.productImage = productImage;
  }

}