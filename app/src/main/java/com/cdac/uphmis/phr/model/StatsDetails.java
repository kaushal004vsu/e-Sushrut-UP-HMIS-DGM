package com.cdac.uphmis.phr.model;

public class StatsDetails {
    String CRNO;
    String SL_NO ;
    String VITAL_NAME;
    String VITAL_ID;
    String VITAL_VALUE ;
    String RECORD_DATE ;
    String VITAL_UNIT;
    String IS_NORMAL ;
    String HNUM_IS_SEVERE;

    public StatsDetails(String CRNO, String SL_NO, String VITAL_NAME, String VITAL_ID, String VITAL_VALUE, String RECORD_DATE, String VITAL_UNIT, String IS_NORMAL, String HNUM_IS_SEVERE) {
        this.CRNO = CRNO;
        this.SL_NO = SL_NO;
        this.VITAL_NAME = VITAL_NAME;
        this.VITAL_ID = VITAL_ID;
        this.VITAL_VALUE = VITAL_VALUE;
        this.RECORD_DATE = RECORD_DATE;
        this.VITAL_UNIT = VITAL_UNIT;
        this.IS_NORMAL = IS_NORMAL;
        this.HNUM_IS_SEVERE = HNUM_IS_SEVERE;
    }

    public String getCRNO() {
        return CRNO;
    }

    public void setCRNO(String CRNO) {
        this.CRNO = CRNO;
    }

    public String getSL_NO() {
        return SL_NO;
    }

    public void setSL_NO(String SL_NO) {
        this.SL_NO = SL_NO;
    }

    public String getVITAL_NAME() {
        return VITAL_NAME;
    }

    public void setVITAL_NAME(String VITAL_NAME) {
        this.VITAL_NAME = VITAL_NAME;
    }

    public String getVITAL_ID() {
        return VITAL_ID;
    }

    public void setVITAL_ID(String VITAL_ID) {
        this.VITAL_ID = VITAL_ID;
    }

    public String getVITAL_VALUE() {
        return VITAL_VALUE;
    }

    public void setVITAL_VALUE(String VITAL_VALUE) {
        this.VITAL_VALUE = VITAL_VALUE;
    }

    public String getRECORD_DATE() {
        return RECORD_DATE;
    }

    public void setRECORD_DATE(String RECORD_DATE) {
        this.RECORD_DATE = RECORD_DATE;
    }

    public String getVITAL_UNIT() {
        return VITAL_UNIT;
    }

    public void setVITAL_UNIT(String VITAL_UNIT) {
        this.VITAL_UNIT = VITAL_UNIT;
    }

    public String getIS_NORMAL() {
        return IS_NORMAL;
    }

    public void setIS_NORMAL(String IS_NORMAL) {
        this.IS_NORMAL = IS_NORMAL;
    }

    public String getHNUM_IS_SEVERE() {
        return HNUM_IS_SEVERE;
    }

    public void setHNUM_IS_SEVERE(String HNUM_IS_SEVERE) {
        this.HNUM_IS_SEVERE = HNUM_IS_SEVERE;
    }
}
