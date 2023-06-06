package com.cdac.uphmis.certificates.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdmissionSlipDetails {

    @SerializedName("HOSP_NAME")
    @Expose
    private String hospName;
    @SerializedName("DEPT_NAME")
    @Expose
    private String deptName;
    @SerializedName("DEATHFLAG")
    @Expose
    private String deathflag;
    @SerializedName("ACCSTATUS")
    @Expose
    private String accstatus;
    @SerializedName("UNIT")
    @Expose
    private String unit;
    @SerializedName("NVL")
    @Expose
    private String nvl;
    @SerializedName("PUK")
    @Expose
    private String puk;
    @SerializedName("FINALBILLFLG")
    @Expose
    private String finalbillflg;
    @SerializedName("BED_NAME")
    @Expose
    private String bedName;
    @SerializedName("AGE_SEX")
    @Expose
    private String AGE_SEX;
    @SerializedName("NAME")
    @Expose
    private String name;
    @SerializedName("WARD")
    @Expose
    private String ward;
    @SerializedName("ROOM_NAME")
    @Expose
    private String roomName;
    @SerializedName("UNIT_NAME")
    @Expose
    private String unitName;
    @SerializedName("BEDCODE")
    @Expose
    private String bedcode;
    @SerializedName("DEPT")
    @Expose
    private String dept;
    @SerializedName("ADM_NO")
    @Expose
    private String admNo;
    @SerializedName("WARD_NAME")
    @Expose
    private String wardName;
    @SerializedName("ROOM")
    @Expose
    private String room;

    public String getHospName() {
        return hospName;
    }

    @SerializedName("DISCDATETIME")
    @Expose
    private String discDateTime;


    @SerializedName("PROFILEID")
    @Expose
    private String profileId;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDiscDateTime() {
        return discDateTime;
    }

    public void setDiscDateTime(String discDateTime) {
        this.discDateTime = discDateTime;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeathflag() {
        return deathflag;
    }

    public void setDeathflag(String deathflag) {
        this.deathflag = deathflag;
    }

    public String getAccstatus() {
        return accstatus;
    }

    public void setAccstatus(String accstatus) {
        this.accstatus = accstatus;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNvl() {
        return nvl;
    }

    public void setNvl(String nvl) {
        this.nvl = nvl;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getFinalbillflg() {
        return finalbillflg;
    }

    public void setFinalbillflg(String finalbillflg) {
        this.finalbillflg = finalbillflg;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getAGE_SEX() {
        return AGE_SEX;
    }

    public void setAGE_SEX(String AGE_SEX) {
        this.AGE_SEX = AGE_SEX;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBedcode() {
        return bedcode;
    }

    public void setBedcode(String bedcode) {
        this.bedcode = bedcode;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getAdmNo() {
        return admNo;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
