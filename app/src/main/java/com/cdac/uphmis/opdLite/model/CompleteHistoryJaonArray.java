package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteHistoryJaonArray {

@SerializedName("strpastHistory")
@Expose
private String strpastHistory;
@SerializedName("strpersonalHistory")
@Expose
private String strpersonalHistory;
@SerializedName("strfamilyHistory")
@Expose
private String strfamilyHistory;
@SerializedName("strtreatmentHistory")
@Expose
private String strtreatmentHistory;
@SerializedName("strsurgicalHistory")
@Expose
private String strsurgicalHistory;

    public CompleteHistoryJaonArray() {
    }

    public CompleteHistoryJaonArray(String strpastHistory, String strpersonalHistory, String strfamilyHistory, String strtreatmentHistory, String strsurgicalHistory) {
        this.strpastHistory = strpastHistory;
        this.strpersonalHistory = strpersonalHistory;
        this.strfamilyHistory = strfamilyHistory;
        this.strtreatmentHistory = strtreatmentHistory;
        this.strsurgicalHistory = strsurgicalHistory;
    }

    public String getStrpastHistory() {
return strpastHistory;
}

public void setStrpastHistory(String strpastHistory) {
this.strpastHistory = strpastHistory;
}

public String getStrpersonalHistory() {
return strpersonalHistory;
}

public void setStrpersonalHistory(String strpersonalHistory) {
this.strpersonalHistory = strpersonalHistory;
}

public String getStrfamilyHistory() {
return strfamilyHistory;
}

public void setStrfamilyHistory(String strfamilyHistory) {
this.strfamilyHistory = strfamilyHistory;
}

public String getStrtreatmentHistory() {
return strtreatmentHistory;
}

public void setStrtreatmentHistory(String strtreatmentHistory) {
this.strtreatmentHistory = strtreatmentHistory;
}

public String getStrsurgicalHistory() {
return strsurgicalHistory;
}

public void setStrsurgicalHistory(String strsurgicalHistory) {
this.strsurgicalHistory = strsurgicalHistory;
}

}