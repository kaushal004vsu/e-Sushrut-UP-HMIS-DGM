package com.cdac.uphmis.reimbursement.model;

public class ClaimEnquiryDetails {
    private String HOSP_NAME;
    private String CLAIM_REF_NO;
    private String STATUS;
    private String REIMBURSEMENT_TYPE;
    private String CLAIM_SUBMIT_DT;
    private String APPROVED_DATE;
    private String APPROVED_BY;
    private String REMARKS;
    private String CLAIM_AMT;
    private String SANCTION_AMT;
    private String NEXT_USER;
    private String TO_HOSP_CODE;
    private String CLAIM_REQ_NO;
    private String SL_NO;
    private String TO_HOSP_NAME;
    private String REV_AMT;
    private String STATUS_CODE;

    public ClaimEnquiryDetails(String HOSP_NAME, String CLAIM_REF_NO, String STATUS, String REIMBURSEMENT_TYPE, String CLAIM_SUBMIT_DT, String APPROVED_DATE, String APPROVED_BY, String REMARKS, String CLAIM_AMT, String SANCTION_AMT, String NEXT_USER, String TO_HOSP_CODE, String CLAIM_REQ_NO, String SL_NO, String TO_HOSP_NAME, String REV_AMT, String STATUS_CODE) {
        this.HOSP_NAME = HOSP_NAME;
        this.CLAIM_REF_NO = CLAIM_REF_NO;
        this.STATUS = STATUS;
        this.REIMBURSEMENT_TYPE = REIMBURSEMENT_TYPE;
        this.CLAIM_SUBMIT_DT = CLAIM_SUBMIT_DT;
        this.APPROVED_DATE = APPROVED_DATE;
        this.APPROVED_BY = APPROVED_BY;
        this.REMARKS = REMARKS;
        this.CLAIM_AMT = CLAIM_AMT;
        this.SANCTION_AMT = SANCTION_AMT;
        this.NEXT_USER = NEXT_USER;
        this.TO_HOSP_CODE = TO_HOSP_CODE;
        this.CLAIM_REQ_NO = CLAIM_REQ_NO;
        this.SL_NO = SL_NO;
        this.TO_HOSP_NAME = TO_HOSP_NAME;
        this.REV_AMT = REV_AMT;
        this.STATUS_CODE = STATUS_CODE;
    }

    public ClaimEnquiryDetails() {
    }

    public String getHOSP_NAME() {
        return HOSP_NAME;
    }

    public void setHOSP_NAME(String HOSP_NAME) {
        this.HOSP_NAME = HOSP_NAME;
    }

    public String getCLAIM_REF_NO() {
        return CLAIM_REF_NO;
    }

    public void setCLAIM_REF_NO(String CLAIM_REF_NO) {
        this.CLAIM_REF_NO = CLAIM_REF_NO;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getREIMBURSEMENT_TYPE() {
        return REIMBURSEMENT_TYPE;
    }

    public void setREIMBURSEMENT_TYPE(String REIMBURSEMENT_TYPE) {
        this.REIMBURSEMENT_TYPE = REIMBURSEMENT_TYPE;
    }

    public String getCLAIM_SUBMIT_DT() {
        return CLAIM_SUBMIT_DT;
    }

    public void setCLAIM_SUBMIT_DT(String CLAIM_SUBMIT_DT) {
        this.CLAIM_SUBMIT_DT = CLAIM_SUBMIT_DT;
    }

    public String getAPPROVED_DATE() {
        return APPROVED_DATE;
    }

    public void setAPPROVED_DATE(String APPROVED_DATE) {
        this.APPROVED_DATE = APPROVED_DATE;
    }

    public String getAPPROVED_BY() {
        return APPROVED_BY;
    }

    public void setAPPROVED_BY(String APPROVED_BY) {
        this.APPROVED_BY = APPROVED_BY;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getCLAIM_AMT() {
        return CLAIM_AMT;
    }

    public void setCLAIM_AMT(String CLAIM_AMT) {
        this.CLAIM_AMT = CLAIM_AMT;
    }

    public String getSANCTION_AMT() {
        return SANCTION_AMT;
    }

    public void setSANCTION_AMT(String SANCTION_AMT) {
        this.SANCTION_AMT = SANCTION_AMT;
    }

    public String getNEXT_USER() {
        return NEXT_USER;
    }

    public void setNEXT_USER(String NEXT_USER) {
        this.NEXT_USER = NEXT_USER;
    }

    public String getTO_HOSP_CODE() {
        return TO_HOSP_CODE;
    }

    public void setTO_HOSP_CODE(String TO_HOSP_CODE) {
        this.TO_HOSP_CODE = TO_HOSP_CODE;
    }

    public String getCLAIM_REQ_NO() {
        return CLAIM_REQ_NO;
    }

    public void setCLAIM_REQ_NO(String CLAIM_REQ_NO) {
        this.CLAIM_REQ_NO = CLAIM_REQ_NO;
    }

    public String getSL_NO() {
        return SL_NO;
    }

    public void setSL_NO(String SL_NO) {
        this.SL_NO = SL_NO;
    }

    public String getTO_HOSP_NAME() {
        return TO_HOSP_NAME;
    }

    public void setTO_HOSP_NAME(String TO_HOSP_NAME) {
        this.TO_HOSP_NAME = TO_HOSP_NAME;
    }

    public String getREV_AMT() {
        return REV_AMT;
    }

    public void setREV_AMT(String REV_AMT) {
        this.REV_AMT = REV_AMT;
    }

    public String getSTATUS_CODE() {
        return STATUS_CODE;
    }

    public void setSTATUS_CODE(String STATUS_CODE) {
        this.STATUS_CODE = STATUS_CODE;
    }
}
