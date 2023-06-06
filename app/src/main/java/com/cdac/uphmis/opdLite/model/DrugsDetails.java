package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DrugsDetails implements Serializable {
    @SerializedName("VALUE")
    @Expose
    private String value;
    @SerializedName("LABEL")
    @Expose
    private String label;
    @SerializedName("COMPANY")
    @Expose
    private String company;
    @SerializedName("SPECIFICATION")
    @Expose
    private String specification;
    @SerializedName("SUBSTANCE")
    @Expose
    private String substance;
    @SerializedName("QTY")
    @Expose
    private String qty;
    @SerializedName("EXPDATE")
    @Expose
    private String expdate;
    @SerializedName("MFCDATE")
    @Expose
    private String mfcdate;
    @SerializedName("RATE")
    @Expose
    private String rate;


    @SerializedName("ITEM_TYPE_SHORT_NAME")
    @Expose
    private String typeShortName;

    public String getTypeShortName() {
        return typeShortName;
    }

    public void setTypeShortName(String typeShortName) {
        this.typeShortName = typeShortName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMfcdate() {
        return mfcdate;
    }

    public void setMfcdate(String mfcdate) {
        this.mfcdate = mfcdate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return label;
    }
}
