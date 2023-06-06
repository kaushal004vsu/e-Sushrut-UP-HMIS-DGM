package com.cdac.uphmis.DoctorDesk.savePrescription;

import com.google.gson.annotations.SerializedName;

public class Pres_data
{
    @SerializedName("pres_history")
    private String pres_history;

    @SerializedName("isattended")
    private String isattended;

    @SerializedName("pres_vitals")
    private String pres_vitals;

    @SerializedName("pres_treatment")
    private String pres_treatment;

    @SerializedName("pres_examination")
    private String pres_examination;

    @SerializedName("pres_procedure")
    private String pres_procedure;

    @SerializedName("pres_test")
    private String pres_test;

    @SerializedName("pres_diagnosis_type")
    private String pres_diagnosis_type;

    @SerializedName("pres_drug")
    private String pres_drug;

    @SerializedName("pres_complaint")
    private String pres_complaint;

    @SerializedName("pres_diagnosis")
    private String pres_diagnosis;






    public String getPres_history ()
    {
        return pres_history;
    }

    public void setPres_history (String pres_history)
    {
        this.pres_history = pres_history;
    }

    public String getIsattended ()
    {
        return isattended;
    }

    public void setIsattended (String isattended)
    {
        this.isattended = isattended;
    }

    public String getPres_vitals ()
    {
        return pres_vitals;
    }

    public void setPres_vitals (String pres_vitals)
    {
        this.pres_vitals = pres_vitals;
    }

    public String getPres_treatment ()
    {
        return pres_treatment;
    }

    public void setPres_treatment (String pres_treatment)
    {
        this.pres_treatment = pres_treatment;
    }

    public String getPres_examination ()
    {
        return pres_examination;
    }

    public void setPres_examination (String pres_examination)
    {
        this.pres_examination = pres_examination;
    }

    public String getPres_procedure ()
    {
        return pres_procedure;
    }

    public void setPres_procedure (String pres_procedure)
    {
        this.pres_procedure = pres_procedure;
    }

    public String getPres_test ()
    {
        return pres_test;
    }

    public void setPres_test (String pres_test)
    {
        this.pres_test = pres_test;
    }

    public String getPres_diagnosis_type ()
    {
        return pres_diagnosis_type;
    }

    public void setPres_diagnosis_type (String pres_diagnosis_type)
    {
        this.pres_diagnosis_type = pres_diagnosis_type;
    }

    public String getPres_drug ()
    {
        return pres_drug;
    }

    public void setPres_drug (String pres_drug)
    {
        this.pres_drug = pres_drug;
    }

    public String getPres_complaint ()
    {
        return pres_complaint;
    }

    public void setPres_complaint (String pres_complaint)
    {
        this.pres_complaint = pres_complaint;
    }

    public String getPres_diagnosis ()
    {
        return pres_diagnosis;
    }

    public void setPres_diagnosis (String pres_diagnosis)
    {
        this.pres_diagnosis = pres_diagnosis;
    }

    public Pres_data(String pres_history, String isattended, String pres_vitals, String pres_treatment, String pres_examination, String pres_procedure, String pres_test, String pres_diagnosis_type, String pres_drug, String pres_complaint, String pres_diagnosis) {
        this.pres_history = pres_history;
        this.isattended = isattended;
        this.pres_vitals = pres_vitals;
        this.pres_treatment = pres_treatment;
        this.pres_examination = pres_examination;
        this.pres_procedure = pres_procedure;
        this.pres_test = pres_test;
        this.pres_diagnosis_type = pres_diagnosis_type;
        this.pres_drug = pres_drug;
        this.pres_complaint = pres_complaint;
        this.pres_diagnosis = pres_diagnosis;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [pres_history = "+pres_history+", isattended = "+isattended+", pres_vitals = "+pres_vitals+", pres_treatment = "+pres_treatment+", pres_examination = "+pres_examination+", pres_procedure = "+pres_procedure+", pres_test = "+pres_test+", pres_diagnosis_type = "+pres_diagnosis_type+", pres_drug = "+pres_drug+", pres_complaint = "+pres_complaint+", pres_diagnosis = "+pres_diagnosis+"]";
//    }
}