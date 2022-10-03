package com.example.gt.model;

public class grpchatmodel {

    private String sender;
    private String grpid;
    private String message;
    private String messageid;
    private String name;



    public grpchatmodel(String sender, String grpid, String message,String messageid,String name) {
        this.sender = sender;
        this.grpid = grpid;
        this.message = message;
        this.messageid=messageid;
        this.name=name;

    }

    public grpchatmodel(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGrpid(String grpid) {
        this.grpid = grpid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGrpid() {
        return grpid;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageid() {
        return messageid;
    }

}
