package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;

import static com.cdac.uphmis.opdLite.util.opdJsonUtility.durationCodeToName;
import static com.cdac.uphmis.opdLite.util.opdJsonUtility.sideCodetoSideName;

public class ComplaintsJsonArray {
    private ComplaintsDetails complaintsDetails;
    private  String side;
    private  String number;
    private  String duration;
    private  String remarks;


    //for emr json
    @Expose
    private String IsExternalVisit="1";
    @Expose
    private String VisitReasonName="";

    @Expose
    private String VisitReasonCode="0";
    @Expose
    private String VisitReasonSideCode="0";
    @Expose
    private String VisitReasonSideName="Side";
    @Expose
    private String VisitReasonNoOfDays="1";
    @Expose
    private String VisitComplaintDurationCode="1";
    @Expose
    private String VisitComplaintDurationName="Day/s";
    @Expose
    private String VisitReasonRemarks="";
    public ComplaintsJsonArray(ComplaintsDetails complaintsDetails, String side, String number, String duration, String remarks) {
        this.complaintsDetails = complaintsDetails;
        this.side = side;
        this.number = number;
        this.duration = duration;
        this.remarks = remarks;



        this.VisitReasonName=complaintsDetails.getTerm();
        this.VisitReasonCode=complaintsDetails.getConceptId();
        this.VisitReasonSideCode=side;
        this.VisitReasonSideName=sideCodetoSideName(side);
        this.VisitReasonNoOfDays=number;
        this.VisitComplaintDurationCode=duration;
        this.VisitComplaintDurationName=durationCodeToName(duration);
        this.VisitReasonRemarks=remarks;
    }


    public ComplaintsDetails getComplaintsDetails() {
        return complaintsDetails;
    }

   public void setComplaintsDetails(ComplaintsDetails complaintsDetails) {
        this.complaintsDetails = complaintsDetails;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    /*public String getEmrJson()
    {      JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("IsExternalVisit", "1");
            jsonObject.put("VisitReasonName", complaintsDetails.getTerm());
            jsonObject.put("VisitReasonCode", complaintsDetails.getConceptId());
            jsonObject.put("VisitReasonSideCode", "");
            jsonObject.put("VisitReasonSideName","" );
            jsonObject.put("VisitReasonNoOfDays", number);
            jsonObject.put("VisitComplaintDurationCode","" );
            jsonObject.put("VisitComplaintDurationName", duration);
            jsonObject.put("VisitReasonRemarks", remarks);
        }catch (Exception ex){ex.printStackTrace();}
        return jsonObject.toString();
    }*/


    @Override
    public String toString() {
        return complaintsDetails.getConceptId()+"^"+complaintsDetails.getTerm()+"^"+side+"^"+number+"^"+duration+"^"+remarks;
    }
}
