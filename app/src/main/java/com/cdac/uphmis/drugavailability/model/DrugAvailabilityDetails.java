package com.cdac.uphmis.drugavailability.model;

/**
 * Created by sudeeprai on 5/15/2019.
 */

public class DrugAvailabilityDetails {
    private String storeName;
    private String drugName;
    private String drugQuantity;

    public DrugAvailabilityDetails(String storeName,String drugName, String drugQuantity) {
        this.storeName=storeName;
        this.drugName = drugName;
        this.drugQuantity = drugQuantity;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugQuantity() {
        return drugQuantity;
    }

    public void setDrugQuantity(String drugQuantity) {
        this.drugQuantity = drugQuantity;
    }
}
