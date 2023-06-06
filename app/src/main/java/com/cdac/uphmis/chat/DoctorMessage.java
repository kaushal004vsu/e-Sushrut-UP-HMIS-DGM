package com.cdac.uphmis.chat;

/*public class DoctorMessage {
    private boolean isMe;
    private String message;
    private String name;
    private String dateTime;


    public DoctorMessage(boolean isMe, String message, String name, String dateTime) {
        this.isMe = isMe;
        this.message = message;
        this.name = name;
        this.dateTime = dateTime;
    }


    public boolean getisMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}*/
public class DoctorMessage {
    private String message;
    private String userName;
    private String timeStamp;
    private boolean isMe;

    public DoctorMessage(String message, String userName, String timeStamp, boolean isMe) {
        this.message = message;
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}