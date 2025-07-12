package com.Sahil.HelloSpring.controller;

public class LoginCustomer {

  public String cedula;
  public String password;
  public String newpassword;
  public String confirmpassword;
  public String forgotpassword;
  public String URL;

  public String getcedula() {
    return cedula;
  }

  public String getpassword() {
    return password;
  }

  public String getnewpassword() {
    return newpassword;
  }

  public String getconfirmpassword() {
    return confirmpassword;
  }

  public String getforgotpassword() {
    return forgotpassword;
  }

   public String getURL() {
    return URL;
  }

  public void setcedula(String cedula) {
    this.cedula = cedula;
  }

  public void setpassword(String password) {
    this.password = password;
  }

  public void setnewpassword(String newpassword) {
    this.newpassword = newpassword;
  }

  public void setconfirmpassword(String confirmpassword) {
    this.confirmpassword = confirmpassword;
  }

  public void setforgotpassword(String forgotpassword) {
    this.forgotpassword = forgotpassword;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }
  
}
