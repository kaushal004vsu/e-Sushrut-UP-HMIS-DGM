package com.cdac.uphmis.lpstatus.model;

public class LPSTatusDetails {
    String s_no;
    String statuss ;
    String item_name;
    String date ;
    String app_qty;
    String hosp_name ;

    public LPSTatusDetails(String s_no, String statuss, String item_name, String date, String app_qty, String hosp_name) {
        this.s_no = s_no;
        this.statuss = statuss;
        this.item_name = item_name;
        this.date = date;
        this.app_qty = app_qty;
        this.hosp_name = hosp_name;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApp_qty() {
        return app_qty;
    }

    public void setApp_qty(String app_qty) {
        this.app_qty = app_qty;
    }

    public String getHosp_name() {
        return hosp_name;
    }

    public void setHosp_name(String hosp_name) {
        this.hosp_name = hosp_name;
    }
}
