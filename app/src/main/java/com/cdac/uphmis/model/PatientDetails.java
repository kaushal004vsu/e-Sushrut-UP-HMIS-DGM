package com.cdac.uphmis.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientDetails {
    @SerializedName("IS_SAIL_EMPLOYEE")
    @Expose
    private String isSailEmployee;
    @SerializedName("MOBILE_NO")
    @Expose
    private String mobileNo;
    @SerializedName("CRNO")
    @Expose
    private String crno;
    @SerializedName("FIRSTNAME")
    @Expose
    private String firstname;
    @SerializedName("MIDDLE_NAME")
    @Expose
    private String middleName;
    @SerializedName("LAST_NAME")
    @Expose
    private String lastName;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("GENDER")
    @Expose
    private String gender="";
    @SerializedName("FATHERNAME")
    @Expose
    private String fathername;
    @SerializedName("MOTHER_NAME")
    @Expose
    private String motherName;
    @SerializedName("SPOUSE_NAME")
    @Expose
    private String SPOUSE_NAME;
    @SerializedName("STATE_CODE")
    @Expose
    private String stateCode;
    @SerializedName("DISTRICT_CODE")
    @Expose
    private String districtCode;
    @SerializedName("HOSPITAL_CODE")
    @Expose
    private String hospitalCode;
    @SerializedName("PATIENT_CAT_CODE")
    @Expose
    private String patientCatCode;
    @SerializedName("CITY")
    @Expose
    private String city;
    @SerializedName("PINCODE")
    @Expose
    private String pincode;
    @SerializedName("EMAIL_ID")
    @Expose
    private String emailId;
    @SerializedName("HEALTH_ID")
    @Expose
    private String healthId;
    @SerializedName("PAT_HEALTH_ID")
    @Expose
    private String patHealthId;
    @SerializedName("UMID_DATA")
    @Expose
    private UMIDData umidData;


    @SerializedName("SUBLOCALITY")
    @Expose
    private String sublocality;
    @SerializedName("DISTRICT_NAME")
    @Expose
    private String district_name;
    @SerializedName("STATE_NAME")
    @Expose
    private String state_name;

    @SerializedName("CAT_NAME")
    @Expose
    private String catName;





    public PatientDetails(String crno, String mobileNo, String firstName, String age, String gender, String fatherName, String motherName, String spouseName, String stateId, String districtId, String email,String hospCode,String ndhmId,String ndhmPatHealthId) {
        this.crno = crno;
        this.mobileNo = mobileNo;
        this.firstname = firstName;
        this.age = age;
        this.gender = gender;
        this.fathername = fatherName;
        this.motherName = motherName;
        this.SPOUSE_NAME = spouseName;
        this.stateCode = stateId;
        this.districtCode = districtId;
        this.emailId = email;
        this.hospitalCode=hospCode;
        this.healthId=ndhmId;
        this.patHealthId=ndhmPatHealthId;
    }
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }


    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getHealthId() {
        return healthId;
    }


    public String getPatHealthId() {
        return patHealthId;
    }


    public UMIDData getUmidData() {
        return umidData;
    }

    public void setIsSailEmployee(String isSailEmployee) {
        this.isSailEmployee = isSailEmployee;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getIsSailEmployee() {
        return isSailEmployee;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPatientCatCode() {
        return patientCatCode;
    }

    public String getCity() {
        return city;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setSPOUSE_NAME(String SPOUSE_NAME) {
        this.SPOUSE_NAME = SPOUSE_NAME;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public void setPatientCatCode(String patientCatCode) {
        this.patientCatCode = patientCatCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }

    public void setPatHealthId(String patHealthId) {
        this.patHealthId = patHealthId;
    }

    public void setUmidData(UMIDData umidData) {
        this.umidData = umidData;
    }

    public String getMotherName() {
        return motherName;
    }



    public String getSPOUSE_NAME() {
        return SPOUSE_NAME;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        String name=firstname;
        if (middleName.isEmpty())
            name+=middleName;

        if (lastName.isEmpty())
            name+=lastName;

        return name;
    }
}

