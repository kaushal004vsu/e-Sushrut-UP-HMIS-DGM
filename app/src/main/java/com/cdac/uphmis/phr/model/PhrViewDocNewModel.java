package com.cdac.uphmis.phr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhrViewDocNewModel {
    @SerializedName("FTP_PATH")
    @Expose
    private String ftpPath;
    @SerializedName("CRNO")
    @Expose
    private String crno;
    @SerializedName("VITAL_ID")
    @Expose
    private String vitalId;

    @SerializedName("RECORD_DATE")
    @Expose
    private String recordDate;

    @SerializedName("IS_CONSENT")
    @Expose
    private String isConsent;

    @SerializedName("SL_NO")
    @Expose
    private String slNo;
    @SerializedName("FILE_TYPE")
    @Expose
    private String fileType;
    @SerializedName("DOC_NAME")
    @Expose
    private String docName;

    public PhrViewDocNewModel(String ftpPath, String crno, String vitalId, String recordDate, String isConsent, String slNo, String fileType, String docName) {
        this.ftpPath = ftpPath;
        this.crno = crno;
        this.vitalId = vitalId;
        this.recordDate = recordDate;
        this.isConsent = isConsent;
        this.slNo = slNo;
        this.fileType = fileType;
        this.docName = docName;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getVitalId() {
        return vitalId;
    }

    public void setVitalId(String vitalId) {
        this.vitalId = vitalId;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getIsConsent() {
        return isConsent;
    }

    public void setIsConsent(String isConsent) {
        this.isConsent = isConsent;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
