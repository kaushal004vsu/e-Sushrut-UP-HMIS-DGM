package com.cdac.uphmis.appointment.model;




public class DateDetails {
    private String date;
    private String displayDate;
    private String slotsAvailable;


    public DateDetails(String date, String displayDate, String slotsAvailable) {
        this.date = date;
        this.displayDate=displayDate;
        this.slotsAvailable = slotsAvailable;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlotsAvailable() {
        return slotsAvailable;
    }

    public void setSlotsAvailable(String slotsAvailable) {
        this.slotsAvailable = slotsAvailable;
    }



}
