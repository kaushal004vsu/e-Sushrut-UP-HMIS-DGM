package com.cdac.uphmis.precriptionView.model;

public class PrescriptionListDetails
{
    private String crno;
    private String episodeCode;
    private String deptCode;
    private String visiDate;
    private String hospCode;
    private String patientName;
    private String deptName;
    private String unitName;
    private String visitNo;
    private String patAge;
    private String genderCode;
    private String hospName;
    private String entryDate;

    public PrescriptionListDetails(String crno, String episodeCode, String deptCode, String visiDate, String hospCode, String patientName, String deptName, String unitName, String visitNo, String patAge, String genderCode, String hospName,String entryDate) {
        this.crno = crno;
        this.episodeCode = episodeCode;
        this.deptCode = deptCode;
        this.visiDate = visiDate;
        this.hospCode = hospCode;
        this.patientName = patientName;
        this.deptName = deptName;
        this.unitName = unitName;
        this.visitNo = visitNo;
        this.patAge = patAge;
        this.genderCode = genderCode;
        this.hospName = hospName;
        this.entryDate = entryDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        this.episodeCode = episodeCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getVisiDate() {
        return visiDate;
    }

    public void setVisiDate(String visiDate) {
        this.visiDate = visiDate;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }
}
