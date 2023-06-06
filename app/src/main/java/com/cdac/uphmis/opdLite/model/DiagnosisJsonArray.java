package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;

import static com.cdac.uphmis.opdLite.util.opdJsonUtility.getTypeNameFromCode;
import static com.cdac.uphmis.opdLite.util.opdJsonUtility.sideCodetoSideName;

public class DiagnosisJsonArray {

    private Diagnosis diagnosis;
    private String side;
    private String type;
    private String remarks;
    private boolean isExternal;

    @Expose
    private String DiagnosisCode;//": "38362002",
    @Expose
    private String DiagnosisName;//": "Dengue",
    @Expose
    private String DiagnosisRemarks;//": "",
    @Expose
    private String DiagnosisSideCode;//": "0",
    @Expose
    private String DiagnosisSideName;//": "Side",
    @Expose
    private String DiagnosisTypeCode;//": "11",
    @Expose
    private String DiagnosisTypeNamee;//": "Provisional",
    @Expose
    private String IsSnomed;//": "1"
    public DiagnosisJsonArray(Diagnosis diagnosis, String side, String type, String remarks, boolean isExternal) {
        this.diagnosis = diagnosis;
        this.side = side;
        this.type = type;
        this.remarks = remarks;
        this.isExternal = isExternal;


        if (diagnosis instanceof DiagnosisDetails) {
            this.DiagnosisCode = ((DiagnosisDetails) diagnosis).getConceptId();
            this.DiagnosisName=((DiagnosisDetails) diagnosis).getTerm();
            this.IsSnomed=(isExternal)?"3":"1";
        }
        if (diagnosis instanceof DiagnosisICDDetails) {
            this.DiagnosisCode = ((DiagnosisICDDetails) diagnosis).getDiseaseCode();
            this.DiagnosisName=((DiagnosisICDDetails) diagnosis).getDiseaseName();
            this.IsSnomed=(isExternal)?"3":"2";
        }

        this.DiagnosisSideCode=side;
        this.DiagnosisTypeCode=type;
        this.DiagnosisSideName=sideCodetoSideName(side);
        this.DiagnosisTypeNamee=getTypeNameFromCode(type);
        this.DiagnosisRemarks=remarks;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public boolean isExternal() {
        return isExternal;
    }

    public void setExternal(boolean external) {
        isExternal = external;
    }

    public Diagnosis getDiagnosisDetails() {
        if (diagnosis instanceof DiagnosisDetails) {
            return (DiagnosisDetails) diagnosis;
        } else if (diagnosis instanceof DiagnosisICDDetails) {
            return (DiagnosisICDDetails) diagnosis;
        } else
            return (DiagnosisDetails) diagnosis;

    }

    public void setDiagnosisDetails(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {

        String typeName=getTypeNameFromCode(type);
        if (diagnosis instanceof DiagnosisDetails) {
            DiagnosisDetails diagnosisDetails = ((DiagnosisDetails) diagnosis);
            if (isExternal) {
                return  "0#0#2^"+diagnosisDetails.getTerm() +"#0#0##0#0";

            }
            return diagnosisDetails.getConceptId() + "#" + type + "#1^" + diagnosisDetails.getTerm() + "#" + typeName + "#" + side + "##0#" + remarks;
        } else {
            DiagnosisICDDetails diagnosisDetails = ((DiagnosisICDDetails) diagnosis);
            if (isExternal) {
                return  "0#0#2^"+diagnosisDetails.getDiseaseName()+"#0#0##0#0";
            }
            return diagnosisDetails.getDiseaseCode() + "#" + type + "#0^" + diagnosisDetails.getDiseaseName() + "#" + typeName + "#" + side + "##0#" + remarks;
        }


    }
}
