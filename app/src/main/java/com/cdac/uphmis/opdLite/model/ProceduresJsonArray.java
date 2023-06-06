package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;

public class ProceduresJsonArray {
    private ProcedureDetails procedureDetails;
    private  String side;
    private  String remarks;
    private  boolean isbExternal;

    @Expose
    private String IsExternal;//:"0",
    @Expose
    private String ProcedureCode;//:"215^0^0^0^0^1^9391141",
    @Expose
    private String ProcedureSideCode;//:"1",
    @Expose
    private String ProcedureSideName;//:"NR",
    @Expose
    private String ProcedureSideRemarks;//:"3d nr",
    @Expose
    private String ProceduresName;//:"3d Crt (General Ward)",
    @Expose
    private String ServiceAreaCode;//:"0",
    @Expose
    private String ServiceAreaName;//:"Select "


    public ProceduresJsonArray(ProcedureDetails procedureDetails, String side, String remarks, boolean isbExternal) {
        this.procedureDetails = procedureDetails;
        this.side = side;
        this.remarks = remarks;
        this.isbExternal = isbExternal;

        this.IsExternal=(isbExternal)?"1":"0";
        this.ProcedureCode=procedureDetails.getProcedureDetail();
        this.ProcedureSideCode=side.substring(0,side.lastIndexOf("#"));
        this.ProcedureSideName=side.substring(side.lastIndexOf("#")+1);
        this.ProcedureSideRemarks=remarks;
        this.ProceduresName=procedureDetails.getProcedureName();
        this.ServiceAreaCode="0";
        this.ServiceAreaName="Select ";
    }

    public ProcedureDetails getProcedureDetails() {
        return procedureDetails;
    }

    public void setProcedureDetails(ProcedureDetails procedureDetails) {
        this.procedureDetails = procedureDetails;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        if (!isbExternal)
            return procedureDetails.getProcedureName() + "#" + procedureDetails.getProcedureDetail() + "#" + side + "#" + remarks + "#0#Select";
        else

            return procedureDetails.getProcedureName() + "#0^0#" + side + "#" + remarks + "#0#Select";
    }
}
