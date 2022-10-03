package com.example.gt.model;

public class Details {

    private String id;
    private String nike;
    private String address;
    private String phone;
    private String email;
    private  String DOB;


    public Details(String id, String nike, String address, String email, String phone,String DOB) {
       this.id=id;
       this.nike=nike;
       this.address=address;
       this.phone=phone;
       this.email=email;
       this.DOB=DOB;


    }

    public Details() {

    }

    public void setId(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getNike() {
        return nike;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDOB() {
        return DOB;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}

