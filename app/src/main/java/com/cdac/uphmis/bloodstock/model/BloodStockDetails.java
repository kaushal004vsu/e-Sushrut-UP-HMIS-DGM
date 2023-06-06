package com.cdac.uphmis.bloodstock.model;

/**
 * Created by sudeep on 26-01-2019.
 */

public class BloodStockDetails {
    private String Oneg;
    private String Aneg;
    private String Opos;
    private String Apos;
    private String ABpos;
    private String Bpos;
    private String Bneg;
    private String bloodComponent;
    private String ABneg;

    public String getABneg() {
        return ABneg;
    }

    public void setABneg(String ABneg) {
        this.ABneg = ABneg;
    }

    @Override
    public String toString() {
        return "BloodStockDetails{" +
                "Oneg='" + Oneg + '\'' +
                ", Aneg='" + Aneg + '\'' +
                ", Opos='" + Opos + '\'' +
                ", Apos='" + Apos + '\'' +
                ", ABpos='" + ABpos + '\'' +
                ", Bpos='" + Bpos + '\'' +
                ", Bneg='" + Bneg + '\'' +
                ", bloodComponent='" + bloodComponent + '\'' +
                '}';
    }

    public String getBloodComponent() {
        return bloodComponent;
    }

    public void setBloodComponent(String bloodComponent) {
        this.bloodComponent = bloodComponent;
    }

    public  BloodStockDetails(String bloodComponent, String oneg, String aneg, String opos, String apos, String ABpos, String bpos, String bneg,String ABneg) {
        this.bloodComponent=bloodComponent;
        this.Oneg = oneg;
        this.Aneg = aneg;
        this.Opos = opos;
        this.Apos = apos;
        this.ABpos = ABpos;
        this.Bpos = bpos;
        this.Bneg = bneg;
        this.ABneg=ABneg;
    }

    public String getOneg() {
        return Oneg;
    }

    public void setOneg(String oneg) {
        Oneg = oneg;
    }

    public String getAneg() {
        return Aneg;
    }

    public void setAneg(String aneg) {
        Aneg = aneg;
    }

    public String getOpos() {
        return Opos;
    }

    public void setOpos(String opos) {
        Opos = opos;
    }

    public String getApos() {
        return Apos;
    }

    public void setApos(String apos) {
        Apos = apos;
    }

    public String getABpos() {
        return ABpos;
    }

    public void setABpos(String ABpos) {
        this.ABpos = ABpos;
    }

    public String getBpos() {
        return Bpos;
    }

    public void setBpos(String bpos) {
        Bpos = bpos;
    }

    public String getBneg() {
        return Bneg;
    }

    public void setBneg(String bneg) {
        Bneg = bneg;
    }
}
