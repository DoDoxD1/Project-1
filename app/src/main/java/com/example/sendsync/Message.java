package com.example.sendsync;

public class Message {
    String Message;
    String senderID;
    long timeStamp;
    String currentTime;

    public Message() {
    }

    public Message(String message, String senderID, long timeStamp, String currentTime) {
        Message = message;
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.currentTime = currentTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
