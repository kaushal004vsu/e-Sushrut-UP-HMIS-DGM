package com.cdac.uphmis.tracker;

public class OrderStatusModel {
    private String tv_status;
    private String tv_orderstatus_time;
    private String status;
    private int image;

    public OrderStatusModel(String tv_status, String tv_orderstatus_time, String status,int image) {
        this.tv_status = tv_status;
        this.tv_orderstatus_time = tv_orderstatus_time;
        this.status = status;
        this.image = image;
    }

    public String getTv_status() {
        return tv_status;
    }

    public void setTv_status(String tv_status) {
        this.tv_status = tv_status;
    }

    public String getTv_orderstatus_time() {
        return tv_orderstatus_time;
    }

    public void setTv_orderstatus_time(String tv_orderstatus_time) {
        this.tv_orderstatus_time = tv_orderstatus_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
