package com.cdac.uphmis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UMIDData {
    @SerializedName("pch_id")
    @Expose
    private String pchId;
    @SerializedName("beneficiary_uuid")
    @Expose
    private String beneficiaryUuid;
    @SerializedName("umid_no")
    @Expose
    private String umidNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("relation")
    @Expose
    private String relation;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("residential_address")
    @Expose
    private String residentialAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("health_unit_opted")
    @Expose
    private String healthUnitOpted;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("job_type")
    @Expose
    private String jobType;
    @SerializedName("current_status")
    @Expose
    private String currentStatus;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("aadhaar_no")
    @Expose
    private String aadhaarNo;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("spouse_name")
    @Expose
    private String spouseName;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("handicap_status")
    @Expose
    private String handicapStatus;
    @SerializedName("level_of_entitilment")
    @Expose
    private String levelOfEntitilment;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("opd_eligibility")
    @Expose
    private String opdEligibility;
    @SerializedName("ipd_eligibility")
    @Expose
    private String ipdEligibility;
    @SerializedName("beneficiary_type")
    @Expose
    private String beneficiaryType;
    @SerializedName("custom_unit")
    @Expose
    private String customUnit;
    @SerializedName("custom_unit_code")
    @Expose
    private String customUnitCode;
    @SerializedName("custom_zone")
    @Expose
    private String customZone;
    @SerializedName("custom_zone_code")
    @Expose
    private String customZoneCode;
    @SerializedName("card_status")
    @Expose
    private String cardStatus;
    @SerializedName("card_inactive_remarks")
    @Expose
    private String cardInactiveRemarks;
    @SerializedName("umid_remarks")
    @Expose
    private String umidRemarks;
    @SerializedName("id_card_validity_status_flag")
    @Expose
    private String idCardValidityStatusFlag;
    @SerializedName("patient_category")
    @Expose
    private Integer patientCategory;

    public String getPchId() {
        return pchId;
    }

    public void setPchId(String pchId) {
        this.pchId = pchId;
    }

    public String getBeneficiaryUuid() {
        return beneficiaryUuid;
    }

    public void setBeneficiaryUuid(String beneficiaryUuid) {
        this.beneficiaryUuid = beneficiaryUuid;
    }

    public String getUmidNo() {
        return umidNo;
    }

    public void setUmidNo(String umidNo) {
        this.umidNo = umidNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHealthUnitOpted() {
        return healthUnitOpted;
    }

    public void setHealthUnitOpted(String healthUnitOpted) {
        this.healthUnitOpted = healthUnitOpted;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getHandicapStatus() {
        return handicapStatus;
    }

    public void setHandicapStatus(String handicapStatus) {
        this.handicapStatus = handicapStatus;
    }

    public String getLevelOfEntitilment() {
        return levelOfEntitilment;
    }

    public void setLevelOfEntitilment(String levelOfEntitilment) {
        this.levelOfEntitilment = levelOfEntitilment;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOpdEligibility() {
        return opdEligibility;
    }

    public void setOpdEligibility(String opdEligibility) {
        this.opdEligibility = opdEligibility;
    }

    public String getIpdEligibility() {
        return ipdEligibility;
    }

    public void setIpdEligibility(String ipdEligibility) {
        this.ipdEligibility = ipdEligibility;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getCustomUnit() {
        return customUnit;
    }

    public void setCustomUnit(String customUnit) {
        this.customUnit = customUnit;
    }

    public String getCustomUnitCode() {
        return customUnitCode;
    }

    public void setCustomUnitCode(String customUnitCode) {
        this.customUnitCode = customUnitCode;
    }

    public String getCustomZone() {
        return customZone;
    }

    public void setCustomZone(String customZone) {
        this.customZone = customZone;
    }

    public String getCustomZoneCode() {
        return customZoneCode;
    }

    public void setCustomZoneCode(String customZoneCode) {
        this.customZoneCode = customZoneCode;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardInactiveRemarks() {
        return cardInactiveRemarks;
    }

    public void setCardInactiveRemarks(String cardInactiveRemarks) {
        this.cardInactiveRemarks = cardInactiveRemarks;
    }

    public String getUmidRemarks() {
        return umidRemarks;
    }

    public void setUmidRemarks(String umidRemarks) {
        this.umidRemarks = umidRemarks;
    }

    public String getIdCardValidityStatusFlag() {
        return idCardValidityStatusFlag;
    }

    public void setIdCardValidityStatusFlag(String idCardValidityStatusFlag) {
        this.idCardValidityStatusFlag = idCardValidityStatusFlag;
    }

    public Integer getPatientCategory() {
        return patientCategory;
    }

    public void setPatientCategory(Integer patientCategory) {
        this.patientCategory = patientCategory;
    }

}