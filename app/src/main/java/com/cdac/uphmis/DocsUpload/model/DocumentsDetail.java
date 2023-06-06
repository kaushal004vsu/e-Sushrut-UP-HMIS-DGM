
package com.cdac.uphmis.DocsUpload.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentsDetail {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("all_data")
    @Expose
    private List<AllDatum> allData = null;
    @SerializedName("recent")
    @Expose
    private List<Recent> recent = null;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<AllDatum> getAllData() {
        return allData;
    }

    public void setAllData(List<AllDatum> allData) {
        this.allData = allData;
    }

    public List<Recent> getRecent() {
        return recent;
    }

    public void setRecent(List<Recent> recent) {
        this.recent = recent;
    }

}
