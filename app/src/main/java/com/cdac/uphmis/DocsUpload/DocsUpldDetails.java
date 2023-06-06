package com.cdac.uphmis.DocsUpload;

import android.net.Uri;

public class DocsUpldDetails {
    private Uri uri;
    private String mimeType;
    private boolean isUploaded;
    private int docName;

    public DocsUpldDetails(Uri uri, String mimeType) {
        this.uri = uri;
        this.mimeType = mimeType;
        this.isUploaded=false;
        this.docName=0;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public int getDocName() {
        return docName;
    }

    public void setDocName(int docName) {
        this.docName = docName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return uri.toString();
    }

}
