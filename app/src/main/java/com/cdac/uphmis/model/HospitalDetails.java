package com.cdac.uphmis.model;

public class HospitalDetails {
    private String hospCode;
    private String hospName;
    private String OPDTimings;

    public HospitalDetails(String hospCode, String hospName) {
        this.hospCode = hospCode;
        this.hospName = hospName;
    }

    public HospitalDetails(String hospCode, String hospName,String OPDTimings) {
        this.hospCode = hospCode;
        this.hospName = hospName;
        this.OPDTimings = OPDTimings;
    }

    public String getOPDTimings() {
        return OPDTimings;
    }

    public void setOPDTimings(String OPDTimings) {
        this.OPDTimings = OPDTimings;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    @Override
    public String toString() {
        return hospName;
    }
}
