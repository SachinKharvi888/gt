package com.example.gt.model;

public class grpname {
    private String grpname;
    private String id;
    private String admin;


    public grpname(String name, String id, String admin) {
        this.grpname = name;
        this.id = id;
        this.admin = admin;
    }

    public grpname() {

    }

    public String getGrpname() {
        return grpname;
    }

    public void setGrpname(String grpname) {
        this.grpname = grpname;
    }

    public String getAdmin() {
        return admin;
    }

    public String getId() {
        return id;
    }

    public void setAdmin(String admin) {
        this.admin = admin;

    }

    public void setId(String id) {
        this.id = id;
    }
}