package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EMRJsonDetails {

    @SerializedName("Patient_Name")
    @Expose
    private String patientName;
    @SerializedName("CR_No")
    @Expose
    private String cRNo;
    @SerializedName("EpisodeCode")
    @Expose
    private String episodeCode;
    @SerializedName("EpisodeVisitNo")
    @Expose
    private String episodeVisitNo;
    @SerializedName("CurrentVisitDate")
    @Expose
    private String currentVisitDate;
    @SerializedName("PatVisitType")
    @Expose
    private String patVisitType;
    @SerializedName("LastVisitDate")
    @Expose
    private String lastVisitDate;
    @SerializedName("PatientGender")
    @Expose
    private String patientGender;
    @SerializedName("PatientAge")
    @Expose
    private String patientAge;
    @SerializedName("PatientCat")
    @Expose
    private String patientCat;
    @SerializedName("PatientQueueNo")
    @Expose
    private String patientQueueNo;
    @SerializedName("hosp_code")
    @Expose
    private String hospCode;
    @SerializedName("hrgnum_is_docuploaded")
    @Expose
    private Integer hrgnumIsDocuploaded;
    @SerializedName("ConsultantName")
    @Expose
    private String consultantName;
    @SerializedName("PatientDept")
    @Expose
    private String patientDept;
    @SerializedName("patGaurdianName")
    @Expose
    private String patGaurdianName;
    @SerializedName("PatCompleteGeneralDtl")
    @Expose
    private String patCompleteGeneralDtl;
    @SerializedName("seatId")
    @Expose
    private String seatId;
    @SerializedName("HistoryOfPresentIllNess")
    @Expose
    private String historyOfPresentIllNess;
    @SerializedName("DiagnosisNote")
    @Expose
    private String diagnosisNote;
    @SerializedName("InvestgationNote")
    @Expose
    private String investgationNote;
    @SerializedName("OtherAllergies")
    @Expose
    private String otherAllergies;
    @SerializedName("ReasonOfVisitJsonArray")
    @Expose
    private List<Object> reasonOfVisitJsonArray = null;
    @SerializedName("DiagnosisJsonArray")
    @Expose
    private List<DiagnosisJsonArray> diagnosisJsonArray = null;
    @SerializedName("InvestigationJsonArray")
    @Expose
    private List<Object> investigationJsonArray = null;
    @SerializedName("CompleteHistoryJaonArray")
    @Expose
    private CompleteHistoryJaonArray completeHistoryJaonArray;
    @SerializedName("SystematicExamniationArray")
    @Expose
    private SystematicExamniationArray systematicExamniationArray;
    @SerializedName("ChronicDiseaseArray")
    @Expose
    private List<Object> chronicDiseaseArray = null;
    @SerializedName("PiccleArray")
    @Expose
    private PiccleArray piccleArray;
    @SerializedName("ClinicalProcedureJsonArray")
    @Expose
    private List<Object> clinicalProcedureJsonArray = null;
    @SerializedName("DrugJsonArray")
    @Expose
    private List<DrugJsonArray> drugJsonArray = null;
    @SerializedName("PatientRefrel")
    @Expose
    private List<Object> patientRefrel = null;
    @SerializedName("strpiccle")
    @Expose
    private Strpiccle strpiccle;
    @SerializedName("strtreatmentAdvice")
    @Expose
    private String strtreatmentAdvice;
    @SerializedName("strConfidentialsInfo")
    @Expose
    private String strConfidentialsInfo;
    @SerializedName("strVitalsChart")
    @Expose
    private String strVitalsChart;
    @SerializedName("FOLLOW_UP")
    @Expose
    private List<FollowUp> followUp = null;
    @SerializedName("strDeptIdflg")
    @Expose
    private String strDeptIdflg;
    @SerializedName("strAllDeptIdflg")
    @Expose
    private String strAllDeptIdflg;
    @SerializedName("strPresCriptionBookmarkNameval")
    @Expose
    private String strPresCriptionBookmarkNameval;
    @SerializedName("strPresCriptionBookmarkDescVal")
    @Expose
    private String strPresCriptionBookmarkDescVal;
    @SerializedName("strUmidNo")
    @Expose
    private String strUmidNo;
    @SerializedName("admissionadviceDeptName")
    @Expose
    private String admissionadviceDeptName;
    @SerializedName("admissionadviceWardName")
    @Expose
    private String admissionadviceWardName;
    @SerializedName("admissionadviceNotes")
    @Expose
    private String admissionadviceNotes;
    @SerializedName("strDesignation")
    @Expose
    private String strDesignation;
    @SerializedName("strStation")
    @Expose
    private String strStation;

    public EMRJsonDetails(String patientName, String cRNo, String episodeCode, String episodeVisitNo, String currentVisitDate, String patVisitType, String lastVisitDate, String patientGender, String patientAge, String patientCat, String patientQueueNo, String hospCode, Integer hrgnumIsDocuploaded, String consultantName, String patientDept, String patGaurdianName, String patCompleteGeneralDtl, String seatId, String historyOfPresentIllNess, String diagnosisNote, String investgationNote, String otherAllergies, List<Object> reasonOfVisitJsonArray, List<DiagnosisJsonArray> diagnosisJsonArray, List<Object> investigationJsonArray, CompleteHistoryJaonArray completeHistoryJaonArray, SystematicExamniationArray systematicExamniationArray, List<Object> chronicDiseaseArray, PiccleArray piccleArray, List<Object> clinicalProcedureJsonArray, List<DrugJsonArray> drugJsonArray, List<Object> patientRefrel, Strpiccle strpiccle, String strtreatmentAdvice, String strConfidentialsInfo, String strVitalsChart, List<FollowUp> followUp, String strDeptIdflg, String strAllDeptIdflg, String strPresCriptionBookmarkNameval, String strPresCriptionBookmarkDescVal, String strUmidNo, String admissionadviceDeptName, String admissionadviceWardName, String admissionadviceNotes, String strDesignation, String strStation) {
        this.patientName = patientName;
        this.cRNo = cRNo;
        this.episodeCode = episodeCode;
        this.episodeVisitNo = episodeVisitNo;
        this.currentVisitDate = currentVisitDate;
        this.patVisitType = patVisitType;
        this.lastVisitDate = lastVisitDate;
        this.patientGender = patientGender;
        this.patientAge = patientAge;
        this.patientCat = patientCat;
        this.patientQueueNo = patientQueueNo;
        this.hospCode = hospCode;
        this.hrgnumIsDocuploaded = hrgnumIsDocuploaded;
        this.consultantName = consultantName;
        this.patientDept = patientDept;
        this.patGaurdianName = patGaurdianName;
        this.patCompleteGeneralDtl = patCompleteGeneralDtl;
        this.seatId = seatId;
        this.historyOfPresentIllNess = historyOfPresentIllNess;
        this.diagnosisNote = diagnosisNote;
        this.investgationNote = investgationNote;
        this.otherAllergies = otherAllergies;
        this.reasonOfVisitJsonArray = reasonOfVisitJsonArray;
        this.diagnosisJsonArray = diagnosisJsonArray;
        this.investigationJsonArray = investigationJsonArray;
        this.completeHistoryJaonArray = completeHistoryJaonArray;
        this.systematicExamniationArray = systematicExamniationArray;
        this.chronicDiseaseArray = chronicDiseaseArray;
        this.piccleArray = piccleArray;
        this.clinicalProcedureJsonArray = clinicalProcedureJsonArray;
        this.drugJsonArray = drugJsonArray;
        this.patientRefrel = patientRefrel;
        this.strpiccle = strpiccle;
        this.strtreatmentAdvice = strtreatmentAdvice;
        this.strConfidentialsInfo = strConfidentialsInfo;
        this.strVitalsChart = strVitalsChart;
        this.followUp = followUp;
        this.strDeptIdflg = strDeptIdflg;
        this.strAllDeptIdflg = strAllDeptIdflg;
        this.strPresCriptionBookmarkNameval = strPresCriptionBookmarkNameval;
        this.strPresCriptionBookmarkDescVal = strPresCriptionBookmarkDescVal;
        this.strUmidNo = strUmidNo;
        this.admissionadviceDeptName = admissionadviceDeptName;
        this.admissionadviceWardName = admissionadviceWardName;
        this.admissionadviceNotes = admissionadviceNotes;
        this.strDesignation = strDesignation;
        this.strStation = strStation;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCRNo() {
        return cRNo;
    }

    public void setCRNo(String cRNo) {
        this.cRNo = cRNo;
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

    public String getCurrentVisitDate() {
        return currentVisitDate;
    }

    public void setCurrentVisitDate(String currentVisitDate) {
        this.currentVisitDate = currentVisitDate;
    }

    public String getPatVisitType() {
        return patVisitType;
    }

    public void setPatVisitType(String patVisitType) {
        this.patVisitType = patVisitType;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientCat() {
        return patientCat;
    }

    public void setPatientCat(String patientCat) {
        this.patientCat = patientCat;
    }

    public String getPatientQueueNo() {
        return patientQueueNo;
    }

    public void setPatientQueueNo(String patientQueueNo) {
        this.patientQueueNo = patientQueueNo;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public Integer getHrgnumIsDocuploaded() {
        return hrgnumIsDocuploaded;
    }

    public void setHrgnumIsDocuploaded(Integer hrgnumIsDocuploaded) {
        this.hrgnumIsDocuploaded = hrgnumIsDocuploaded;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getPatientDept() {
        return patientDept;
    }

    public void setPatientDept(String patientDept) {
        this.patientDept = patientDept;
    }

    public String getPatGaurdianName() {
        return patGaurdianName;
    }

    public void setPatGaurdianName(String patGaurdianName) {
        this.patGaurdianName = patGaurdianName;
    }

    public String getPatCompleteGeneralDtl() {
        return patCompleteGeneralDtl;
    }

    public void setPatCompleteGeneralDtl(String patCompleteGeneralDtl) {
        this.patCompleteGeneralDtl = patCompleteGeneralDtl;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getHistoryOfPresentIllNess() {
        return historyOfPresentIllNess;
    }

    public void setHistoryOfPresentIllNess(String historyOfPresentIllNess) {
        this.historyOfPresentIllNess = historyOfPresentIllNess;
    }

    public String getDiagnosisNote() {
        return diagnosisNote;
    }

    public void setDiagnosisNote(String diagnosisNote) {
        this.diagnosisNote = diagnosisNote;
    }

    public String getInvestgationNote() {
        return investgationNote;
    }

    public void setInvestgationNote(String investgationNote) {
        this.investgationNote = investgationNote;
    }

    public String getOtherAllergies() {
        return otherAllergies;
    }

    public void setOtherAllergies(String otherAllergies) {
        this.otherAllergies = otherAllergies;
    }

    public List<Object> getReasonOfVisitJsonArray() {
        return reasonOfVisitJsonArray;
    }

    public void setReasonOfVisitJsonArray(List<Object> reasonOfVisitJsonArray) {
        this.reasonOfVisitJsonArray = reasonOfVisitJsonArray;
    }

    public List<DiagnosisJsonArray> getDiagnosisJsonArray() {
        return diagnosisJsonArray;
    }

    public void setDiagnosisJsonArray(List<DiagnosisJsonArray> diagnosisJsonArray) {
        this.diagnosisJsonArray = diagnosisJsonArray;
    }

    public List<Object> getInvestigationJsonArray() {
        return investigationJsonArray;
    }

    public void setInvestigationJsonArray(List<Object> investigationJsonArray) {
        this.investigationJsonArray = investigationJsonArray;
    }

    public CompleteHistoryJaonArray getCompleteHistoryJaonArray() {
        return completeHistoryJaonArray;
    }

    public void setCompleteHistoryJaonArray(CompleteHistoryJaonArray completeHistoryJaonArray) {
        this.completeHistoryJaonArray = completeHistoryJaonArray;
    }

    public SystematicExamniationArray getSystematicExamniationArray() {
        return systematicExamniationArray;
    }

    public void setSystematicExamniationArray(SystematicExamniationArray systematicExamniationArray) {
        this.systematicExamniationArray = systematicExamniationArray;
    }

    public List<Object> getChronicDiseaseArray() {
        return chronicDiseaseArray;
    }

    public void setChronicDiseaseArray(List<Object> chronicDiseaseArray) {
        this.chronicDiseaseArray = chronicDiseaseArray;
    }

    public PiccleArray getPiccleArray() {
        return piccleArray;
    }

    public void setPiccleArray(PiccleArray piccleArray) {
        this.piccleArray = piccleArray;
    }

    public List<Object> getClinicalProcedureJsonArray() {
        return clinicalProcedureJsonArray;
    }

    public void setClinicalProcedureJsonArray(List<Object> clinicalProcedureJsonArray) {
        this.clinicalProcedureJsonArray = clinicalProcedureJsonArray;
    }

    public List<DrugJsonArray> getDrugJsonArray() {
        return drugJsonArray;
    }

    public void setDrugJsonArray(List<DrugJsonArray> drugJsonArray) {
        this.drugJsonArray = drugJsonArray;
    }

    public List<Object> getPatientRefrel() {
        return patientRefrel;
    }

    public void setPatientRefrel(List<Object> patientRefrel) {
        this.patientRefrel = patientRefrel;
    }

    public Strpiccle getStrpiccle() {
        return strpiccle;
    }

    public void setStrpiccle(Strpiccle strpiccle) {
        this.strpiccle = strpiccle;
    }

    public String getStrtreatmentAdvice() {
        return strtreatmentAdvice;
    }

    public void setStrtreatmentAdvice(String strtreatmentAdvice) {
        this.strtreatmentAdvice = strtreatmentAdvice;
    }

    public String getStrConfidentialsInfo() {
        return strConfidentialsInfo;
    }

    public void setStrConfidentialsInfo(String strConfidentialsInfo) {
        this.strConfidentialsInfo = strConfidentialsInfo;
    }

    public String getStrVitalsChart() {
        return strVitalsChart;
    }

    public void setStrVitalsChart(String strVitalsChart) {
        this.strVitalsChart = strVitalsChart;
    }

    public List<FollowUp> getFollowUp() {
        return followUp;
    }

    public void setFollowUp(List<FollowUp> followUp) {
        this.followUp = followUp;
    }

    public String getStrDeptIdflg() {
        return strDeptIdflg;
    }

    public void setStrDeptIdflg(String strDeptIdflg) {
        this.strDeptIdflg = strDeptIdflg;
    }

    public String getStrAllDeptIdflg() {
        return strAllDeptIdflg;
    }

    public void setStrAllDeptIdflg(String strAllDeptIdflg) {
        this.strAllDeptIdflg = strAllDeptIdflg;
    }

    public String getStrPresCriptionBookmarkNameval() {
        return strPresCriptionBookmarkNameval;
    }

    public void setStrPresCriptionBookmarkNameval(String strPresCriptionBookmarkNameval) {
        this.strPresCriptionBookmarkNameval = strPresCriptionBookmarkNameval;
    }

    public String getStrPresCriptionBookmarkDescVal() {
        return strPresCriptionBookmarkDescVal;
    }

    public void setStrPresCriptionBookmarkDescVal(String strPresCriptionBookmarkDescVal) {
        this.strPresCriptionBookmarkDescVal = strPresCriptionBookmarkDescVal;
    }

    public String getStrUmidNo() {
        return strUmidNo;
    }

    public void setStrUmidNo(String strUmidNo) {
        this.strUmidNo = strUmidNo;
    }

    public String getAdmissionadviceDeptName() {
        return admissionadviceDeptName;
    }

    public void setAdmissionadviceDeptName(String admissionadviceDeptName) {
        this.admissionadviceDeptName = admissionadviceDeptName;
    }

    public String getAdmissionadviceWardName() {
        return admissionadviceWardName;
    }

    public void setAdmissionadviceWardName(String admissionadviceWardName) {
        this.admissionadviceWardName = admissionadviceWardName;
    }

    public String getAdmissionadviceNotes() {
        return admissionadviceNotes;
    }

    public void setAdmissionadviceNotes(String admissionadviceNotes) {
        this.admissionadviceNotes = admissionadviceNotes;
    }

    public String getStrDesignation() {
        return strDesignation;
    }

    public void setStrDesignation(String strDesignation) {
        this.strDesignation = strDesignation;
    }

    public String getStrStation() {
        return strStation;
    }

    public void setStrStation(String strStation) {
        this.strStation = strStation;
    }
}
