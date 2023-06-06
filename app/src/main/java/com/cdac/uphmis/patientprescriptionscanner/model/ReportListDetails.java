package com.cdac.uphmis.patientprescriptionscanner.model;

public class ReportListDetails {
    private String name;
    private String reqNo;
    private String date;

    public ReportListDetails(String name, String reqNo, String date) {
        this.name = name;
        this.reqNo = reqNo;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
