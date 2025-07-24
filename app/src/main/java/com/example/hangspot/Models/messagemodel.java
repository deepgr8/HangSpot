package com.example.hangspot.Models;

public class messagemodel {
    String username,message,messageid;
    String receiveruser;
    Long timestamp;

    public messagemodel(String username,String receiveruser, String message, Long timestamp) {
        this.username = username;
        this.receiveruser=receiveruser;
        this.message = message;
        this.timestamp = timestamp;
    }
    public messagemodel(){

    }

    public messagemodel(String username, String message) {
        this.username = username;
        this.messageid = message;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
