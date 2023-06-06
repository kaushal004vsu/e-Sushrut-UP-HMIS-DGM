package com.cdac.uphmis.opdLite.model;

public class ReasonOfVisitDetails {

    private String IsExternalVisit;//": "1",
    private String VisitReasonName;//": "Fever",
    private String VisitReasonCode;//": "386661006",
    private String VisitReasonSideCode;//": "0",
    private String VisitReasonSideName;//": "Side",
    private String VisitReasonNoOfDays;//": "5",
    private String VisitComplaintDurationCode;//": "2",
    private String VisitComplaintDurationName;//": "Week/s",
    private String VisitReasonRemarks;//": "remarks1"

    public ReasonOfVisitDetails(String isExternalVisit, String visitReasonName, String visitReasonCode, String visitReasonSideCode, String visitReasonSideName, String visitReasonNoOfDays, String visitComplaintDurationCode, String visitComplaintDurationName, String visitReasonRemarks) {
        IsExternalVisit = isExternalVisit;
        VisitReasonName = visitReasonName;
        VisitReasonCode = visitReasonCode;
        VisitReasonSideCode = visitReasonSideCode;
        VisitReasonSideName = visitReasonSideName;
        VisitReasonNoOfDays = visitReasonNoOfDays;
        VisitComplaintDurationCode = visitComplaintDurationCode;
        VisitComplaintDurationName = visitComplaintDurationName;
        VisitReasonRemarks = visitReasonRemarks;
    }

    public String getIsExternalVisit() {
        return IsExternalVisit;
    }

    public void setIsExternalVisit(String isExternalVisit) {
        IsExternalVisit = isExternalVisit;
    }

    public String getVisitReasonName() {
        return VisitReasonName;
    }

    public void setVisitReasonName(String visitReasonName) {
        VisitReasonName = visitReasonName;
    }

    public String getVisitReasonCode() {
        return VisitReasonCode;
    }

    public void setVisitReasonCode(String visitReasonCode) {
        VisitReasonCode = visitReasonCode;
    }

    public String getVisitReasonSideCode() {
        return VisitReasonSideCode;
    }

    public void setVisitReasonSideCode(String visitReasonSideCode) {
        VisitReasonSideCode = visitReasonSideCode;
    }

    public String getVisitReasonSideName() {
        return VisitReasonSideName;
    }

    public void setVisitReasonSideName(String visitReasonSideName) {
        VisitReasonSideName = visitReasonSideName;
    }

    public String getVisitReasonNoOfDays() {
        return VisitReasonNoOfDays;
    }

    public void setVisitReasonNoOfDays(String visitReasonNoOfDays) {
        VisitReasonNoOfDays = visitReasonNoOfDays;
    }

    public String getVisitComplaintDurationCode() {
        return VisitComplaintDurationCode;
    }

    public void setVisitComplaintDurationCode(String visitComplaintDurationCode) {
        VisitComplaintDurationCode = visitComplaintDurationCode;
    }

    public String getVisitComplaintDurationName() {
        return VisitComplaintDurationName;
    }

    public void setVisitComplaintDurationName(String visitComplaintDurationName) {
        VisitComplaintDurationName = visitComplaintDurationName;
    }

    public String getVisitReasonRemarks() {
        return VisitReasonRemarks;
    }

    public void setVisitReasonRemarks(String visitReasonRemarks) {
        VisitReasonRemarks = visitReasonRemarks;
    }
}
