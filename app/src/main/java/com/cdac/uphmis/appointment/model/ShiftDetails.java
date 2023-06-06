package com.cdac.uphmis.appointment.model;

public class ShiftDetails {
    private String date,shift,shiftName,shiftst,shiftet,slotst,slotet,freeslotdetail,slotstatus,opdslots,ipdslots,opdbookedslots,ipdbookedslots;

    public String getShiftst() {
        return shiftst;
    }

    public void setShiftst(String shiftst) {
        this.shiftst = shiftst;
    }

    public String getShiftet() {
        return shiftet;
    }

    public void setShiftet(String shiftet) {
        this.shiftet = shiftet;
    }

    public ShiftDetails(String date, String shift, String shiftName, String shiftst, String shiftet, String slotst, String slotet, String freeslotdetail, String slotstatus, String opdslots, String ipdslots, String opdbookedslots, String ipdbookedslots) {
        this.date = date;
        this.shift = shift;
        this.shiftName = shiftName;
        this.shiftst=shiftst;
        this.shiftet=shiftet;

        this.slotst = slotst;
        this.slotet = slotet;
        this.freeslotdetail = freeslotdetail;
        this.slotstatus = slotstatus;
        this.opdslots = opdslots;
        this.ipdslots = ipdslots;
        this.opdbookedslots = opdbookedslots;
        this.ipdbookedslots = ipdbookedslots;
    }

    public ShiftDetails() {
        super();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getSlotst() {
        return slotst;
    }

    public void setSlotst(String slotst) {
        this.slotst = slotst;
    }

    public String getSlotet() {
        return slotet;
    }

    public void setSlotet(String slotet) {
        this.slotet = slotet;
    }

    public String getFreeslotdetail() {
        return freeslotdetail;
    }

    public void setFreeslotdetail(String freeslotdetail) {
        this.freeslotdetail = freeslotdetail;
    }

    public String getSlotstatus() {
        return slotstatus;
    }

    public void setSlotstatus(String slotstatus) {
        this.slotstatus = slotstatus;
    }

    public String getOpdslots() {
        return opdslots;
    }

    public void setOpdslots(String opdslots) {
        this.opdslots = opdslots;
    }

    public String getIpdslots() {
        return ipdslots;
    }

    public void setIpdslots(String ipdslots) {
        this.ipdslots = ipdslots;
    }

    public String getOpdbookedslots() {
        return opdbookedslots;
    }

    public void setOpdbookedslots(String opdbookedslots) {
        this.opdbookedslots = opdbookedslots;
    }

    public String getIpdbookedslots() {
        return ipdbookedslots;
    }

    public void setIpdbookedslots(String ipdbookedslots) {
        this.ipdbookedslots = ipdbookedslots;
    }

    @Override
    public String toString() {
        return this.freeslotdetail;
    }
}
