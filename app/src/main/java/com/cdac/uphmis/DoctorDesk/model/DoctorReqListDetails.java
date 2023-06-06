package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DoctorReqListDetails implements Serializable {
    @SerializedName("requestID")
    private String requestID;

    @SerializedName("CRNo")
    private String CRNo;

    @SerializedName("scrResponse")
    private String scrResponse;

    @SerializedName("consName")
    private String consName;

    @SerializedName("deptUnitCode")
    private String deptUnitCode;

    @SerializedName("deptUnitName")
    private String deptUnitName;

    @SerializedName("hospCode")
    private String hospCode;

    @SerializedName("requestStatus")
    private String requestStatus;

    @SerializedName("patMobileNo")
    private String patMobileNo;

    @SerializedName("consMobileNo")
    private String consMobileNo;

    @SerializedName("patDocs")
    private String patDocs;

    @SerializedName("docMessage")
    private String docMessage;

    @SerializedName("cnsltntId")
    private String cnsltntId;

    @SerializedName("patName")
    private String patName;

    @SerializedName("patAge")
    private String patAge;

    @SerializedName("patGender")
    private String patGender;

    @SerializedName("rmrks")
    private String rmrks;

    @SerializedName("email")
    private String email;

    @SerializedName("pVisitdate")
    private String pVisitdate;

    @SerializedName("patWeight")
    private String patWeight;

    @SerializedName("patHeight")
    private String patHeight;

    @SerializedName("patMedication")
    private String patMedication;

    @SerializedName("patPastDiagnosis")
    private String patPastDiagnosis;

    @SerializedName("patAllergies")
    private String patAllergies;

    @SerializedName("departmentId")
    private String departmentId = "";

    @SerializedName("patientCategory")
    private String patientCategory = "-1";

    @SerializedName("hcode")
    private String hcode = "";

    @SerializedName("patVisitNo")
    private String patVisitNo = "1";

    @SerializedName("roomId")
    private String roomId = "0";

    @SerializedName("departmentName")
    private String departmentName;


    @SerializedName("isDocUploaded")
    private String isDocUploaded;

    @SerializedName("patientToken")
    private String patientToken;

    @SerializedName("doctorToken")
    private String doctorToken;


    //8 new parameters added in appointment based system.
    @SerializedName("appointmentNo")
    private String appointmentNo;

    @SerializedName("apptStartTime")
    private String apptStartTime;

    @SerializedName("apptEndTime")
    private String apptEndTime;

    @SerializedName("apptDate")
    private String apptDate;

    @SerializedName("hospitalName")
    private String hospitalName;

    @SerializedName("isEpisodeExist")
    private String isEpisodeExist;

    @SerializedName("episodeCode")
    private String episodeCode;

    @SerializedName("episodeVisitNo")
    private String episodeVisitNo;

    @SerializedName("requestStatusCompleteDate")
    private String requestStatusCompleteDate;

    @SerializedName("requestStatusCompleteMode")
    private String requestStatusCompleteMode;


    public DoctorReqListDetails(String requestID, String CRNo, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String hospCode, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String cnsltntId, String patName, String patAge, String patGender, String rmrks, String email, String date, String patWeight, String patHeight, String patMedication, String patPastDiagnosis, String patAllergies, String departmentId, String departmentName, String isDocUploaded, String patientToken, String doctorToken, String appointmentNo, String apptStartTime, String apptEndTime, String apptDate, String hospitalName, String isEpisodeExist, String episodeCode, String episodeVisitNo,String requestStatusCompleteDate,String requestStatusCompleteMode) {
        this.requestID = requestID;
        this.CRNo = CRNo;
        this.scrResponse = scrResponse;
        this.consName = consName;
        this.deptUnitCode = deptUnitCode;
        this.deptUnitName = deptUnitName;
        this.hospCode = hospCode;
        this.requestStatus = requestStatus;
        this.patMobileNo = patMobileNo;
        this.consMobileNo = consMobileNo;
        this.patDocs = patDocs;
        this.docMessage = docMessage;

        this.cnsltntId = cnsltntId;
        this.patName = patName;
        this.patAge = patAge;
        this.patGender = patGender;
        this.rmrks = rmrks;
        this.email = email;


        this.departmentId = departmentId;
        this.patientCategory = "General";
        this.pVisitdate = date;
        this.hcode = hospCode;
        this.patVisitNo = "1";
        this.roomId = "0";


        this.patWeight = patWeight;
        this.patHeight = patHeight;
        this.patMedication = patMedication;
        this.patPastDiagnosis = patPastDiagnosis;
        this.patAllergies = patAllergies;
        this.departmentName = departmentName;

        this.isDocUploaded = isDocUploaded;
        if (cnsltntId.equalsIgnoreCase("")) {
            this.cnsltntId = "0";
        }

        this.patientToken = patientToken;
        this.doctorToken = doctorToken;


        this.appointmentNo = appointmentNo;
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
        this.apptDate = apptDate;
        this.hospitalName = hospitalName;
        this.isEpisodeExist = isEpisodeExist;
        this.episodeCode = episodeCode;
        this.episodeVisitNo = episodeVisitNo;

        this.requestStatusCompleteDate=requestStatusCompleteDate;
        this.requestStatusCompleteMode=requestStatusCompleteMode;

    }

    public String getRequestStatusCompleteDate() {
        return requestStatusCompleteDate;
    }

    public void setRequestStatusCompleteDate(String requestStatusCompleteDate) {
        this.requestStatusCompleteDate = requestStatusCompleteDate;
    }

    public String getRequestStatusCompleteMode() {
        return requestStatusCompleteMode;
    }

    public void setRequestStatusCompleteMode(String requestStatusCompleteMode) {
        this.requestStatusCompleteMode = requestStatusCompleteMode;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getApptStartTime() {
        return apptStartTime;
    }

    public void setApptStartTime(String apptStartTime) {
        this.apptStartTime = apptStartTime;
    }

    public String getApptEndTime() {
        return apptEndTime;
    }

    public void setApptEndTime(String apptEndTime) {
        this.apptEndTime = apptEndTime;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getIsEpisodeExist() {
        return isEpisodeExist;
    }

    public void setIsEpisodeExist(String isEpisodeExist) {
        this.isEpisodeExist = isEpisodeExist;
    }

    public String getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        this.episodeCode = episodeCode;
    }

    public String getEpisodeVisitNo() {
        return episodeVisitNo;
    }

    public void setEpisodeVisitNo(String episodeVisitNo) {
        this.episodeVisitNo = episodeVisitNo;
    }

    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

    public String getDoctorToken() {
        return doctorToken;
    }

    public void setDoctorToken(String doctorToken) {
        this.doctorToken = doctorToken;
    }

    public String getIsDocUploaded() {
        return isDocUploaded;
    }

    public void setIsDocUploaded(String isDocUploaded) {
        this.isDocUploaded = isDocUploaded;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getpVisitdate() {
        return pVisitdate;
    }

    public void setpVisitdate(String pVisitdate) {
        this.pVisitdate = pVisitdate;
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

    public String getPatMedication() {
        return patMedication;
    }

    public void setPatMedication(String patMedication) {
        this.patMedication = patMedication;
    }

    public String getPatPastDiagnosis() {
        return patPastDiagnosis;
    }

    public void setPatPastDiagnosis(String patPastDiagnosis) {
        this.patPastDiagnosis = patPastDiagnosis;
    }

    public String getPatAllergies() {
        return patAllergies;
    }

    public void setPatAllergies(String patAllergies) {
        this.patAllergies = patAllergies;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPatientCategory() {
        return patientCategory;
    }

    public void setPatientCategory(String patientCategory) {
        this.patientCategory = patientCategory;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getPatVisitNo() {
        return patVisitNo;
    }

    public void setPatVisitNo(String patVisitNo) {
        this.patVisitNo = patVisitNo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCnsltntId() {
        return cnsltntId;
    }

    public void setCnsltntId(String cnsltntId) {
        this.cnsltntId = cnsltntId;
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

    public String getRmrks() {
        return rmrks;
    }

    public void setRmrks(String rmrks) {
        this.rmrks = rmrks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getCRNo() {
        return CRNo;
    }

    public void setCRNo(String CRNo) {
        this.CRNo = CRNo;
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

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
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

    @Override
    public String toString() {
//        return "ClassPojo [clinicId = "+clinicId+", occupation = "+occupation+", pCatgId = "+pCatgId+", departmentId = "+departmentId+", pVisitdate = "+pVisitdate+", deptContextVisit = "+deptContextVisit+", hospitalCode = "+hospitalCode+", roomId = "+roomId+", broughtDeath = "+broughtDeath+", pStateName = "+pStateName+", pName = "+pName+", doctorId = "+doctorId+", unitId = "+unitId+", pAge = "+pAge+", pMobileNo = "+pMobileNo+", departmentName = "+departmentName+", pSex = "+pSex+", isMlc = "+isMlc+", isReffred = "+isReffred+", pUhid = "+pUhid+", userId = "+userId+", pCountryName = "+pCountryName+", genderCode = "+genderCode+", hospitalid = "+hospitalid+", regdate = "+regdate+", pStateCode = "+pStateCode+", queueNo = "+queueNo+"]";
        return this.CRNo;
    }

}
