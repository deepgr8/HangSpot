package com.example.hangspot.Models;

public class FriendModel {
    String senderName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String receiverName;

    String key;

    String ImageProfile;
    Boolean Accept , Decline;

    public FriendModel() {

    }
    public FriendModel(String senderName, String receiverName, String imageProfile, Boolean accept, Boolean decline) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        ImageProfile = imageProfile;
        Accept = accept;
        Decline = decline;
    }

    public String getImageProfile() {
        return ImageProfile;
    }

    public void setImageProfile(String imageProfile) {
        ImageProfile = imageProfile;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Boolean getAccept() {
        return Accept;
    }

    public void setAccept(Boolean accept) {
        Accept = accept;
    }

    public Boolean getDecline() {
        return Decline;
    }

    public void setDecline(Boolean decline) {
        Decline = decline;
    }
}
