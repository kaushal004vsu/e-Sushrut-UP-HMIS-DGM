package com.cdac.uphmis.covid19.model;

import java.io.Serializable;

public class ScreeningDetails implements Serializable {
    private String requestId;
    private String crno;
    private String scrResponse;
    private String consName;
    private String deptUnitCode;
    private String deptUnitName;
    private String requestStatus;
    private String patMobileNo;
    private String consMobileNo;
    private String patDocs;
    private String docMessage;
    private String constId;
    private String patName;
    private String patAge;
    private String patGender;
    private String email;
    private String remarks;
    private String patWeight;
    private String patHeight;
    private String medications;
    private String pastdiagnosis;
    private String pastAllergies;
    private String userId;
    private String stateCode;
    private String districtCode;

    private String apptDeptUnit;
    private String guardianName;
    private String patientToken;
    private String hospCode;
    private String hospName;
    private String OPDTimings="";


    public ScreeningDetails(String requestId, String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode ,String apptDeptUnit,String guardianName,String patientToken,String hospCode,String hospName,String OPDTimings) {
        this.requestId = requestId;
        this.crno = crno;
        this.scrResponse = scrResponse;
        this.consName = consName;
        this.deptUnitCode = deptUnitCode;
        this.deptUnitName = deptUnitName;
        this.requestStatus = requestStatus;
        this.patMobileNo = patMobileNo;
        this.consMobileNo = consMobileNo;
        this.patDocs = patDocs;
        this.docMessage = docMessage;
        this.constId = constId;
        this.patName = patName;
        this.patAge = patAge;
        this.patGender = patGender;
        this.email = email;
        this.remarks = remarks;
        this.patWeight = patWeight;
        this.patHeight = patHeight;
        this.medications = medications;
        this.pastdiagnosis = pastdiagnosis;
        this.pastAllergies = pastAllergies;
        this.userId = userId;
        this.stateCode = stateCode;
        this.districtCode = districtCode;

        this.apptDeptUnit=apptDeptUnit;
        this.guardianName=guardianName;
        this.patientToken=patientToken;
        this.hospCode=hospCode;
        this.hospName=hospName;
        this.OPDTimings=OPDTimings;


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

    public String getApptDeptUnit() {
        return apptDeptUnit;
    }

    public void setApptDeptUnit(String apptDeptUnit) {
        this.apptDeptUnit = apptDeptUnit;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getScrResponse() {
        return scrResponse;
    }

    public void setScrResponse(String scrResponse) {
        this.scrResponse = scrResponse;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getDeptUnitCode() {
        return deptUnitCode;
    }

    public void setDeptUnitCode(String deptUnitCode) {
        this.deptUnitCode = deptUnitCode;
    }

    public String getDeptUnitName() {
        return deptUnitName;
    }

    public void setDeptUnitName(String deptUnitName) {
        this.deptUnitName = deptUnitName;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getPatMobileNo() {
        return patMobileNo;
    }

    public void setPatMobileNo(String patMobileNo) {
        this.patMobileNo = patMobileNo;
    }

    public String getConsMobileNo() {
        return consMobileNo;
    }

    public void setConsMobileNo(String consMobileNo) {
        this.consMobileNo = consMobileNo;
    }

    public String getPatDocs() {
        return patDocs;
    }

    public void setPatDocs(String patDocs) {
        this.patDocs = patDocs;
    }

    public String getDocMessage() {
        return docMessage;
    }

    public void setDocMessage(String docMessage) {
        this.docMessage = docMessage;
    }

    public String getConstId() {
        return constId;
    }

    public void setConstId(String constId) {
        this.constId = constId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    public String getPatGender() {
        return patGender;
    }

    public void setPatGender(String patGender) {
        this.patGender = patGender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPatWeight() {
        return patWeight;
    }

    public void setPatWeight(String patWeight) {
        this.patWeight = patWeight;
    }

    public String getPatHeight() {
        return patHeight;
    }

    public void setPatHeight(String patHeight) {
        this.patHeight = patHeight;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getPastdiagnosis() {
        return pastdiagnosis;
    }

    public void setPastdiagnosis(String pastdiagnosis) {
        this.pastdiagnosis = pastdiagnosis;
    }

    public String getPastAllergies() {
        return pastAllergies;
    }

    public void setPastAllergies(String pastAllergies) {
        this.pastAllergies = pastAllergies;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }
}
