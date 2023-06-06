package com.cdac.uphmis.drugavailability.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrugRxModel {

        @SerializedName("CR_NO")
        @Expose
        private String crNo;
        @SerializedName("EPISODE_CODE")
        @Expose
        private String episodeCode;
        @SerializedName("EPISODE_VISIT_NO")
        @Expose
        private String episodeVisitNo;
        @SerializedName("ADMISSION_NO")
        @Expose
        private String admissionNo;
        @SerializedName("HOSPITAL_CODE")
        @Expose
        private String hospitalCode;
        @SerializedName("ENTRYDATE")
        @Expose
        private String entrydate;
        @SerializedName("GENERIC_DRUG_CODE")
        @Expose
        private String genericDrugCode;
        @SerializedName("GENERIC_DRUG_NAME")
        @Expose
        private String genericDrugName;
        @SerializedName("DRUG_NAME")
        @Expose
        private String drugName;
        @SerializedName("DRUG_CODE")
        @Expose
        private String drugCode;
        @SerializedName("DRUG_TYPE_CODE")
        @Expose
        private String drugTypeCode;
        @SerializedName("DRUG_TYPE")
        @Expose
        private String drugType;
        @SerializedName("CODE_TYPE")
        @Expose
        private String codeType;
        @SerializedName("ROUTE_CODE")
        @Expose
        private String routeCode;
        @SerializedName("FREQUENCY_CODE")
        @Expose
        private String frequencyCode;
        @SerializedName("FREQUENCY")
        @Expose
        private String frequency;
        @SerializedName("DOSAGE_CODE")
        @Expose
        private String dosageCode;
        @SerializedName("DOSAGE")
        @Expose
        private String dosage;
        @SerializedName("NO_OF_DAYS")
        @Expose
        private String noOfDays;
        @SerializedName("START_DATE")
        @Expose
        private String startDate;
        @SerializedName("END_DATE")
        @Expose
        private String endDate;
        @SerializedName("INSTRUNTION")
        @Expose
        private String instruntion;
        @SerializedName("QUANTITY")
        @Expose
        private String quantity;
        @SerializedName("ORDER_STATUS")
        @Expose
        private String orderStatus;
        @SerializedName("IS_RUNNING")
        @Expose
        private String isRunning;
        @SerializedName("ADVISE_DATE")
        @Expose
        private String adviseDate;
        @SerializedName("ON_DATE")
        @Expose
        private String onDate;
        @SerializedName("BY_USER_CODE")
        @Expose
        private String byUserCode;
        @SerializedName("BY_USER_NAME")
        @Expose
        private String byUserName;
        @SerializedName("BY_USER_DESG")
        @Expose
        private String byUserDesg;
        @SerializedName("ADVISED_BY_ON_DATE")
        @Expose
        private String advisedByOnDate;
        @SerializedName("ADVISED_BY_USER_CODE")
        @Expose
        private String advisedByUserCode;
        @SerializedName("ADVISED_BY_USER_NAME")
        @Expose
        private String advisedByUserName;
        @SerializedName("ADVISED_BY_USER_DESG")
        @Expose
        private String advisedByUserDesg;
        @SerializedName("ADVICED_QTY")
        @Expose
        private String advicedqty;
        @SerializedName("ISSUE_QTY")
        @Expose
        private String issueqty;
    @SerializedName("HOSP_NAME")
    @Expose
    private String hospitalName;
    @SerializedName("UNIT_NAME")
    @Expose
    private String unitName;
        public String getCrNo() {
            return crNo;
        }

        public void setCrNo(String crNo) {
            this.crNo = crNo;
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

        public String getHospitalCode() {
            return hospitalCode;
        }

        public void setHospitalCode(String hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public String getEntrydate() {
            return entrydate;
        }

        public void setEntrydate(String entrydate) {
            this.entrydate = entrydate;
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

        public String getDrugTypeCode() {
            return drugTypeCode;
        }

        public void setDrugTypeCode(String drugTypeCode) {
            this.drugTypeCode = drugTypeCode;
        }

        public String getDrugType() {
            return drugType;
        }

        public void setDrugType(String drugType) {
            this.drugType = drugType;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getRouteCode() {
            return routeCode;
        }

        public void setRouteCode(String routeCode) {
            this.routeCode = routeCode;
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

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
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

        public String getInstruntion() {
            return instruntion;
        }

        public void setInstruntion(String instruntion) {
            this.instruntion = instruntion;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
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

        public String getOnDate() {
            return onDate;
        }

        public void setOnDate(String onDate) {
            this.onDate = onDate;
        }

        public String getByUserCode() {
            return byUserCode;
        }

        public void setByUserCode(String byUserCode) {
            this.byUserCode = byUserCode;
        }

        public String getByUserName() {
            return byUserName;
        }

        public void setByUserName(String byUserName) {
            this.byUserName = byUserName;
        }

        public String getByUserDesg() {
            return byUserDesg;
        }

        public void setByUserDesg(String byUserDesg) {
            this.byUserDesg = byUserDesg;
        }

        public String getAdvisedByOnDate() {
            return advisedByOnDate;
        }

        public void setAdvisedByOnDate(String advisedByOnDate) {
            this.advisedByOnDate = advisedByOnDate;
        }

        public String getAdvisedByUserCode() {
            return advisedByUserCode;
        }

        public void setAdvisedByUserCode(String advisedByUserCode) {
            this.advisedByUserCode = advisedByUserCode;
        }

        public String getAdvisedByUserName() {
            return advisedByUserName;
        }

        public void setAdvisedByUserName(String advisedByUserName) {
            this.advisedByUserName = advisedByUserName;
        }

        public String getAdvisedByUserDesg() {
            return advisedByUserDesg;
        }

        public void setAdvisedByUserDesg(String advisedByUserDesg) {
            this.advisedByUserDesg = advisedByUserDesg;
        }

        public String getAdvicedqty() {
            return advicedqty;
        }

        public void setAdvicedqty(String advicedqty) {
            this.advicedqty = advicedqty;
        }

        public String getIssueqty() {
            return issueqty;
        }

        public void setIssueqty(String issueqty) {
            this.issueqty = issueqty;
        }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}


