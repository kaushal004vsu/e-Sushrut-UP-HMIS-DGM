package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcedureDetails {

    @SerializedName("PROCEDURE_DETAIL")
    @Expose
    private String procedureDetail;
    @SerializedName("PROCEDURE_NAME")
    @Expose
    private String procedureName;

    public String getProcedureDetail() {
        return procedureDetail;
    }

    public void setProcedureDetail(String procedureDetail) {
        this.procedureDetail = procedureDetail;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }


    @Override
    public String toString() {
        return procedureName;
    }
}
