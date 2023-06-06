
package com.cdac.uphmis.drugavailability.model.expandableList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medication {

    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("drug_name")
    @Expose
    private String drugName;
    @SerializedName("drug_code")
    @Expose
    private String drugCode;
    @SerializedName("generic_drug_code")
    @Expose
    private String genericDrugCode;
    @SerializedName("generic_drug_name")
    @Expose
    private String genericDrugName;
    @SerializedName("drug_type_code")
    @Expose
    private String drugTypeCode;
    @SerializedName("code_type")
    @Expose
    private String codeType;
    @SerializedName("drug_type")
    @Expose
    private String drugType;
    @SerializedName("cr_no")
    @Expose
    private String crNo;
    @SerializedName("hospital_code")
    @Expose
    private String hospitalCode;
    @SerializedName("episode_code")
    @Expose
    private String episodeCode;
    @SerializedName("episode_visit_no")
    @Expose
    private String episodeVisitNo;
    @SerializedName("admission_no")
    @Expose
    private String admissionNo;
    @SerializedName("encounter_detail")
    @Expose
    private EncounterDetail__2 encounterDetail;
    @SerializedName("frequency_code")
    @Expose
    private String frequencyCode;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("dosage_code")
    @Expose
    private String dosageCode;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("dosage")
    @Expose
    private String dosage;
    @SerializedName("route_code")
    @Expose
    private String routeCode;
    @SerializedName("no_of_days")
    @Expose
    private String noOfDays;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("is_running")
    @Expose
    private String isRunning;
    @SerializedName("advise_date")
    @Expose
    private String adviseDate;
    @SerializedName("adviced_qty")
    @Expose
    private String advicedQty;
    @SerializedName("issue_qty")
    @Expose
    private String issueQty;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getGenericDrugCode() {
        return genericDrugCode;
    }

    public void setGenericDrugCode(String genericDrugCode) {
        this.genericDrugCode = genericDrugCode;
    }

    public String getGenericDrugName() {
        return genericDrugName;
    }

    public void setGenericDrugName(String genericDrugName) {
        this.genericDrugName = genericDrugName;
    }

    public String getDrugTypeCode() {
        return drugTypeCode;
    }

    public void setDrugTypeCode(String drugTypeCode) {
        this.drugTypeCode = drugTypeCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getCrNo() {
        return crNo;
    }

    public void setCrNo(String crNo) {
        this.crNo = crNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
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

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public EncounterDetail__2 getEncounterDetail() {
        return encounterDetail;
    }

    public void setEncounterDetail(EncounterDetail__2 encounterDetail) {
        this.encounterDetail = encounterDetail;
    }

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDosageCode() {
        return dosageCode;
    }

    public void setDosageCode(String dosageCode) {
        this.dosageCode = dosageCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(String isRunning) {
        this.isRunning = isRunning;
    }

    public String getAdviseDate() {
        return adviseDate;
    }

    public void setAdviseDate(String adviseDate) {
        this.adviseDate = adviseDate;
    }

    public String getAdvicedQty() {
        return advicedQty;
    }

    public void setAdvicedDty(String advicedQty) {
        this.advicedQty = advicedQty;
    }

    public String getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(String issueQty) {
        this.issueQty = issueQty;
    }
}
