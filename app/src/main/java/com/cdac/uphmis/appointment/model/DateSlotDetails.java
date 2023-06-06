package com.cdac.uphmis.appointment.model;

public class DateSlotDetails {
    private String app_date;
    private String t_app;
    private String book_app;
    private String avl_app;

    private String displayDate;


    public DateSlotDetails(String app_date, String t_app, String book_app, String avl_app,String displayDate) {
        this.app_date = app_date;
        this.displayDate = displayDate;
        this.t_app = t_app;
        this.book_app = book_app;
        this.avl_app = avl_app;
    }

    public String getApp_date() {
        return app_date;
    }

    public void setApp_date(String app_date) {
        this.app_date = app_date;
    }

    public String getT_app() {
        return t_app;
    }

    public void setT_app(String t_app) {
        this.t_app = t_app;
    }

    public String getBook_app() {
        return book_app;
    }

    public void setBook_app(String book_app) {
        this.book_app = book_app;
    }

    public String getAvl_app() {
        return avl_app;
    }

    public void setAvl_app(String avl_app) {
        this.avl_app = avl_app;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }
}
