package com.cdac.uphmis.model;

import java.io.Serializable;

/**
 * Created by sudeep on 20-12-2018.
 */

public class SelectCrDetails implements Serializable {
    private String crno;
    private String mobileNo;
    private String patName;
    private String patientHasWallet;

    public SelectCrDetails(String crno, String mobileNo, String patName, String patientHasWallet) {
        this.crno = crno;
        this.mobileNo = mobileNo;
        this.patName = patName;
        this.patientHasWallet = patientHasWallet;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatientHasWallet() {
        return patientHasWallet;
    }

    public void setPatientHasWallet(String patientHasWallet) {
        this.patientHasWallet = patientHasWallet;
    }

    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }
}
