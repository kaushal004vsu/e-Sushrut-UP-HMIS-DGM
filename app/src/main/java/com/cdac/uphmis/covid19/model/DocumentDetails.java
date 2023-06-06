package com.cdac.uphmis.covid19.model;

import android.net.Uri;

public class DocumentDetails {
    private Uri uri;
    private String mimeType;

    public DocumentDetails(Uri uri,  String mimeType) {
        this.uri = uri;
        this.mimeType = mimeType;
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
