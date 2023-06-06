package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvestigationDetails {
    @SerializedName("TEST_DETAIL")
    @Expose
    private String testDetail;
    @SerializedName("TEST_NAME")
    @Expose
    private String TESTNAME;
    @SerializedName("LABNAME")
    @Expose
    private String labname;

    public String getTestDetail() {
        return testDetail;
    }

    public void setTestDetail(String testDetail) {
        this.testDetail = testDetail;
    }

    public String getTESTNAME() {
        return TESTNAME;
    }

    public void setTESTNAME(String TESTNAME) {
        this.TESTNAME = TESTNAME;
    }

    public String getLabname() {
        return labname;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    @Override
    public String toString() {
        return TESTNAME;

    }
}
