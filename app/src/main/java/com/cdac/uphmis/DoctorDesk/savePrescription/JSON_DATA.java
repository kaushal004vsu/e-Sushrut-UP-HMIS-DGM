package com.cdac.uphmis.DoctorDesk.savePrescription;

import com.google.gson.annotations.SerializedName;

import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;


public class JSON_DATA
{
    @SerializedName("pat_data")
    private DoctorReqListDetails pat_data;

    @SerializedName("pres_data")
    private Pres_data pres_data;

    public Pres_data getPres_data() {
        return pres_data;
    }

    public void setPres_data(Pres_data pres_data) {
        this.pres_data = pres_data;
    }

    public DoctorReqListDetails getPat_data ()
    {
        return pat_data;
    }

    public void setPat_data (DoctorReqListDetails pat_data)
    {
        this.pat_data = pat_data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pat_data = "+pat_data+"]";
    }
}