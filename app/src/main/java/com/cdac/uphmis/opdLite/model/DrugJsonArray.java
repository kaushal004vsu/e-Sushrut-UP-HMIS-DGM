package com.cdac.uphmis.opdLite.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DrugJsonArray  implements Serializable {


    @SerializedName("IsExterNal")
    @Expose
    private String isExterNal;

    @SerializedName("DrugName")
    @Expose
    private String drugName;

    @SerializedName("DrugCode")
    @Expose
    private String drugCode;

    @SerializedName("DoaseName")
    @Expose
    private String doaseName;

    @SerializedName("DoaseCode")
    @Expose
    private String doaseCode;

    @SerializedName("FrequencyName")
    @Expose
    private String frequencyName;

    @SerializedName("FrequencyCode")
    @Expose
    private String frequencyCode;

    @SerializedName("StartDate")
    @Expose
    private String startDate;

    @SerializedName("DrugDays")
    @Expose
    private String drugDays;

    @SerializedName("DrugQuantity")
    @Expose
    private String drugQuantity;

    @SerializedName("DrugInstruction")
    @Expose
    private String drugInstruction;


    @SerializedName("moring")
    private  String moring;

    @SerializedName("afternoon")
    private  String afternoon;

    @SerializedName("evening")
    private  String evening;

    @SerializedName("night")
    private  String night;



    @SerializedName("drugDetails")
    private DrugsDetails drugDetails;

    @SerializedName("Mode")
    @Expose
    private String Mode="NEW";

    @SerializedName("EpisodeCode")
    @Expose
    private String episodeCode;

    @SerializedName("VisitNo")
    @Expose
    private String visitNo;


    public DrugJsonArray() {
    }

    public DrugJsonArray(String isExterNal, String drugName, String drugCode, String doaseName, String doaseCode, String frequencyName, String frequencyCode, String startDate, String drugDays, String drugQuantity, String drugInstruction, DrugsDetails drugsDetails, String morning, String afternoon, String evening, String night, String episodeCode, String visitNo) {
        this.isExterNal = isExterNal;
        this.drugName = drugName;
        this.drugCode = drugCode;
        this.doaseName = doaseName;
        this.doaseCode = doaseCode;
        this.frequencyName = frequencyName;
        this.frequencyCode = frequencyCode;
        this.startDate = startDate;
        this.drugDays = drugDays;
        this.drugQuantity = drugQuantity;
        this.drugInstruction = drugInstruction;
        this.drugInstruction = drugInstruction;


        this.drugDetails = drugsDetails;
        this.moring = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;


        this.episodeCode=episodeCode;
        this.visitNo=visitNo;

    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getEpisodeCode() {
        return episodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        this.episodeCode = episodeCode;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getIsExterNal() {
        return isExterNal;
    }

    public void setIsExterNal(String isExterNal) {
        this.isExterNal = isExterNal;
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

    public String getDoaseName() {
        return doaseName;
    }

    public void setDoaseName(String doaseName) {
        this.doaseName = doaseName;
    }

    public String getDoaseCode() {
        return doaseCode;
    }

    public void setDoaseCode(String doaseCode) {
        this.doaseCode = doaseCode;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDrugDays() {
        return drugDays;
    }

    public void setDrugDays(String drugDays) {
        this.drugDays = drugDays;
    }

    public String getDrugQuantity() {
        return drugQuantity;
    }

    public void setDrugQuantity(String drugQuantity) {
        this.drugQuantity = drugQuantity;
    }

    public String getDrugInstruction() {
        return drugInstruction;
    }

    public void setDrugInstruction(String drugInstruction) {
        this.drugInstruction = drugInstruction;
    }


    public DrugsDetails getDrugDetails() {
        return drugDetails;
    }

    public void setDrugDetails(DrugsDetails drugDetails) {
        this.drugDetails = drugDetails;
    }


    public String getMoring() {
        return moring;
    }

    public void setMoring(String moring) {
        this.moring = moring;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    @Override
    public String toString() {

            return drugDetails.getLabel() + "&&" + drugDetails.getValue() + "&&" + doaseName + "&&" + doaseCode + "&&" + frequencyName + "&&" + frequencyCode + "&&" + startDate + "&&" + drugDays + "#" + drugQuantity + "&&" + drugInstruction + "&& && &&";
        }

}