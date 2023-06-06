package com.cdac.uphmis.drugavailability.model;

public class ItemBrandDetails {

    private String ITEM_BRAND_ID;
    private String BRAND_NAME;

    public ItemBrandDetails(String ITEM_BRAND_ID, String BRAND_NAME) {
        this.ITEM_BRAND_ID = ITEM_BRAND_ID;
        this.BRAND_NAME = BRAND_NAME;
    }

    public String getITEM_BRAND_ID() {
        return ITEM_BRAND_ID;
    }

    public void setITEM_BRAND_ID(String ITEM_BRAND_ID) {
        this.ITEM_BRAND_ID = ITEM_BRAND_ID;
    }

    public String getBRAND_NAME() {
        return BRAND_NAME;
    }

    public void setBRAND_NAME(String BRAND_NAME) {
        this.BRAND_NAME = BRAND_NAME;
    }

    @Override
    public String toString() {
        return BRAND_NAME;
    }
}
