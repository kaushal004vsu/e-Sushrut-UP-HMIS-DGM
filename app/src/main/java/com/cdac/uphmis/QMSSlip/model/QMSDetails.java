package com.cdac.uphmis.QMSSlip.model;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QMSDetails implements Serializable {

    @SerializedName("PATCRNO")
    @Expose
    private String patcrno;
    @SerializedName("EPISODECODE")
    @Expose
    private String episodecode;
    @SerializedName("VISITNO")
    @Expose
    private String visitno;
    @SerializedName("VISITTYPE")
    @Expose
    private String visittype;
    @SerializedName("DEPTUNITCODE")
    @Expose
    private String deptunitcode;
    @SerializedName("ISEPISODEOPEN")
    @Expose
    private String isepisodeopen;
    @SerializedName("EPISODESTARTDATE")
    @Expose
    private String episodestartdate;
    @SerializedName("EPISODETARIFF")
    @Expose
    private String episodetariff;
    @SerializedName("HOSPNAME")
    @Expose
    private String hospname;
    @SerializedName("HOSPCODE")
    @Expose
    private String hospcode;
    @SerializedName("QUEUENO")
    @Expose
    private String queueno;
    @SerializedName("ROOMNAME")
    @Expose
    private String roomname;
    @SerializedName("DEPTNAME")
    @Expose
    private String deptname;
    @SerializedName("DEPTUNITNAME")
    @Expose
    private String deptunitname;
    @SerializedName("PRINTEDON")
    @Expose
    private String printedon;
    @SerializedName("UMID")
    @Expose
    private String umid;
    @SerializedName("PATNAME")
    @Expose
    private String patname;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("GENDERCODE")
    @Expose
    private String gendercode;
    @SerializedName("ENTRY_DATE")
    @Expose
    private String entryDate;

    public String getPatcrno() {
        return patcrno;
    }

    public void setPatcrno(String patcrno) {
        this.patcrno = patcrno;
    }

    public String getEpisodecode() {
        return episodecode;
    }

    public void setEpisodecode(String episodecode) {
        this.episodecode = episodecode;
    }

    public String getVisitno() {
        return visitno;
    }

    public void setVisitno(String visitno) {
        this.visitno = visitno;
    }

    public String getVisittype() {
        return visittype;
    }

    public void setVisittype(String visittype) {
        this.visittype = visittype;
    }

    public String getDeptunitcode() {
        return deptunitcode;
    }

    public void setDeptunitcode(String deptunitcode) {
        this.deptunitcode = deptunitcode;
    }

    public String getIsepisodeopen() {
        return isepisodeopen;
    }

    public void setIsepisodeopen(String isepisodeopen) {
        this.isepisodeopen = isepisodeopen;
    }

    public String getEpisodestartdate() {
        return episodestartdate;
    }

    public void setEpisodestartdate(String episodestartdate) {
        this.episodestartdate = episodestartdate;
    }

    public String getEpisodetariff() {
        return episodetariff;
    }

    public void setEpisodetariff(String episodetariff) {
        this.episodetariff = episodetariff;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public String getHospcode() {
        return hospcode;
    }

    public void setHospcode(String hospcode) {
        this.hospcode = hospcode;
    }

    public String getQueueno() {
        return queueno;
    }

    public void setQueueno(String queueno) {
        this.queueno = queueno;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDeptunitname() {
        return deptunitname;
    }

    public void setDeptunitname(String deptunitname) {
        this.deptunitname = deptunitname;
    }

    public String getPrintedon() {
        return printedon;
    }

    public void setPrintedon(String printedon) {
        this.printedon = printedon;
    }

    public String getUmid() {
        return umid;
    }

    public void setUmid(String umid) {
        this.umid = umid;
    }

    public String getPatname() {
        return patname;
    }

    public void setPatname(String patname) {
        this.patname = patname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGendercode() {
        return gendercode;
    }

    public void setGendercode(String gendercode) {
        this.gendercode = gendercode;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
