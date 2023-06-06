package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EHRJsonDetails {

    @SerializedName("InvTestCode")
    @Expose
    private List<Object> invTestCode = null;
    @SerializedName("InvTestCodeToPrint")
    @Expose
    private List<Object> invTestCodeToPrint = null;
    @SerializedName("DrugCodeCat")
    @Expose
    private List<Object> drugCodeCat = null;
    @SerializedName("ReasonOfVisit")
    @Expose
    private List<Object> reasonOfVisit = null;
    @SerializedName("Diagnosis")
    @Expose
    private List<Object> diagnosis = null;
    @SerializedName("FOLLOW_UP")
    @Expose
    private List<FollowUp> followUp = null;
    @SerializedName("pat_Name")
    @Expose
    private String patName;
    @SerializedName("CR_No")
    @Expose
    private String cRNo;
    @SerializedName("episodeCode")
    @Expose
    private String episodeCode;
    @SerializedName("episodeVisitNo")
    @Expose
    private String episodeVisitNo;
    @SerializedName("currentVisitDate")
    @Expose
    private String currentVisitDate;
    @SerializedName("patVisitType")
    @Expose
    private String patVisitType;
    @SerializedName("lastVisitDate")
    @Expose
    private String lastVisitDate;
    @SerializedName("patGender")
    @Expose
    private String patGender;
    @SerializedName("patAge")
    @Expose
    private String patAge;
    @SerializedName("patCat")
    @Expose
    private String patCat;
    @SerializedName("patQueueNo")
    @Expose
    private String patQueueNo;
    @SerializedName("hosp_code")
    @Expose
    private String hospCode;
    @SerializedName("seatId")
    @Expose
    private String seatId;
    @SerializedName("hrgnum_is_docuploaded")
    @Expose
    private Integer hrgnumIsDocuploaded;
    @SerializedName("patConsultantName")
    @Expose
    private String patConsultantName;
    @SerializedName("patDept")
    @Expose
    private String patDept;
    @SerializedName("patGaurdianName")
    @Expose
    private String patGaurdianName;
    @SerializedName("PatCompleteGeneralDtl")
    @Expose
    private String patCompleteGeneralDtl;
    @SerializedName("strCompleteHistory")
    @Expose
    private StrCompleteHistory strCompleteHistory;
    @SerializedName("strSystematicExamniation")
    @Expose
    private StrSystematicExamniation strSystematicExamniation;
    @SerializedName("strChronicDisease")
    @Expose
    private List<Object> strChronicDisease = null;
    @SerializedName("strHistoryOfPresentIllNess")
    @Expose
    private String strHistoryOfPresentIllNess;
    @SerializedName("strDiagnosisNote")
    @Expose
    private String strDiagnosisNote;
    @SerializedName("strDrugAllergy")
    @Expose
    private List<Object> strDrugAllergy = null;
    @SerializedName("strInvestgationNote")
    @Expose
    private String strInvestgationNote;
    @SerializedName("strotherAllergies")
    @Expose
    private String strotherAllergies;
    @SerializedName("strClinicalProcedure")
    @Expose
    private List<Object> strClinicalProcedure = null;
    @SerializedName("strtreatmentAdvice")
    @Expose
    private String strtreatmentAdvice;
    @SerializedName("strVitalsChart")
    @Expose
    private String strVitalsChart;
    @SerializedName("strpiccle")
    @Expose
    private Strpiccle strpiccle;
    @SerializedName("strConfidentialsInfo")
    @Expose
    private String strConfidentialsInfo;
    @SerializedName("strReferal")
    @Expose
    private List<Object> strReferal = null;
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

    public EHRJsonDetails(List<Object> invTestCode, List<Object> invTestCodeToPrint, List<Object> drugCodeCat, List<Object> reasonOfVisit, List<Object> diagnosis, List<FollowUp> followUp, String patName, String cRNo, String episodeCode, String episodeVisitNo, String currentVisitDate, String patVisitType, String lastVisitDate, String patGender, String patAge, String patCat, String patQueueNo, String hospCode, String seatId, Integer hrgnumIsDocuploaded, String patConsultantName, String patDept, String patGaurdianName, String patCompleteGeneralDtl, StrCompleteHistory strCompleteHistory, StrSystematicExamniation strSystematicExamniation, List<Object> strChronicDisease, String strHistoryOfPresentIllNess, String strDiagnosisNote, List<Object> strDrugAllergy, String strInvestgationNote, String strotherAllergies, List<Object> strClinicalProcedure, String strtreatmentAdvice, String strVitalsChart, Strpiccle strpiccle, String strConfidentialsInfo, List<Object> strReferal, String strDeptIdflg, String strAllDeptIdflg, String strPresCriptionBookmarkNameval, String strPresCriptionBookmarkDescVal, String strUmidNo, String admissionadviceDeptName, String admissionadviceWardName, String admissionadviceNotes, String strDesignation, String strStation) {
        this.invTestCode = invTestCode;
        this.invTestCodeToPrint = invTestCodeToPrint;
        this.drugCodeCat = drugCodeCat;
        this.reasonOfVisit = reasonOfVisit;
        this.diagnosis = diagnosis;
        this.followUp = followUp;
        this.patName = patName;
        this.cRNo = cRNo;
        this.episodeCode = episodeCode;
        this.episodeVisitNo = episodeVisitNo;
        this.currentVisitDate = currentVisitDate;
        this.patVisitType = patVisitType;
        this.lastVisitDate = lastVisitDate;
        this.patGender = patGender;
        this.patAge = patAge;
        this.patCat = patCat;
        this.patQueueNo = patQueueNo;
        this.hospCode = hospCode;
        this.seatId = seatId;
        this.hrgnumIsDocuploaded = hrgnumIsDocuploaded;
        this.patConsultantName = patConsultantName;
        this.patDept = patDept;
        this.patGaurdianName = patGaurdianName;
        this.patCompleteGeneralDtl = patCompleteGeneralDtl;
        this.strCompleteHistory = strCompleteHistory;
        this.strSystematicExamniation = strSystematicExamniation;
        this.strChronicDisease = strChronicDisease;
        this.strHistoryOfPresentIllNess = strHistoryOfPresentIllNess;
        this.strDiagnosisNote = strDiagnosisNote;
        this.strDrugAllergy = strDrugAllergy;
        this.strInvestgationNote = strInvestgationNote;
        this.strotherAllergies = strotherAllergies;
        this.strClinicalProcedure = strClinicalProcedure;
        this.strtreatmentAdvice = strtreatmentAdvice;
        this.strVitalsChart = strVitalsChart;
        this.strpiccle = strpiccle;
        this.strConfidentialsInfo = strConfidentialsInfo;
        this.strReferal = strReferal;
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

    public List<Object> getInvTestCode() {
        return invTestCode;
    }

    public void setInvTestCode(List<Object> invTestCode) {
        this.invTestCode = invTestCode;
    }

    public List<Object> getInvTestCodeToPrint() {
        return invTestCodeToPrint;
    }

    public void setInvTestCodeToPrint(List<Object> invTestCodeToPrint) {
        this.invTestCodeToPrint = invTestCodeToPrint;
    }

    public List<Object> getDrugCodeCat() {
        return drugCodeCat;
    }

    public void setDrugCodeCat(List<Object> drugCodeCat) {
        this.drugCodeCat = drugCodeCat;
    }

    public List<Object> getReasonOfVisit() {
        return reasonOfVisit;
    }

    public void setReasonOfVisit(List<Object> reasonOfVisit) {
        this.reasonOfVisit = reasonOfVisit;
    }

    public List<Object> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Object> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<FollowUp> getFollowUp() {
        return followUp;
    }

    public void setFollowUp(List<FollowUp> followUp) {
        this.followUp = followUp;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
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

    public String getPatGender() {
        return patGender;
    }

    public void setPatGender(String patGender) {
        this.patGender = patGender;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    public String getPatCat() {
        return patCat;
    }

    public void setPatCat(String patCat) {
        this.patCat = patCat;
    }

    public String getPatQueueNo() {
        return patQueueNo;
    }

    public void setPatQueueNo(String patQueueNo) {
        this.patQueueNo = patQueueNo;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public Integer getHrgnumIsDocuploaded() {
        return hrgnumIsDocuploaded;
    }

    public void setHrgnumIsDocuploaded(Integer hrgnumIsDocuploaded) {
        this.hrgnumIsDocuploaded = hrgnumIsDocuploaded;
    }

    public String getPatConsultantName() {
        return patConsultantName;
    }

    public void setPatConsultantName(String patConsultantName) {
        this.patConsultantName = patConsultantName;
    }

    public String getPatDept() {
        return patDept;
    }

    public void setPatDept(String patDept) {
        this.patDept = patDept;
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

    public StrCompleteHistory getStrCompleteHistory() {
        return strCompleteHistory;
    }

    public void setStrCompleteHistory(StrCompleteHistory strCompleteHistory) {
        this.strCompleteHistory = strCompleteHistory;
    }

    public StrSystematicExamniation getStrSystematicExamniation() {
        return strSystematicExamniation;
    }

    public void setStrSystematicExamniation(StrSystematicExamniation strSystematicExamniation) {
        this.strSystematicExamniation = strSystematicExamniation;
    }

    public List<Object> getStrChronicDisease() {
        return strChronicDisease;
    }

    public void setStrChronicDisease(List<Object> strChronicDisease) {
        this.strChronicDisease = strChronicDisease;
    }

    public String getStrHistoryOfPresentIllNess() {
        return strHistoryOfPresentIllNess;
    }

    public void setStrHistoryOfPresentIllNess(String strHistoryOfPresentIllNess) {
        this.strHistoryOfPresentIllNess = strHistoryOfPresentIllNess;
    }

    public String getStrDiagnosisNote() {
        return strDiagnosisNote;
    }

    public void setStrDiagnosisNote(String strDiagnosisNote) {
        this.strDiagnosisNote = strDiagnosisNote;
    }

    public List<Object> getStrDrugAllergy() {
        return strDrugAllergy;
    }

    public void setStrDrugAllergy(List<Object> strDrugAllergy) {
        this.strDrugAllergy = strDrugAllergy;
    }

    public String getStrInvestgationNote() {
        return strInvestgationNote;
    }

    public void setStrInvestgationNote(String strInvestgationNote) {
        this.strInvestgationNote = strInvestgationNote;
    }

    public String getStrotherAllergies() {
        return strotherAllergies;
    }

    public void setStrotherAllergies(String strotherAllergies) {
        this.strotherAllergies = strotherAllergies;
    }

    public List<Object> getStrClinicalProcedure() {
        return strClinicalProcedure;
    }

    public void setStrClinicalProcedure(List<Object> strClinicalProcedure) {
        this.strClinicalProcedure = strClinicalProcedure;
    }

    public String getStrtreatmentAdvice() {
        return strtreatmentAdvice;
    }

    public void setStrtreatmentAdvice(String strtreatmentAdvice) {
        this.strtreatmentAdvice = strtreatmentAdvice;
    }

    public String getStrVitalsChart() {
        return strVitalsChart;
    }

    public void setStrVitalsChart(String strVitalsChart) {
        this.strVitalsChart = strVitalsChart;
    }

    public Strpiccle getStrpiccle() {
        return strpiccle;
    }

    public void setStrpiccle(Strpiccle strpiccle) {
        this.strpiccle = strpiccle;
    }

    public String getStrConfidentialsInfo() {
        return strConfidentialsInfo;
    }

    public void setStrConfidentialsInfo(String strConfidentialsInfo) {
        this.strConfidentialsInfo = strConfidentialsInfo;
    }

    public List<Object> getStrReferal() {
        return strReferal;
    }

    public void setStrReferal(List<Object> strReferal) {
        this.strReferal = strReferal;
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
