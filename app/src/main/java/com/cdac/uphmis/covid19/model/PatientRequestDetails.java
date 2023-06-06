package com.cdac.uphmis.covid19.model;

import java.io.Serializable;

public class PatientRequestDetails implements Serializable {

    private String requestID;
    private String CRNo;
    private String scrResponse;
    private String consName;
    private String deptUnitCode;
    private String deptUnitName;
    private String hospCode;
    private String requestStatus;
    private String patMobileNo;
    private String consMobileNo;
    private String patDocs;
    private String docMessage;
    private String cnsltntId;
    private String patName;
    private String patAge;
    private String patGender;
    private String rmrks;
    private String email;
    private String date;

    private String patWeight;
    private String patHeight;
    private String patMedication;
    private String patPastDiagnosis;
    private String patAllergies;
    private String deptName;
    private String deptCode;

    private String appointmentNo;
    private String apptStartTime;
    private String apptEndTime;
    private String apptDate;
    private String hospitalName;

    private String isEpisodeExist;
    private String episodeCode;
    private String episodeVisitNo;
    private String requestStatusCompleteDate;
    private String requestStatusCompleteMode;


    private boolean isPast;

    private String patientToken;
    private String doctorToken;
    private String consultationTime;

    public PatientRequestDetails(String requestID, String CRNo, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String hospCode, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String cnsltntId, String patName, String patAge, String patGender, String rmrks, String email, String date, String patWeight, String patHeight, String patMedication, String patPastDiagnosis, String patAllergies, String deptName, String deptCode, String appointmentNo, String apptStartTime, String apptEndTime, String apptDate, String hospitalName, String isEpisodeExist, String episodeCode, String episodeVisitNo, String requestStatusCompleteDate, String requestStatusCompleteMode, boolean isPast,String patientToken,String doctorToken,String consultationTime) {
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
        this.date = date;
        this.patWeight = patWeight;
        this.patHeight = patHeight;
        this.patMedication = patMedication;
        this.patPastDiagnosis = patPastDiagnosis;
        this.patAllergies = patAllergies;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.appointmentNo = appointmentNo;
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
        this.apptDate = apptDate;
        this.hospitalName = hospitalName;
        this.isEpisodeExist = isEpisodeExist;
        this.episodeCode = episodeCode;
        this.episodeVisitNo = episodeVisitNo;
        this.requestStatusCompleteDate = requestStatusCompleteDate;
        this.requestStatusCompleteMode = requestStatusCompleteMode;

        this.isPast=isPast;

        this.patientToken=patientToken;
        this.doctorToken=doctorToken;
        this.consultationTime=consultationTime;
    }

    public String getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(String consultationTime) {
        this.consultationTime = consultationTime;
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

    public boolean isPast() {
        return isPast;
    }

    public void setPast(boolean past) {
        isPast = past;
    }
/* public PatientRequestDetails(String requestID, String CRNo, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String hospCode, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String cnsltntId, String patName, String patAge, String patGender, String rmrks, String email, String date, String patWeight, String patHeight, String patMedication, String patPastDiagnosis, String patAllergies, String deptName, String deptCode, String appointmentNo, String apptStartTime, String apptEndTime, String apptDate, String hospitalName) {
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
        this.date = date;

        this.patWeight = patWeight;
        this.patHeight = patHeight;
        this.patMedication = patMedication;
        this.patPastDiagnosis = patPastDiagnosis;
        this.patAllergies = patAllergies;
        this.deptName = deptName;
        this.deptCode = deptCode;

        this.appointmentNo = appointmentNo;
        this.apptStartTime = apptStartTime;
        this.apptEndTime = apptEndTime;
        this.apptDate = apptDate;
        this.hospitalName = hospitalName;
    }
*/

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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
