package com.cdac.uphmis.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cdac.uphmis.model.PatientDetails;
import com.google.gson.Gson;


public class ManagingSharedData {

    private SharedPreferences sharedPreferences;

    public ManagingSharedData(Context ct) {
        sharedPreferences = ct.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

//Patient
    public String setPatientDetails(PatientDetails patientDetails) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(patientDetails);
        editor.putString("PatientDetails", json);
        editor.putString("crno", patientDetails.getCrno());
        editor.putString("mobileno", patientDetails.getMobileNo());
        editor.putString("hospCode", patientDetails.getHospitalCode());
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public PatientDetails getPatientDetails() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("PatientDetails", null);
        PatientDetails obj = gson.fromJson(json, PatientDetails.class);
        return obj;
    }

    public String setAppFlag(String app_flag) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("app_flag", app_flag);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getAppFlag() {
        String app_flag = null;
        app_flag = sharedPreferences.getString("app_flag", null);
        return app_flag;
    }
    public String setMobileNo(String crno) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobileno", crno);
        editor.commit();
        return "data saved";
    }

    public String getMobileNo() {
        String id;
        id = sharedPreferences.getString("mobileno", null);
        return id;
    }
    public String setCrList(String crList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("crlist", crList);
        editor.commit();
        return "Data saved";
    }

    public String getCrList() {
        String crList;
        crList = sharedPreferences.getString("crlist", null);
        return crList;
    }


    //doctor
    public String setCrNo(String crno) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("crno", crno);
        editor.commit();
        return "data saved";
    }

    public String getCrNo() {
        String id;
        id = sharedPreferences.getString("crno", null);
        return id;
    }
    public String setToken(String crno) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", crno);
        editor.commit();
        return "Data saved";
    }
    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    //doctor
    public String setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getUsername() {
        String username = null;
        username = sharedPreferences.getString("username", null);
        return username;
    }


    public String writeSeatNo(String seatid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("seatid", seatid);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getSeatId() {
        String seatId = null;
        seatId = sharedPreferences.getString("seatid", null);
        return seatId;
    }


    public String setWhichModuleToLogin(String moduleName) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("moduleName", moduleName);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getWhichModuleToLogin() {
        String moduleName = null;
        moduleName = sharedPreferences.getString("moduleName", null);
        return moduleName;
    }


    public String setEmloyeeCode(String employeeCode) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("employeeCode", employeeCode);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getEmployeeCode() {
        String employeeCode = null;
        employeeCode = sharedPreferences.getString("employeeCode", null);
        return employeeCode;
    }

    public String setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getuserId() {
        String moduleName = null;
        moduleName = sharedPreferences.getString("userId", null);
        return moduleName;
    }

    public String setHospCode(String hospCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hospCode", hospCode);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getHospCode() {
        String hospCode = null;
        hospCode = sharedPreferences.getString("hospCode", null);
        return hospCode;
    }
    public String setBloodGroup(String BloodGroup) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BloodGroup", BloodGroup);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getBloodGroup() {
        String BloodGroup = null;
        BloodGroup = sharedPreferences.getString("BloodGroup", null);
        return BloodGroup;
    }

    public String setLangaugeFlag(String lnaguage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lnaguage", lnaguage);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getLanguageFlag() {
        String lnaguage = null;
        lnaguage = sharedPreferences.getString("lnaguage", null);
        return lnaguage;
    }
    public String setDarkMode(String s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("darkMode", s);
        editor.commit();
        String msg = "data saved";
        return msg;
    }

    public String getDarkMode() {
        String s = null;
        s = sharedPreferences.getString("darkMode", null);
        return s;
    }

    public String setAbhaReminderCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int value=getAbhaReminderCount();
        value=value+1;
        editor.putInt("abhaReminderCount", value);
        editor.commit();
        String msg = "data saved";
        return msg;
    }
    public int getAbhaReminderCount() {
        int count=0 ;
        count = sharedPreferences.getInt("abhaReminderCount", 0);
        return count;
    }
    public String logOut() {
        String msg;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("crno", null);
        editor.putString("mobileno", null);
        editor.putString("patientHasWallet", null);
        editor.putString("crlist", null);
        editor.putString("PatientDetails", null);
        editor.putInt("abhaReminderCount", 0);
        //doctor
        editor.putString("username", null);
        editor.putString("seatid", null);
        editor.putString("moduleName", null);
        editor.putString("userId", null);
        editor.putString("doctorusername", null);
        editor.putString("employeeCode", null);
        editor.putString("token", null);
        editor.putString("hospCode", null);
        editor.putString("BloodGroup", null);
        editor.putString("app_flag", null);

        editor.commit();
        msg = "User logged out successfully.";
        return msg;
    }
}
