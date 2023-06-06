
package com.cdac.uphmis.drugavailability.model.expandableList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class DrugRxModel {

    @SerializedName("medication_detail")
    @Expose
    private MedicationDetail medicationDetail;

    public MedicationDetail getMedicationDetail() {
        return medicationDetail;
    }

    public void setMedicationDetail(MedicationDetail medicationDetail) {
        this.medicationDetail = medicationDetail;
    }

}
