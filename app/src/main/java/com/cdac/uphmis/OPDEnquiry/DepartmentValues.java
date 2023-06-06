package com.cdac.uphmis.OPDEnquiry;

public class DepartmentValues {
    private String deptName;
    private String unitName;
    private String location;
    private String roomName;
    //private String opdName;
    private String unitDays;
   // private String shiftTime;
    private String rosterType;

    public DepartmentValues(String deptName, String unitName, String location, String roomName, String unitDays, String rosterType) {
        this.deptName = deptName;
        this.unitName = unitName;
        this.location = location;
        this.roomName = roomName;
       // this.opdName = opdName;
        this.unitDays = unitDays;
       // this.shiftTime = shiftTime;
        this.rosterType = rosterType;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

//    public String getOpdName() {
//        return opdName;
//    }
//
//    public void setOpdName(String opdName) {
//        this.opdName = opdName;
//    }

    public String getUnitDays() {
        return unitDays;
    }

    public void setUnitDays(String unitDays) {
        this.unitDays = unitDays;
    }

//    public String getShiftTime() {
//        return shiftTime;
//    }
//
//    public void setShiftTime(String shiftTime) {
//        this.shiftTime = shiftTime;
//    }

    public String getRosterType() {
        return rosterType;
    }

    public void setRosterType(String rosterType) {
        this.rosterType = rosterType;
    }
}
