package com.cdac.uphmis.qms;



import java.io.Serializable;

public class TokenListDetails implements Serializable {
    private String TOKEN_NO;
    private String SERVICE_ID;
    private String TOKEN_STATUS;
    private String COUNTER_NO;
    private String COUNTER_NAME;
    private String HOSP_CODE;
    private String HOSP_NAME;


    public TokenListDetails(String TOKEN_NO, String SERVICE_ID, String TOKEN_STATUS, String COUNTER_NO, String COUNTER_NAME, String HOSP_CODE, String HOSP_NAME) {
        this.TOKEN_NO = TOKEN_NO;
        this.SERVICE_ID = SERVICE_ID;
        this.TOKEN_STATUS = TOKEN_STATUS;
        this.COUNTER_NO = COUNTER_NO;
        this.COUNTER_NAME = COUNTER_NAME;
        this.HOSP_CODE = HOSP_CODE;
        this.HOSP_NAME = HOSP_NAME;
    }

    public String getTOKEN_NO() {
        return TOKEN_NO;
    }

    public void setTOKEN_NO(String TOKEN_NO) {
        this.TOKEN_NO = TOKEN_NO;
    }

    public String getSERVICE_ID() {
        return SERVICE_ID;
    }

    public void setSERVICE_ID(String SERVICE_ID) {
        this.SERVICE_ID = SERVICE_ID;
    }

    public String getTOKEN_STATUS() {
        return TOKEN_STATUS;
    }

    public void setTOKEN_STATUS(String TOKEN_STATUS) {
        this.TOKEN_STATUS = TOKEN_STATUS;
    }

    public String getCOUNTER_NO() {
        return COUNTER_NO;
    }

    public void setCOUNTER_NO(String COUNTER_NO) {
        this.COUNTER_NO = COUNTER_NO;
    }

    public String getCOUNTER_NAME() {
        return COUNTER_NAME;
    }

    public void setCOUNTER_NAME(String COUNTER_NAME) {
        this.COUNTER_NAME = COUNTER_NAME;
    }

    public String getHOSP_CODE() {
        return HOSP_CODE;
    }

    public void setHOSP_CODE(String HOSP_CODE) {
        this.HOSP_CODE = HOSP_CODE;
    }

    public String getHOSP_NAME() {
        return HOSP_NAME;
    }

    public void setHOSP_NAME(String HOSP_NAME) {
        this.HOSP_NAME = HOSP_NAME;
    }


}

