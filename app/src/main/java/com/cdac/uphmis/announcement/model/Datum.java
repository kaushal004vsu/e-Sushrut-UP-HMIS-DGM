
package com.cdac.uphmis.announcement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("REQ_NO")
    @Expose
    private String reqNo;
    @SerializedName("SUBJECT")
    @Expose
    private String subject;
    @SerializedName("PUBLISH_DATE")
    @Expose
    private String publishDate;
    @SerializedName("VALID_TILL")
    @Expose
    private String validTill;
    @SerializedName("DOCUMENT_FILE")
    @Expose
    private String documentFile;
    @SerializedName("IS_NEW")
    @Expose
    private String IS_NEW;

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }
 public String getIS_NEW() {
        return IS_NEW;
    }

    public void setIS_NEW(String IS_NEW) {
        this.IS_NEW = IS_NEW;
    }

}
