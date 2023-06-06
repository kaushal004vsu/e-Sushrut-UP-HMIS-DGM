package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FollowUp {

    @SerializedName("endTreatment")
    @Expose
    private String endTreatment="";
    @SerializedName("Planned_Visit")
    @Expose
    private List<PlannedVisit> plannedVisit = null;
    @SerializedName("progressNote")
    @Expose
    private String progressNote="";

    public FollowUp(String endTreatment, List<PlannedVisit> plannedVisit, String progressNote) {
        this.endTreatment = endTreatment;
        this.plannedVisit = plannedVisit;
        this.progressNote = progressNote;
    } public FollowUp(String endTreatment, List<PlannedVisit> plannedVisit) {
        this.endTreatment = endTreatment;
        this.plannedVisit = plannedVisit;

    }

    public FollowUp()
    {

    }
    public String getEndTreatment() {
        return endTreatment;
    }

    public void setEndTreatment(String endTreatment) {
        this.endTreatment = endTreatment;
    }

    public List<PlannedVisit> getPlannedVisit() {
        return plannedVisit;
    }

    public void setPlannedVisit(List<PlannedVisit> plannedVisit) {
        this.plannedVisit = plannedVisit;
    }

    public String getProgressNote() {
        return progressNote;
    }

    public void setProgressNote(String progressNote) {
        this.progressNote = progressNote;
    }

}