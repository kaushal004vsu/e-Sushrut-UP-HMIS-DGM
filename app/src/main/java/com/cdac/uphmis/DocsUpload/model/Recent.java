
package com.cdac.uphmis.DocsUpload.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recent {

    @SerializedName("document_id")
    @Expose
    private String documentId;
    @SerializedName("document_type_code")
    @Expose
    private String documentTypeCode;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("document_upload_date")
    @Expose
    private String documentUploadDate;
    @SerializedName("document_category")
    @Expose
    private String documentCategory;
    @SerializedName("cr_no")
    @Expose
    private String crNo;
    @SerializedName("hospital_code")
    @Expose
    private String hospitalCode;
    @SerializedName("encounter_detail")
    @Expose
    private EncounterDetail__1 encounterDetail;
    @SerializedName("document_title")
    @Expose
    private String documentTitle;
    @SerializedName("document_content_type")
    @Expose
    private String documentContentType;
    @SerializedName("document_base64")
    @Expose
    private String documentBase64;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUploadDate() {
        return documentUploadDate;
    }

    public void setDocumentUploadDate(String documentUploadDate) {
        this.documentUploadDate = documentUploadDate;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    public String getCrNo() {
        return crNo;
    }

    public void setCrNo(String crNo) {
        this.crNo = crNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public EncounterDetail__1 getEncounterDetail() {
        return encounterDetail;
    }

    public void setEncounterDetail(EncounterDetail__1 encounterDetail) {
        this.encounterDetail = encounterDetail;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public String getDocumentBase64() {
        return documentBase64;
    }

    public void setDocumentBase64(String documentBase64) {
        this.documentBase64 = documentBase64;
    }

}
