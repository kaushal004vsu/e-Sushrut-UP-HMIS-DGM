package com.cdac.uphmis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressDetails {

    @SerializedName("DISTRICT_ID")
    @Expose
    private Integer districtId;
    @SerializedName("STATE_ID")
    @Expose
    private Integer stateId;
    @SerializedName("DISTRICT_NAME")
    @Expose
    private String districtName;
    @SerializedName("STATE_NAME")
    @Expose
    private String stateName;
    @SerializedName("VILLAGE")
    @Expose
    private String village;
    @SerializedName("TALUK")
    @Expose
    private String taluk;
    @SerializedName("TOWN")
    @Expose
    private String town;
    @SerializedName("STATE_SHORT_NAME")
    @Expose
    private String stateShortName;
    @SerializedName("LANGUAGE_CODE")
    @Expose
    private Integer languageCode;
    @SerializedName("COUNTRY_CODE")
    @Expose
    private String countryCode;

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStateShortName() {
        return stateShortName;
    }

    public void setStateShortName(String stateShortName) {
        this.stateShortName = stateShortName;
    }

    public Integer getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(Integer languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
