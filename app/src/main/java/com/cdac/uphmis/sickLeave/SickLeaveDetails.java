package com.cdac.uphmis.sickLeave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SickLeaveDetails {


    @SerializedName("SICK_END")
    @Expose
    private String sickEnd;
    @SerializedName("PRINT")
    @Expose
    private String print;
    @SerializedName("CURRENT_STATUS")
    @Expose
    private String currentStatus;
    @SerializedName("REQUEST_DATE")
    @Expose
    private String requestDate;
    @SerializedName("CATEGORY")
    @Expose
    private String category;
    @SerializedName("CHEBOXVAL")
    @Expose
    private String cheboxval;
    @SerializedName("DEPARTMENT")
    @Expose
    private String department;
    @SerializedName("SICK_START")
    @Expose
    private String sickStart;
    @SerializedName("SICK_PERIOD")
    @Expose
    private String sickPeriod;
    @SerializedName("SICK_TYPE")
    @Expose
    private String sickType;

    public String getSickEnd() {
        return sickEnd;
    }

    public void setSickEnd(String sickEnd) {
        this.sickEnd = sickEnd;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCheboxval() {
        return cheboxval;
    }

    public void setCheboxval(String cheboxval) {
        this.cheboxval = cheboxval;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSickStart() {
        return sickStart;
    }

    public void setSickStart(String sickStart) {
        this.sickStart = sickStart;
    }

    public String getSickPeriod() {
        return sickPeriod;
    }

    public void setSickPeriod(String sickPeriod) {
        this.sickPeriod = sickPeriod;
    }

    public String getSickType() {
        return sickType;
    }

    public void setSickType(String sickType) {
        this.sickType = sickType;
    }




}


