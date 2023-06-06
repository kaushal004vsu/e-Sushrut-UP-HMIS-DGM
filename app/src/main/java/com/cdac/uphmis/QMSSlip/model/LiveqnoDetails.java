package com.cdac.uphmis.QMSSlip.model;

public class LiveqnoDetails {

    private String hospitalName;
    private String deptName;
    private String unitName;
    private String queueNo;
    private String waitingTime;

    public LiveqnoDetails(String hospitalName, String deptName, String unitName, String queueNo, String waitingTime) {
        this.hospitalName = hospitalName;
        this.deptName = deptName;
        this.unitName = unitName;
        this.queueNo = queueNo;
        this.waitingTime = waitingTime;
    }


    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }


    @Override
    public String toString() {
        return deptName;
    }
}
