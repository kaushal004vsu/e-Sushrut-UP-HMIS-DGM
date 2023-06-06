
package com.cdac.uphmis.drugavailability.model.expandableList;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllDatum {

    @SerializedName("encounter_detail")
    @Expose
    private EncounterDetail_1 encounterDetail;
    @SerializedName("medications")
    @Expose
    private List<Medication> medications;

    public EncounterDetail_1 getEncounterDetail() {
        return encounterDetail;
    }

    public void setEncounterDetail(EncounterDetail_1 encounterDetail) {
        this.encounterDetail = encounterDetail;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

}
