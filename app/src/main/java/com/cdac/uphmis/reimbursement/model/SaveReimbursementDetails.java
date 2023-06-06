package com.cdac.uphmis.reimbursement.model;

public class SaveReimbursementDetails {

    private String crno;
    private String umid;
    private String treatmentDtl;
    private String instituteCode;
    private String instituteName;
    private String claimNo;
    private String reqSlNo;
    private String RefNo;
    private String lstModDate;
    private String status;
    private String submitToHospCode;
    private String reimbursementType;
    private String remarks;
    private String diagnosis;
    private String settlementDate;
    private String rejectionDate;
    private String claimRejectionDate;
    private String claimRevocationDate;
    private String approvedBy;
    private String billedAmount;
    private String claimedAmount;
    private String billNo;
    private String admssionDate;
    private String dischargeDate;
    private String completionDate;
    private String diagnosisDtl;
    //section b
    private String isCtse;
    private String isClaimResubmitted;
    private String isEmergency;
    private String isOldClaim;
    private String isHavingInsurance;
    private String isInsuranceClaimed;
    private String isAccidentalVerified;
    //section c
    private String dischargeSummaryPno;
    private String billSummaryPno;
    private String cashVoucherPno;
    private String outerEnclosePno;
    private String otherEnclosePno;
    private String outerPouchPno;
    private String seatId;
    private String lastModSeatId;
    private String empty3;
    private String empty4;
    private String empty5;
    private String gnum_sanctioned_amt;
    private String gnum_forward_to_user;

    public SaveReimbursementDetails() {
    }

    public SaveReimbursementDetails(String crno, String umid, String treatmentDtl, String instituteCode, String instituteName, String claimNo, String reqSlNo, String refNo, String lstModDate, String status, String submitToHospCode, String reimbursementType, String remarks, String diagnosis, String settlementDate, String rejectionDate, String claimRejectionDate, String claimRevocationDate, String approvedBy, String billedAmount, String claimedAmount, String billNo, String admssionDate, String dischargeDate, String completionDate, String diagnosisDtl, String isCtse, String isClaimResubmitted, String isEmergency, String isOldClaim, String isHavingInsurance, String isInsuranceClaimed, String isAccidentalVerified, String dischargeSummaryPno, String billSummaryPno, String cashVoucherPno, String outerEnclosePno, String otherEnclosePno, String outerPouchPno, String seatId, String lastModSeatId, String empty3, String empty4, String empty5, String gnum_sanctioned_amt, String gnum_forward_to_user) {
        this.crno = crno;
        this.umid = umid;
        this.treatmentDtl = treatmentDtl;
        this.instituteCode = instituteCode;
        this.instituteName = instituteName;
        this.claimNo = claimNo;
        this.reqSlNo = reqSlNo;
        RefNo = refNo;
        this.lstModDate = lstModDate;
        this.status = status;
        this.submitToHospCode = submitToHospCode;
        this.reimbursementType = reimbursementType;
        this.remarks = remarks;
        this.diagnosis = diagnosis;
        this.settlementDate = settlementDate;
        this.rejectionDate = rejectionDate;
        this.claimRejectionDate = claimRejectionDate;
        this.claimRevocationDate = claimRevocationDate;
        this.approvedBy = approvedBy;
        this.billedAmount = billedAmount;
        this.claimedAmount = claimedAmount;
        this.billNo = billNo;
        this.admssionDate = admssionDate;
        this.dischargeDate = dischargeDate;
        this.completionDate = completionDate;
        this.diagnosisDtl = diagnosisDtl;
        this.isCtse = isCtse;
        this.isClaimResubmitted = isClaimResubmitted;
        this.isEmergency = isEmergency;
        this.isOldClaim = isOldClaim;
        this.isHavingInsurance = isHavingInsurance;
        this.isInsuranceClaimed = isInsuranceClaimed;
        this.isAccidentalVerified = isAccidentalVerified;
        this.dischargeSummaryPno = dischargeSummaryPno;
        this.billSummaryPno = billSummaryPno;
        this.cashVoucherPno = cashVoucherPno;
        this.outerEnclosePno = outerEnclosePno;
        this.otherEnclosePno = otherEnclosePno;
        this.outerPouchPno = outerPouchPno;
        this.seatId = seatId;
        this.lastModSeatId = lastModSeatId;
        this.empty3 = empty3;
        this.empty4 = empty4;
        this.empty5 = empty5;
        this.gnum_sanctioned_amt = gnum_sanctioned_amt;
        this.gnum_forward_to_user = gnum_forward_to_user;
    }


    public String getCrno() {
        return crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getUmid() {
        return umid;
    }

    public void setUmid(String umid) {
        this.umid = umid;
    }

    public String getTreatmentDtl() {
        return treatmentDtl;
    }

    public void setTreatmentDtl(String treatmentDtl) {
        this.treatmentDtl = treatmentDtl;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public void setInstituteCode(String instituteCode) {
        this.instituteCode = instituteCode;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getReqSlNo() {
        return reqSlNo;
    }

    public void setReqSlNo(String reqSlNo) {
        this.reqSlNo = reqSlNo;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getLstModDate() {
        return lstModDate;
    }

    public void setLstModDate(String lstModDate) {
        this.lstModDate = lstModDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitToHospCode() {
        return submitToHospCode;
    }

    public void setSubmitToHospCode(String submitToHospCode) {
        this.submitToHospCode = submitToHospCode;
    }

    public String getReimbursementType() {
        return reimbursementType;
    }

    public void setReimbursementType(String reimbursementType) {
        this.reimbursementType = reimbursementType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(String rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public String getClaimRejectionDate() {
        return claimRejectionDate;
    }

    public void setClaimRejectionDate(String claimRejectionDate) {
        this.claimRejectionDate = claimRejectionDate;
    }

    public String getClaimRevocationDate() {
        return claimRevocationDate;
    }

    public void setClaimRevocationDate(String claimRevocationDate) {
        this.claimRevocationDate = claimRevocationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(String billedAmount) {
        this.billedAmount = billedAmount;
    }

    public String getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(String claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getAdmssionDate() {
        return admssionDate;
    }

    public void setAdmssionDate(String admssionDate) {
        this.admssionDate = admssionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getDiagnosisDtl() {
        return diagnosisDtl;
    }

    public void setDiagnosisDtl(String diagnosisDtl) {
        this.diagnosisDtl = diagnosisDtl;
    }

    public String getIsCtse() {
        return isCtse;
    }

    public void setIsCtse(String isCtse) {
        this.isCtse = isCtse;
    }

    public String getIsClaimResubmitted() {
        return isClaimResubmitted;
    }

    public void setIsClaimResubmitted(String isClaimResubmitted) {
        this.isClaimResubmitted = isClaimResubmitted;
    }

    public String getIsEmergency() {
        return isEmergency;
    }

    public void setIsEmergency(String isEmergency) {
        this.isEmergency = isEmergency;
    }

    public String getIsOldClaim() {
        return isOldClaim;
    }

    public void setIsOldClaim(String isOldClaim) {
        this.isOldClaim = isOldClaim;
    }

    public String getIsHavingInsurance() {
        return isHavingInsurance;
    }

    public void setIsHavingInsurance(String isHavingInsurance) {
        this.isHavingInsurance = isHavingInsurance;
    }

    public String getIsInsuranceClaimed() {
        return isInsuranceClaimed;
    }

    public void setIsInsuranceClaimed(String isInsuranceClaimed) {
        this.isInsuranceClaimed = isInsuranceClaimed;
    }

    public String getIsAccidentalVerified() {
        return isAccidentalVerified;
    }

    public void setIsAccidentalVerified(String isAccidentalVerified) {
        this.isAccidentalVerified = isAccidentalVerified;
    }

    public String getDischargeSummaryPno() {
        return dischargeSummaryPno;
    }

    public void setDischargeSummaryPno(String dischargeSummaryPno) {
        this.dischargeSummaryPno = dischargeSummaryPno;
    }

    public String getBillSummaryPno() {
        return billSummaryPno;
    }

    public void setBillSummaryPno(String billSummaryPno) {
        this.billSummaryPno = billSummaryPno;
    }

    public String getCashVoucherPno() {
        return cashVoucherPno;
    }

    public void setCashVoucherPno(String cashVoucherPno) {
        this.cashVoucherPno = cashVoucherPno;
    }

    public String getOuterEnclosePno() {
        return outerEnclosePno;
    }

    public void setOuterEnclosePno(String outerEnclosePno) {
        this.outerEnclosePno = outerEnclosePno;
    }

    public String getOtherEnclosePno() {
        return otherEnclosePno;
    }

    public void setOtherEnclosePno(String otherEnclosePno) {
        this.otherEnclosePno = otherEnclosePno;
    }

    public String getOuterPouchPno() {
        return outerPouchPno;
    }

    public void setOuterPouchPno(String outerPouchPno) {
        this.outerPouchPno = outerPouchPno;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getLastModSeatId() {
        return lastModSeatId;
    }

    public void setLastModSeatId(String lastModSeatId) {
        this.lastModSeatId = lastModSeatId;
    }

    public String getEmpty3() {
        return empty3;
    }

    public void setEmpty3(String empty3) {
        this.empty3 = empty3;
    }

    public String getEmpty4() {
        return empty4;
    }

    public void setEmpty4(String empty4) {
        this.empty4 = empty4;
    }

    public String getEmpty5() {
        return empty5;
    }

    public void setEmpty5(String empty5) {
        this.empty5 = empty5;
    }

    public String getGnum_sanctioned_amt() {
        return gnum_sanctioned_amt;
    }

    public void setGnum_sanctioned_amt(String gnum_sanctioned_amt) {
        this.gnum_sanctioned_amt = gnum_sanctioned_amt;
    }

    public String getGnum_forward_to_user() {
        return gnum_forward_to_user;
    }

    public void setGnum_forward_to_user(String gnum_forward_to_user) {
        this.gnum_forward_to_user = gnum_forward_to_user;
    }
}
