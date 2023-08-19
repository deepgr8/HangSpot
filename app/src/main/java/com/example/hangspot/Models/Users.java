package com.example.hangspot.Models;

public class Users {
   String profileImg;
    String name;
    String email;
    String lastmessage;
    String status;
    String senderId;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Users(String senderId) {
        this.senderId = senderId;
    }


   public Users(){}

    public Users(String profileImg, String name, String email, String lastmessage, String status) {
        this.profileImg = profileImg;
        this.name = name;
        this.email = email;
        this.lastmessage = lastmessage;
        this.status = status;
    }



    public Users(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public String getProfileImg() {
        return profileImg;
    }





    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
