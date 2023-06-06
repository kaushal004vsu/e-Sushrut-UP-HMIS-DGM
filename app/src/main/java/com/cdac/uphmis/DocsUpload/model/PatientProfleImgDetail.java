
package com.cdac.uphmis.DocsUpload.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientProfleImgDetail {

    @SerializedName("documents_detail")
    @Expose
    private DocumentsDetail documentsDetail;

    public DocumentsDetail getDocumentsDetail() {
        return documentsDetail;
    }

    public void setDocumentsDetail(DocumentsDetail documentsDetail) {
        this.documentsDetail = documentsDetail;
    }

}
