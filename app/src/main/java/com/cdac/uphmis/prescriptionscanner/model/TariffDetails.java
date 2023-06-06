package com.cdac.uphmis.prescriptionscanner.model;

/**
 * Created by sudeep on 19-09-2018.
 */

public class TariffDetails {
    private String unitName;
    private String visitNo;
    private String crNo;
    private String deptCode;
    private String deptName;
    private String episodeCode;
    private String episodeDate;
    private String patAddress;
    private String hospitalCode;
    private String unitCode;
    private String isUploaded;

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
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

    public String getCrNo() {
        return crNo;
    }

    public void setCrNo(String crNo) {
        this.crNo = crNo;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        this.episodeCode = episodeCode;
    }

    public String getEpisodeDate() {
        return episodeDate;
    }

    public void setEpisodeDate(String episodeDate) {
        this.episodeDate = episodeDate;
    }

    public String getPatAddress() {
        return patAddress;
    }

    public void setPatAddress(String patAddress) {
        this.patAddress = patAddress;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    @Override
    public String toString() {
        return deptName + " (" + unitName + ") " + episodeDate.substring(0, 10);
    }

    private String patName;

    public TariffDetails(String unitName, String visitNo, String crNo, String deptCode, String deptName, String episodeCode, String episodeDate, String patAddress, String hospitalCode, String unitCode, String patName, String isUploaded) {

        this.unitName = unitName;
        this.visitNo = visitNo;
        this.crNo = crNo;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.episodeCode = episodeCode;
        this.episodeDate = episodeDate;
        this.patAddress = patAddress;
        this.hospitalCode = hospitalCode;
        this.unitCode = unitCode;
        this.patName = patName;
        this.isUploaded = isUploaded;
    }

}
