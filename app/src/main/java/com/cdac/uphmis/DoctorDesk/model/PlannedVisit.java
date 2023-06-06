package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlannedVisit {

@SerializedName("plannedVisitSos")
@Expose
private String plannedVisitSos="";
@SerializedName("plannedVisitDays")
@Expose
private String plannedVisitDays="";
@SerializedName("plannedVisitDate")
@Expose
private String plannedVisitDate="";

public String getPlannedVisitSos() {
return plannedVisitSos;
}

public void setPlannedVisitSos(String plannedVisitSos) {
this.plannedVisitSos = plannedVisitSos;
}

public String getPlannedVisitDays() {
return plannedVisitDays;
}

public void setPlannedVisitDays(String plannedVisitDays) {
this.plannedVisitDays = plannedVisitDays;
}

public String getPlannedVisitDate() {
return plannedVisitDate;
}

public void setPlannedVisitDate(String plannedVisitDate) {
this.plannedVisitDate = plannedVisitDate;
}

}