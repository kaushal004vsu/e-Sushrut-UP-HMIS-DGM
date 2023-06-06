package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.SerializedName;

public class DiagnosisDetails {

    @SerializedName("term")
    private String term;

    @SerializedName("id")
    private String id;

    public DiagnosisDetails(String term, String id) {
        this.term = term;
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  term ;
    }
}
