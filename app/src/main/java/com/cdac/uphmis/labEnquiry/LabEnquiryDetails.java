package com.cdac.uphmis.labEnquiry;

/**
 * Created by sudeeprai on 2/1/2019.
 */

public class LabEnquiryDetails {

   private String testId, testName, labCode, labName, isApptBased,isApptBooking,testCharge;





    public LabEnquiryDetails(String testId, String testName, String labCode, String labName, String isApptBased, String isApptBooking, String testCharge) {
        this.testId = testId;
        this.testName = testName;
        this.labCode = labCode;
        this.labName = labName;
        this.isApptBased = isApptBased;
        this.isApptBooking=isApptBooking;

        this.testCharge = testCharge;
    }
    public String getIsApptBooking() {
        return isApptBooking;
    }

    public void setIsApptBooking(String isApptBooking) {
        this.isApptBooking = isApptBooking;
    }



    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getIsApptBased() {
        return isApptBased;
    }

    public void setIsApptBased(String isApptBased) {
        this.isApptBased = isApptBased;
    }

    public String getTestCharge() {
        return testCharge;
    }

    public void setTestCharge(String testCharge) {
        this.testCharge = testCharge;
    }
}
