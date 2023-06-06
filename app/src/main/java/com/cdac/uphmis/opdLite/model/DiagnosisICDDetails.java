package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosisICDDetails implements Diagnosis {
    @SerializedName("DISEASE_CODE")
    @Expose
    private String diseaseCode;
    @SerializedName("DISEASE_NAME")
    @Expose
    private String diseaseName;

    private boolean searchByCode=false;

    public DiagnosisICDDetails(String diseaseCode, String diseaseName, boolean searchByCode) {
        this.diseaseCode=diseaseCode;
        this.diseaseName=diseaseName;
        this.searchByCode=searchByCode;
    }

    public DiagnosisICDDetails() {

    }

    public boolean isSearchByCode() {
        return searchByCode;
    }

    public void setSearchByCode(boolean searchByCode) {
        this.searchByCode = searchByCode;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }


    @Override
    public String toString() {
        if (searchByCode) {
            return diseaseCode;
        }
        else {
            return diseaseName;
        }
    }
}
