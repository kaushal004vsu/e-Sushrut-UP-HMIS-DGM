package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StrSystematicExamniation {

@SerializedName("strcvs")
@Expose
private String strcvs="";
@SerializedName("strrs")
@Expose
private String strrs="";
@SerializedName("strcns")
@Expose
private String strcns="";
@SerializedName("strpA")
@Expose
private String strpA="";
@SerializedName("strotherExamn")
@Expose
private String strotherExamn="";
@SerializedName("strmuscularExamn")
@Expose
private String strmuscularExamn="";
@SerializedName("strLocalExamn")
@Expose
private String strLocalExamn="";

public String getStrcvs() {
return strcvs;
}

public void setStrcvs(String strcvs) {
this.strcvs = strcvs;
}

public String getStrrs() {
return strrs;
}

public void setStrrs(String strrs) {
this.strrs = strrs;
}

public String getStrcns() {
return strcns;
}

public void setStrcns(String strcns) {
this.strcns = strcns;
}

public String getStrpA() {
return strpA;
}

public void setStrpA(String strpA) {
this.strpA = strpA;
}

public String getStrotherExamn() {
return strotherExamn;
}

public void setStrotherExamn(String strotherExamn) {
this.strotherExamn = strotherExamn;
}

public String getStrmuscularExamn() {
return strmuscularExamn;
}

public void setStrmuscularExamn(String strmuscularExamn) {
this.strmuscularExamn = strmuscularExamn;
}

public String getStrLocalExamn() {
return strLocalExamn;
}

public void setStrLocalExamn(String strLocalExamn) {
this.strLocalExamn = strLocalExamn;
}

}