package com.cdac.uphmis.DoctorDesk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosisJsonArray {

@SerializedName("IsSnomed")
@Expose
private String isSnomed;
@SerializedName("DiagnosisName")
@Expose
private String diagnosisName;
@SerializedName("DiagnosisCode")
@Expose
private String diagnosisCode;
@SerializedName("DiagnosisSideCode")
@Expose
private String diagnosisSideCode;
@SerializedName("DiagnosisSideName")
@Expose
private String diagnosisSideName;
@SerializedName("DiagnosisTypeCode")
@Expose
private String diagnosisTypeCode;
@SerializedName("DiagnosisTypeNamee")
@Expose
private String diagnosisTypeNamee;
@SerializedName("DiagnosisRemarks")
@Expose
private String diagnosisRemarks;

    public DiagnosisJsonArray() {
    }

    public DiagnosisJsonArray(String isSnomed, String diagnosisName, String diagnosisCode, String diagnosisSideCode, String diagnosisSideName, String diagnosisTypeCode, String diagnosisTypeNamee, String diagnosisRemarks) {
        this.isSnomed = isSnomed;
        this.diagnosisName = diagnosisName;
        this.diagnosisCode = diagnosisCode;
        this.diagnosisSideCode = diagnosisSideCode;
        this.diagnosisSideName = diagnosisSideName;
        this.diagnosisTypeCode = diagnosisTypeCode;
        this.diagnosisTypeNamee = diagnosisTypeNamee;
        this.diagnosisRemarks = diagnosisRemarks;
    }

    public String getIsSnomed() {
return isSnomed;
}

public void setIsSnomed(String isSnomed) {
this.isSnomed = isSnomed;
}

public String getDiagnosisName() {
return diagnosisName;
}

public void setDiagnosisName(String diagnosisName) {
this.diagnosisName = diagnosisName;
}

public String getDiagnosisCode() {
return diagnosisCode;
}

public void setDiagnosisCode(String diagnosisCode) {
this.diagnosisCode = diagnosisCode;
}

public String getDiagnosisSideCode() {
return diagnosisSideCode;
}

public void setDiagnosisSideCode(String diagnosisSideCode) {
this.diagnosisSideCode = diagnosisSideCode;
}

public String getDiagnosisSideName() {
return diagnosisSideName;
}

public void setDiagnosisSideName(String diagnosisSideName) {
this.diagnosisSideName = diagnosisSideName;
}

public String getDiagnosisTypeCode() {
return diagnosisTypeCode;
}

public void setDiagnosisTypeCode(String diagnosisTypeCode) {
this.diagnosisTypeCode = diagnosisTypeCode;
}

public String getDiagnosisTypeNamee() {
return diagnosisTypeNamee;
}

public void setDiagnosisTypeNamee(String diagnosisTypeNamee) {
this.diagnosisTypeNamee = diagnosisTypeNamee;
}

public String getDiagnosisRemarks() {
return diagnosisRemarks;
}

public void setDiagnosisRemarks(String diagnosisRemarks) {
this.diagnosisRemarks = diagnosisRemarks;
}

}