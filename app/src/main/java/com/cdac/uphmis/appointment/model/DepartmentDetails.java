package com.cdac.uphmis.appointment.model;


public class DepartmentDetails {

    private String unitcode, loCode, loName, deptname, workingDays, newPatPortalLimit, oldpatportallimit, loweragelimit, maxAgeLimit, isrefer, actualparameterreferenceid, boundGenderCode, isTeleconsUnit, unitType, unitTypeCode, tariffId, charge,inchargeName;

    public String getActualparameterreferenceid() {
        return actualparameterreferenceid;
    }

    public void setActualparameterreferenceid(String actualparameterreferenceid) {
        this.actualparameterreferenceid = actualparameterreferenceid;
    }

    public String getMaxAgeLimit() {
        return maxAgeLimit;
    }

    public void setMaxAgeLimit(String maxAgeLimit) {
        this.maxAgeLimit = maxAgeLimit;
    }

    public DepartmentDetails(String unitcode, String loCode, String loName, String deptname, String workingDays, String newPatPortalLimit, String oldpatportallimit, String loweragelimit, String maxAgeLimit, String isrefer, String actualparameterreferenceid, String boundGenderCode, String isTeleconsUnit, String unitType, String unitTypeCode, String tariffId, String charge,String inchargeName) {
        this.unitcode = unitcode;
        this.loCode = loCode;
        this.loName = loName;
        this.deptname = deptname;
        this.workingDays = workingDays;
        this.newPatPortalLimit = newPatPortalLimit;
        this.oldpatportallimit = oldpatportallimit;
        this.loweragelimit = loweragelimit;
        this.maxAgeLimit = maxAgeLimit;

        this.isrefer = isrefer;

        this.actualparameterreferenceid = actualparameterreferenceid;
        this.boundGenderCode = boundGenderCode;
        this.isTeleconsUnit = isTeleconsUnit;
        this.unitType = unitType;
        this.unitTypeCode = unitTypeCode;
        this.tariffId = tariffId;
        this.charge = charge;
        this.inchargeName=inchargeName;
    }


    public String getInchargeName() {
        return inchargeName;
    }

    public void setInchargeName(String inchargeName) {
        this.inchargeName = inchargeName;
    }

    public String getBoundGenderCode() {
        return boundGenderCode;
    }

    public void setBoundGenderCode(String boundGenderCode) {
        this.boundGenderCode = boundGenderCode;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getLoCode() {
        return loCode;
    }

    public void setLoCode(String loCode) {
        this.loCode = loCode;
    }

    public String getLoName() {
        return loName;
    }

    public void setLoName(String loName) {
        this.loName = loName;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getNewPatPortalLimit() {
        return newPatPortalLimit;
    }

    public void setNewPatPortalLimit(String newPatPortalLimit) {
        this.newPatPortalLimit = newPatPortalLimit;
    }

    public String getOldpatportallimit() {
        return oldpatportallimit;
    }

    public void setOldpatportallimit(String oldpatportallimit) {
        this.oldpatportallimit = oldpatportallimit;
    }

    public String getLoweragelimit() {
        return loweragelimit;
    }

    public void setLoweragelimit(String loweragelimit) {
        this.loweragelimit = loweragelimit;
    }

    public String getIsrefer() {
        return isrefer;
    }

    public void setIsrefer(String isrefer) {
        this.isrefer = isrefer;
    }


    public String getIsTeleconsUnit() {
        return isTeleconsUnit;
    }

    public void setIsTeleconsUnit(String isTeleconsUnit) {
        this.isTeleconsUnit = isTeleconsUnit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitTypeCode() {
        return unitTypeCode;
    }

    public void setUnitTypeCode(String unitTypeCode) {
        this.unitTypeCode = unitTypeCode;
    }

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return this.deptname;
    }


}
