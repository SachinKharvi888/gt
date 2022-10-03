package com.example.gt.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String messageid;
    private boolean isseen;
    private int feeling = -1;
    private  Long date;


    public Chat(String sender, String receiver, String message, boolean isseen,String messageid) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.messageid=messageid;

    }

    public Chat() {
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getMessageId() {
        return messageid;
    }

    public void setMessageId(String messageId) {
        this.messageid = messageid;
    }


    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }


    public String getId() {
        return null;
    }
}

