package com.cdac.uphmis.opdLite.model;

import android.util.Log;

import com.google.gson.annotations.Expose;

import static com.cdac.uphmis.opdLite.util.opdJsonUtility.sideNameToCode;

public class InvestigationJsonArray {

    private InvestigationDetails investigationDetails;
    private String side;
    private String remarks;
    private boolean isBoolExternal;

    @Expose
    private String EpisodeCode;//": "10512015",
    @Expose
    private String IsExternal;//": "0",
    @Expose
    private String IsTestGroup;//": "0",
    @Expose
    private String LabCode;//": "10003",
    @Expose
    private String LabName;//": "Biochemistry-Biochemistry",
    @Expose
    private String SampleCode;//": "1002",
    @Expose
    private String SampleName;//": "BLD",
    @Expose
    private String SideCode;//": "0",
    @Expose
    private String SideName;//": "Side",
    @Expose
    private String SideRemarks;//": "",
    @Expose
    private String TestCode;//": "10008",
    @Expose
    private String TestName;//": "Alkaline Phosphatase (ALP) (1002) ",
    @Expose
    private String VisitNo;//": "1"

    public InvestigationJsonArray(InvestigationDetails investigationDetails, String side, String remarks, boolean isBoolExternal, String episodeCode, String visitNo) {
        this.investigationDetails = investigationDetails;
        this.side = side;
        this.remarks = remarks;
        this.isBoolExternal = isBoolExternal;

        //emr parameters
        this.EpisodeCode = episodeCode;
        this.IsExternal = (isBoolExternal) ? "1" : "0";
        this.IsTestGroup = "0";
        Log.i("inestigationdetails", "InvestigationJsonArray: " + investigationDetails.getTestDetail());
        this.LabCode = investigationDetails.getTestDetail().split("\\^")[1];
        this.LabName = investigationDetails.getLabname();
        this.SampleCode = investigationDetails.getTestDetail().split("\\^")[2];
        this.SampleName = investigationDetails.getTestDetail().split("\\^")[3];
        this.SideCode = sideNameToCode(side);
        this.SideName = side;
        this.SideRemarks = remarks;
        this.TestCode = investigationDetails.getTestDetail().split("\\^")[0];
        this.TestName = investigationDetails.getTESTNAME();
        this.VisitNo = visitNo;


    }

    public String getEpisodeCode() {
        return EpisodeCode;
    }

    public void setEpisodeCode(String episodeCode) {
        EpisodeCode = episodeCode;
    }

    public String getIsExternal() {
        return IsExternal;
    }

    public void setIsExternal(String isExternal) {
        IsExternal = isExternal;
    }

    public String getIsTestGroup() {
        return IsTestGroup;
    }

    public void setIsTestGroup(String isTestGroup) {
        IsTestGroup = isTestGroup;
    }

    public String getLabCode() {
        return LabCode;
    }

    public void setLabCode(String labCode) {
        LabCode = labCode;
    }

    public String getLabName() {
        return LabName;
    }

    public void setLabName(String labName) {
        LabName = labName;
    }

    public String getSampleCode() {
        return SampleCode;
    }

    public void setSampleCode(String sampleCode) {
        SampleCode = sampleCode;
    }

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String sampleName) {
        SampleName = sampleName;
    }

    public String getSideCode() {
        return SideCode;
    }

    public void setSideCode(String sideCode) {
        SideCode = sideCode;
    }

    public String getSideName() {
        return SideName;
    }

    public void setSideName(String sideName) {
        SideName = sideName;
    }

    public String getSideRemarks() {
        return SideRemarks;
    }

    public void setSideRemarks(String sideRemarks) {
        SideRemarks = sideRemarks;
    }

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public String getVisitNo() {
        return VisitNo;
    }

    public void setVisitNo(String visitNo) {
        VisitNo = visitNo;
    }

    public boolean isBoolExternal() {
        return isBoolExternal;
    }

    public void setBoolExternal(boolean boolExternal) {
        isBoolExternal = boolExternal;
    }

    public InvestigationDetails getInvestigationDetails() {
        return investigationDetails;
    }

    public void setInvestigationDetails(InvestigationDetails investigationDetails) {
        this.investigationDetails = investigationDetails;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public String toString() {
        Log.i("side", "toString: " + side);
        Log.i("remarks", "toString: " + remarks);
        String finalString ="";
//        if (this.side.trim().equalsIgnoreCase("Side")) {
//            this.side = "";
//        }
        if (!side.trim().isEmpty()&&!side.trim().equalsIgnoreCase("Side")) {
            finalString += " (" + this.side + ")";
        }
        if (!this.remarks.trim().isEmpty()) {
            finalString = " (" + this.remarks + ")";
        }


        if (isBoolExternal) {
            return investigationDetails.getTestDetail() + "^" + investigationDetails.getTESTNAME() + "^" + investigationDetails.getTESTNAME() +finalString+"*";

        }

//    return investigationDetails.getTestDetail() + "^" + investigationDetails.getTESTNAME() + " (" + side + ") " + " (" + remarks + ")";
        return investigationDetails.getTestDetail() + "^" + investigationDetails.getTESTNAME() + finalString;
    }

}
