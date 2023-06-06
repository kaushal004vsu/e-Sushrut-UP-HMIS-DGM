package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Strpiccle {

@SerializedName("strpallor")
@Expose
private String strpallor="";
@SerializedName("stricterus")
@Expose
private String stricterus="";
@SerializedName("strcyanosis")
@Expose
private String strcyanosis="";
@SerializedName("strclubbing")
@Expose
private String strclubbing="";
@SerializedName("striymphadenopathyId")
@Expose
private String striymphadenopathyId="";
@SerializedName("stredema")
@Expose
private String stredema="";

public String getStrpallor() {
return strpallor;
}

public void setStrpallor(String strpallor) {
this.strpallor = strpallor;
}

public String getStricterus() {
return stricterus;
}

public void setStricterus(String stricterus) {
this.stricterus = stricterus;
}

public String getStrcyanosis() {
return strcyanosis;
}

public void setStrcyanosis(String strcyanosis) {
this.strcyanosis = strcyanosis;
}

public String getStrclubbing() {
return strclubbing;
}

public void setStrclubbing(String strclubbing) {
this.strclubbing = strclubbing;
}

public String getStriymphadenopathyId() {
return striymphadenopathyId;
}

public void setStriymphadenopathyId(String striymphadenopathyId) {
this.striymphadenopathyId = striymphadenopathyId;
}

public String getStredema() {
return stredema;
}

public void setStredema(String stredema) {
this.stredema = stredema;
}

}