package com.cdac.uphmis.covid19.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewPatientDetails implements Serializable {

    @SerializedName("mobileNo")
    private String mobileNo;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("age")
    private String age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("fatherName")
    private String fatherName;

    @SerializedName("motherName")
    private String motherName;

    @SerializedName("spouseName")
    private String spouseName;

    @SerializedName("departmentId")
    private String departmentId;

    @SerializedName("tempRegNo")
    private String tempRegNo;

    @SerializedName("stateId")
    private String stateId;

    @SerializedName("districtId")
    private String districtId;

    @SerializedName("city")
    private String city;

    @SerializedName("email")
    private String email;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NewPatientDetails(String mobileNo, String firstName, String lastName, String age, String gender, String fatherName, String motherName, String spouseName, String departmentId, String tempRegNo, String stateId, String districtId, String city, String email) {
        this.mobileNo = mobileNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.spouseName = spouseName;
        this.departmentId = departmentId;
        this.tempRegNo = tempRegNo;
        this.stateId = stateId;
        this.districtId = districtId;
        this.city = city;
        this.email=email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getTempRegNo() {
        return tempRegNo;
    }

    public void setTempRegNo(String tempRegNo) {
        this.tempRegNo = tempRegNo;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
