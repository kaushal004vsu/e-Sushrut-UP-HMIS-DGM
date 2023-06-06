package com.cdac.uphmis.covid19.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewDocNewModel {
    @SerializedName("HRGNUM_REQ_ID")
    @Expose
    private String HRGNUM_REQ_ID;
    @SerializedName("GNUM_HOSPITAL_CODE")
    @Expose
    private String GNUM_HOSPITAL_CODE;
    @SerializedName("HRGNUM_S_NO")
    @Expose
    private String HRGNUM_S_NO;
    @SerializedName("HRGSTR_FTP_PATH")
    @Expose
    private String HRGSTR_FTP_PATH;
    @SerializedName("GNUM_ISVALID")
    @Expose
    private String GNUM_ISVALID;
    @SerializedName("GDT_ENTRY_DATE")
    @Expose
    private String GDT_ENTRY_DATE;
    @SerializedName("HRGSTR_FILE_TYPE")
    @Expose
    private String HRGSTR_FILE_TYPE;

    public String getHRGNUM_REQ_ID() {
        return HRGNUM_REQ_ID;
    }

    public void setHRGNUM_REQ_ID(String HRGNUM_REQ_ID) {
        this.HRGNUM_REQ_ID = HRGNUM_REQ_ID;
    }

    public String getGNUM_HOSPITAL_CODE() {
        return GNUM_HOSPITAL_CODE;
    }

    public void setGNUM_HOSPITAL_CODE(String GNUM_HOSPITAL_CODE) {
        this.GNUM_HOSPITAL_CODE = GNUM_HOSPITAL_CODE;
    }

    public String getHRGNUM_S_NO() {
        return HRGNUM_S_NO;
    }

    public void setHRGNUM_S_NO(String HRGNUM_S_NO) {
        this.HRGNUM_S_NO = HRGNUM_S_NO;
    }

    public String getHRGSTR_FTP_PATH() {
        return HRGSTR_FTP_PATH;
    }

    public void setHRGSTR_FTP_PATH(String HRGSTR_FTP_PATH) {
        this.HRGSTR_FTP_PATH = HRGSTR_FTP_PATH;
    }

    public String getGNUM_ISVALID() {
        return GNUM_ISVALID;
    }

    public void setGNUM_ISVALID(String GNUM_ISVALID) {
        this.GNUM_ISVALID = GNUM_ISVALID;
    }

    public String getGDT_ENTRY_DATE() {
        return GDT_ENTRY_DATE;
    }

    public void setGDT_ENTRY_DATE(String GDT_ENTRY_DATE) {
        this.GDT_ENTRY_DATE = GDT_ENTRY_DATE;
    }

    public String getHRGSTR_FILE_TYPE() {
        return HRGSTR_FILE_TYPE;
    }

    public void setHRGSTR_FILE_TYPE(String HRGSTR_FILE_TYPE) {
        this.HRGSTR_FILE_TYPE = HRGSTR_FILE_TYPE;
    }
}
