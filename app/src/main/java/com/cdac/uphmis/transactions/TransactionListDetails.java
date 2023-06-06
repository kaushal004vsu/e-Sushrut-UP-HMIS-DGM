package com.cdac.uphmis.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionListDetails {
    @SerializedName("TRANS_NO")
    @Expose
    private String transNo;

    @SerializedName("TRANS_DATE")
    @Expose
    private String transDate;

    @SerializedName("RECIEPTNO")
    @Expose
    private String recieptNo;

    @SerializedName("DEPOSIT")
    @Expose
    private String deposit;

    @SerializedName("DEDUCTED")
    @Expose
    private String deducted;

    @SerializedName("HOSP_CODE")
    @Expose
    private String hospCode;

    @SerializedName("HOSP_NAME")
    @Expose
    private String hospName;

    public TransactionListDetails(String transNo, String transDate, String recieptNo, String deposit, String deducted, String hospCode, String hospName) {
        this.transNo = transNo;
        this.transDate = transDate;
        this.recieptNo = recieptNo;
        this.deposit = deposit;
        this.deducted = deducted;
        this.hospCode = hospCode;
        this.hospName = hospName;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getRecieptNo() {
        return recieptNo;
    }

    public void setRecieptNo(String recieptNo) {
        this.recieptNo = recieptNo;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getDeducted() {
        return deducted;
    }

    public void setDeducted(String deducted) {
        this.deducted = deducted;
    }

    public String getHospCode() {
        return hospCode;
    }

    public void setHospCode(String hospCode) {
        this.hospCode = hospCode;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }
}
