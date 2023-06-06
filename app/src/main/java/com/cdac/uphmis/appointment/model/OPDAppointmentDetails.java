package com.cdac.uphmis.appointment.model;

import java.io.Serializable;

public class OPDAppointmentDetails implements Serializable {
    private String appointmentForId;
    private String patFirstName;
    private String patMiddleName;
    private String patLastName;
    private String patGuardianName;
    private String patSpouseName;
    private String patDOB;
    private String appointmentDate;
    private String emailId;
    private String mobileNo;
    private String appointmentTime;
    private String appointmentStatus;
    private String slotType;
    private String remarks;
    private String appointmentTypeId;
    private String appointmentMode;
    private String patAgeUnit;
    private String patAge;
    private String patGenderCode;
    private String allActualParameterId;
    private String shiftId;
    private String slotST;
    private String slotET;
    private String actualParameterReferenceId;
    private String shiftST;
    private String shiftET;
    private String hcode;
    private String deptUnitCode;
    private String deptUnitName;
    private String deptLocation;
    private String patCrNo;


    private String tariffId;
    private String charge;

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    String testname, labname, reqdate, reqdno, testcode, labcode,
             episodecode, preffereddate,hospName;

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getLabname() {
        return labname;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getReqdno() {
        return reqdno;
    }

    public void setReqdno(String reqdno) {
        this.reqdno = reqdno;
    }

    public String getTestcode() {
        return testcode;
    }

    public void setTestcode(String testcode) {
        this.testcode = testcode;
    }

    public String getLabcode() {
        return labcode;
    }

    public void setLabcode(String labcode) {
        this.labcode = labcode;
    }


    public String getEpisodecode() {
        return episodecode;
    }

    public void setEpisodecode(String episodecode) {
        this.episodecode = episodecode;
    }



    public String getPreffereddate() {
        return preffereddate;
    }

    public void setPreffereddate(String preffereddate) {
        this.preffereddate = preffereddate;
    }


    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public OPDAppointmentDetails(String appointmentForId, String patFirstName, String patMiddleName, String patLastName, String patGuardianName, String patSpouseName, String patDOB, String appointmentDate, String emailId, String mobileNo, String appointmentTime, String appointmentStatus, String slotType, String remarks, String appointmentTypeId, String appointmentMode, String patAgeUnit, String patAge, String patGenderCode, String allActualParameterId, String shiftId, String slotST, String slotET, String actualParameterReferenceId, String shiftST, String shiftET, String hcode, String deptUnitCode, String deptUnitName, String deptLocation, String patCrNo) {
        this.appointmentForId = appointmentForId;
        this.patFirstName = patFirstName;
        this.patMiddleName = patMiddleName;
        this.patLastName = patLastName;
        this.patGuardianName = patGuardianName;
        this.patSpouseName = patSpouseName;
        this.patDOB = patDOB;
        this.appointmentDate = appointmentDate;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
        this.slotType = slotType;
        this.remarks = remarks;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentMode = appointmentMode;
        this.patAgeUnit = patAgeUnit;
        this.patAge = patAge;
        this.patGenderCode = patGenderCode;
        this.allActualParameterId = allActualParameterId;
        this.shiftId = shiftId;
        this.slotST = slotST;
        this.slotET = slotET;
        this.actualParameterReferenceId = actualParameterReferenceId;
        this.shiftST = shiftST;
        this.shiftET = shiftET;
        this.hcode = hcode;
        this.deptUnitCode = deptUnitCode;
        this.deptUnitName = deptUnitName;
        this.deptLocation = deptLocation;
        this.patCrNo = patCrNo;
    }

    public OPDAppointmentDetails() {
    }

    public String getAppointmentForId() {
        return appointmentForId;
    }

    public void setAppointmentForId(String appointmentForId) {
        this.appointmentForId = appointmentForId;
    }

    public String getPatFirstName() {
        return patFirstName;
    }

    public void setPatFirstName(String patFirstName) {
        this.patFirstName = patFirstName;
    }

    public String getPatMiddleName() {
        return patMiddleName;
    }

    public void setPatMiddleName(String patMiddleName) {
        this.patMiddleName = patMiddleName;
    }

    public String getPatLastName() {
        return patLastName;
    }

    public void setPatLastName(String patLastName) {
        this.patLastName = patLastName;
    }

    public String getPatGuardianName() {
        return patGuardianName;
    }

    public void setPatGuardianName(String patGuardianName) {
        this.patGuardianName = patGuardianName;
    }

    public String getPatSpouseName() {
        return patSpouseName;
    }

    public void setPatSpouseName(String patSpouseName) {
        this.patSpouseName = patSpouseName;
    }

    public String getPatDOB() {
        return patDOB;
    }

    public void setPatDOB(String patDOB) {
        this.patDOB = patDOB;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(String appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public String getAppointmentMode() {
        return appointmentMode;
    }

    public void setAppointmentMode(String appointmentMode) {
        this.appointmentMode = appointmentMode;
    }

    public String getPatAgeUnit() {
        return patAgeUnit;
    }

    public void setPatAgeUnit(String patAgeUnit) {
        this.patAgeUnit = patAgeUnit;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    public String getPatGenderCode() {
        return patGenderCode;
    }

    public void setPatGenderCode(String patGenderCode) {
        this.patGenderCode = patGenderCode;
    }

    public String getAllActualParameterId() {
        return allActualParameterId;
    }

    public void setAllActualParameterId(String allActualParameterId) {
        this.allActualParameterId = allActualParameterId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getSlotST() {
        return slotST;
    }

    public void setSlotST(String slotST) {
        this.slotST = slotST;
    }

    public String getSlotET() {
        return slotET;
    }

    public void setSlotET(String slotET) {
        this.slotET = slotET;
    }

    public String getActualParameterReferenceId() {
        return actualParameterReferenceId;
    }

    public void setActualParameterReferenceId(String actualParameterReferenceId) {
        this.actualParameterReferenceId = actualParameterReferenceId;
    }

    public String getShiftST() {
        return shiftST;
    }

    public void setShiftST(String shiftST) {
        this.shiftST = shiftST;
    }

    public String getShiftET() {
        return shiftET;
    }

    public void setShiftET(String shiftET) {
        this.shiftET = shiftET;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
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

    public String getDeptLocation() {
        return deptLocation;
    }

    public void setDeptLocation(String deptLocation) {
        this.deptLocation = deptLocation;
    }

    public String getPatCrNo() {
        return patCrNo;
    }

    public void setPatCrNo(String patCrNo) {
        this.patCrNo = patCrNo;
    }
}

