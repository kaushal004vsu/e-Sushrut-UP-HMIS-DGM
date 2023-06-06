
package com.cdac.uphmis.drugavailability.model.expandableList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicationDetail {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("recent")
    @Expose
    private List<Recent> recent;
    @SerializedName("all_data")
    @Expose
    private List<AllDatum> allData;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Recent> getRecent() {
        return recent;
    }

    public void setRecent(List<Recent> recent) {
        this.recent = recent;
    }

    public List<AllDatum> getAllData() {
        return allData;
    }

    public void setAllData(List<AllDatum> allData) {
        this.allData = allData;
    }

}
