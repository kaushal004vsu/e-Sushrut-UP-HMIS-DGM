package com.cdac.uphmis.appointment.model;

class RegistrationDetails {
    private String patGender, patName, patCrNo, email, patMobileNo, address, patAge, country_code, district_code, state_code, pat_guardian, isPaymentDone, isGatewayAvailable;

    public RegistrationDetails(String patGender, String patName, String patCrNo, String email, String patMobileNo, String address, String patAge, String country_code, String district_code, String state_code, String pat_guardian, String isPaymentDone, String isGatewayAvailable) {
        this.patGender = patGender;
        this.patName = patName;
        this.patCrNo = patCrNo;
        this.email = email;
        this.patMobileNo = patMobileNo;
        this.address = address;
        this.patAge = patAge;
        this.country_code = country_code;
        this.district_code = district_code;
        this.state_code = state_code;
        this.pat_guardian = pat_guardian;
        this.isPaymentDone = isPaymentDone;
        this.isGatewayAvailable = isGatewayAvailable;
    }


    public String getPatGender() {
        return patGender;
    }

    public void setPatGender(String patGender) {
        this.patGender = patGender;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatCrNo() {
        return patCrNo;
    }

    public void setPatCrNo(String patCrNo) {
        this.patCrNo = patCrNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatMobileNo() {
        return patMobileNo;
    }

    public void setPatMobileNo(String patMobileNo) {
        this.patMobileNo = patMobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatAge() {
        return patAge;
    }

    public void setPatAge(String patAge) {
        this.patAge = patAge;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getPat_guardian() {
        return pat_guardian;
    }

    public void setPat_guardian(String pat_guardian) {
        this.pat_guardian = pat_guardian;
    }

    public String getIsPaymentDone() {
        return isPaymentDone;
    }

    public void setIsPaymentDone(String isPaymentDone) {
        this.isPaymentDone = isPaymentDone;
    }

    public String getIsGatewayAvailable() {
        return isGatewayAvailable;
    }

    public void setIsGatewayAvailable(String isGatewayAvailable) {
        this.isGatewayAvailable = isGatewayAvailable;
    }
}
