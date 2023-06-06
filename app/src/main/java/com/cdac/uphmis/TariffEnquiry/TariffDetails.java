package com.cdac.uphmis.TariffEnquiry;

public class TariffDetails {
    private String patCategory;
  //  private String chargeType;
    private String tariffName;
    private String tariffCharge;

    public TariffDetails(String patCategory, String tariffName, String tariffCharge) {
        this.patCategory = patCategory;
       // this.chargeType = chargeType;
        this.tariffName = tariffName;
        this.tariffCharge = tariffCharge;
    }

    public String getPatCategory() {

        return patCategory;
    }

    public void setPatCategory(String patCategory) {
        this.patCategory = patCategory;
    }

//    public String getChargeType() {
//        return chargeType;
//    }
//
//    public void setChargeType(String chargeType) {
//        this.chargeType = chargeType;
//    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getTariffCharge() {
        return tariffCharge;
    }

    public void setTariffCharge(String tariffCharge) {
        this.tariffCharge = tariffCharge;
    }
}
