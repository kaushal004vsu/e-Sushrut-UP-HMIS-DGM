package com.cdac.uphmis.precriptionView.model;

public class LabReportListDetails {


    private String testName;
    private String reqDNo;
    private String reportDate;
    private String hospCode;
    private String hospName;
    private String status_no;
    private String status_covered;

    public LabReportListDetails(String testName, String reqDNo, String reportDate, String hospCode, String hospName, String status_no, String status_covered) {
        this.testName = testName;
        this.reqDNo = reqDNo;
        this.reportDate = reportDate;
        this.hospCode = hospCode;
        this.hospName = hospName;
        this.status_no = status_no;
        this.status_covered = status_covered;
    }


    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getReqDNo() {
        return reqDNo;
    }

    public void setReqDNo(String reqDNo) {
        this.reqDNo = reqDNo;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
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

    public String getStatus_no() {
        return status_no;
    }

    public void setStatus_no(String status_no) {
        this.status_no = status_no;
    }

    public String getStatus_covered() {
        return status_covered;
    }

    public void setStatus_covered(String status_covered) {
        this.status_covered = status_covered;
    }
}
