package com.cdac.uphmis.appointment.model;

import java.io.Serializable;

/**
 * Created by sudeeprai on 2/18/2019.
 */

public class MyAppointmentDetails implements Serializable {
    private String is_previous_appointment, appointmentno, patcrno, episodecode, patfirstname, patmiddlename, patlastname, patguardianname, patgendercode, emailid, mobileno, appointmentqueueno, appointmenttime, appointmentstatus, statusremarks, slottype, remarks, appointmenttypeid, modulespecificcode, appointmentmode, modulespecifickeyname, patage, patspousename, appointmentdate, appointmentforid, appointmentforname, actulaparaid1, actulaparaid2, actulaparaid3, actulaparaid4, actulaparaid5, actulaparaid6, actulaparaid7, actulaparaname1, actulaparaname2, actulaparaname3, actulaparaname4, actulaparaname5, actulaparaname6, actulaparaname7,isFeesPaid,hospCode,hospName,actualParaRefId;


    public MyAppointmentDetails(String is_previous_appointment, String appointmentno, String patcrno, String episodecode, String patfirstname, String patmiddlename, String patlastname, String patguardianname, String patgendercode, String emailid, String mobileno, String appointmentqueueno, String appointmenttime, String appointmentstatus, String statusremarks, String slottype, String remarks, String appointmenttypeid, String modulespecificcode, String appointmentmode, String modulespecifickeyname, String patage, String patspousename, String appointmentdate, String appointmentforid, String appointmentforname, String actulaparaid1, String actulaparaid2, String actulaparaid3, String actulaparaid4, String actulaparaid5, String actulaparaid6, String actulaparaid7, String actulaparaname1, String actulaparaname2, String actulaparaname3, String actulaparaname4, String actulaparaname5, String actulaparaname6, String actulaparaname7,String isFeesPaid,String hospCode,String hospName,String actualParaRefId) {
        this.is_previous_appointment = is_previous_appointment;
        this.appointmentno = appointmentno;
        this.patcrno = patcrno;
        this.episodecode = episodecode;
        this.patfirstname = patfirstname;
        this.patmiddlename = patmiddlename;
        this.patlastname = patlastname;
        this.patguardianname = patguardianname;
        this.patgendercode = patgendercode;
        this.emailid = emailid;
        this.mobileno = mobileno;
        this.appointmentqueueno = appointmentqueueno;
        this.appointmenttime = appointmenttime;
        this.appointmentstatus = appointmentstatus;
        this.statusremarks = statusremarks;
        this.slottype = slottype;
        this.remarks = remarks;
        this.appointmenttypeid = appointmenttypeid;
        this.modulespecificcode = modulespecificcode;
        this.appointmentmode = appointmentmode;
        this.modulespecifickeyname = modulespecifickeyname;
        this.patage = patage;
        this.patspousename = patspousename;
        this.appointmentdate = appointmentdate;
        this.appointmentforid = appointmentforid;
        this.appointmentforname = appointmentforname;
        this.actulaparaid1 = actulaparaid1;
        this.actulaparaid2 = actulaparaid2;
        this.actulaparaid3 = actulaparaid3;
        this.actulaparaid4 = actulaparaid4;
        this.actulaparaid5 = actulaparaid5;
        this.actulaparaid6 = actulaparaid6;
        this.actulaparaid7 = actulaparaid7;
        this.actulaparaname1 = actulaparaname1;
        this.actulaparaname2 = actulaparaname2;
        this.actulaparaname3 = actulaparaname3;
        this.actulaparaname4 = actulaparaname4;
        this.actulaparaname5 = actulaparaname5;
        this.actulaparaname6 = actulaparaname6;
        this.actulaparaname7 = actulaparaname7;
        this.isFeesPaid=isFeesPaid;
        this.hospCode=hospCode;
        this.hospName=hospName;
        this.actualParaRefId=actualParaRefId;
    }

    public String getIs_previous_appointment() {
        return is_previous_appointment;
    }

    public void setIs_previous_appointment(String is_previous_appointment) {
        this.is_previous_appointment = is_previous_appointment;
    }

    public String getActualParaRefId() {
        return actualParaRefId;
    }

    public void setActualParaRefId(String actualParaRefId) {
        this.actualParaRefId = actualParaRefId;
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

    public String getIsFeesPaid() {
        return isFeesPaid;
    }

    public void setIsFeesPaid(String isFeesPaid) {
        this.isFeesPaid = isFeesPaid;
    }

    public String getAppointmentno() {
        return appointmentno;
    }

    public void setAppointmentno(String appointmentno) {
        this.appointmentno = appointmentno;
    }

    public String getPatcrno() {
        return patcrno;
    }

    public void setPatcrno(String patcrno) {
        this.patcrno = patcrno;
    }

    public String getEpisodecode() {
        return episodecode;
    }

    public void setEpisodecode(String episodecode) {
        this.episodecode = episodecode;
    }

    public String getPatfirstname() {
        return patfirstname;
    }

    public void setPatfirstname(String patfirstname) {
        this.patfirstname = patfirstname;
    }

    public String getPatmiddlename() {
        return patmiddlename;
    }

    public void setPatmiddlename(String patmiddlename) {
        this.patmiddlename = patmiddlename;
    }

    public String getPatlastname() {
        return patlastname;
    }

    public void setPatlastname(String patlastname) {
        this.patlastname = patlastname;
    }

    public String getPatguardianname() {
        return patguardianname;
    }

    public void setPatguardianname(String patguardianname) {
        this.patguardianname = patguardianname;
    }

    public String getPatgendercode() {
        return patgendercode;
    }

    public void setPatgendercode(String patgendercode) {
        this.patgendercode = patgendercode;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAppointmentqueueno() {
        return appointmentqueueno;
    }

    public void setAppointmentqueueno(String appointmentqueueno) {
        this.appointmentqueueno = appointmentqueueno;
    }

    public String getAppointmenttime() {
        return appointmenttime;
    }

    public void setAppointmenttime(String appointmenttime) {
        this.appointmenttime = appointmenttime;
    }

    public String getAppointmentstatus() {
        return appointmentstatus;
    }

    public void setAppointmentstatus(String appointmentstatus) {
        this.appointmentstatus = appointmentstatus;
    }

    public String getStatusremarks() {
        return statusremarks;
    }

    public void setStatusremarks(String statusremarks) {
        this.statusremarks = statusremarks;
    }

    public String getSlottype() {
        return slottype;
    }

    public void setSlottype(String slottype) {
        this.slottype = slottype;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppointmenttypeid() {
        return appointmenttypeid;
    }

    public void setAppointmenttypeid(String appointmenttypeid) {
        this.appointmenttypeid = appointmenttypeid;
    }

    public String getModulespecificcode() {
        return modulespecificcode;
    }

    public void setModulespecificcode(String modulespecificcode) {
        this.modulespecificcode = modulespecificcode;
    }

    public String getAppointmentmode() {
        return appointmentmode;
    }

    public void setAppointmentmode(String appointmentmode) {
        this.appointmentmode = appointmentmode;
    }

    public String getModulespecifickeyname() {
        return modulespecifickeyname;
    }

    public void setModulespecifickeyname(String modulespecifickeyname) {
        this.modulespecifickeyname = modulespecifickeyname;
    }

    public String getPatage() {
        return patage;
    }

    public void setPatage(String patage) {
        this.patage = patage;
    }

    public String getPatspousename() {
        return patspousename;
    }

    public void setPatspousename(String patspousename) {
        this.patspousename = patspousename;
    }

    public String getAppointmentdate() {
        return appointmentdate;
    }

    public void setAppointmentdate(String appointmentdate) {
        this.appointmentdate = appointmentdate;
    }

    public String getAppointmentforid() {
        return appointmentforid;
    }

    public void setAppointmentforid(String appointmentforid) {
        this.appointmentforid = appointmentforid;
    }

    public String getAppointmentforname() {
        return appointmentforname;
    }

    public void setAppointmentforname(String appointmentforname) {
        this.appointmentforname = appointmentforname;
    }

    public String getActulaparaid1() {
        return actulaparaid1;
    }

    public void setActulaparaid1(String actulaparaid1) {
        this.actulaparaid1 = actulaparaid1;
    }

    public String getActulaparaid2() {
        return actulaparaid2;
    }

    public void setActulaparaid2(String actulaparaid2) {
        this.actulaparaid2 = actulaparaid2;
    }

    public String getActulaparaid3() {
        return actulaparaid3;
    }

    public void setActulaparaid3(String actulaparaid3) {
        this.actulaparaid3 = actulaparaid3;
    }

    public String getActulaparaid4() {
        return actulaparaid4;
    }

    public void setActulaparaid4(String actulaparaid4) {
        this.actulaparaid4 = actulaparaid4;
    }

    public String getActulaparaid5() {
        return actulaparaid5;
    }

    public void setActulaparaid5(String actulaparaid5) {
        this.actulaparaid5 = actulaparaid5;
    }

    public String getActulaparaid6() {
        return actulaparaid6;
    }

    public void setActulaparaid6(String actulaparaid6) {
        this.actulaparaid6 = actulaparaid6;
    }

    public String getActulaparaid7() {
        return actulaparaid7;
    }

    public void setActulaparaid7(String actulaparaid7) {
        this.actulaparaid7 = actulaparaid7;
    }

    public String getActulaparaname1() {
        return actulaparaname1;
    }

    public void setActulaparaname1(String actulaparaname1) {
        this.actulaparaname1 = actulaparaname1;
    }

    public String getActulaparaname2() {
        return actulaparaname2;
    }

    public void setActulaparaname2(String actulaparaname2) {
        this.actulaparaname2 = actulaparaname2;
    }

    public String getActulaparaname3() {
        return actulaparaname3;
    }

    public void setActulaparaname3(String actulaparaname3) {
        this.actulaparaname3 = actulaparaname3;
    }

    public String getActulaparaname4() {
        return actulaparaname4;
    }

    public void setActulaparaname4(String actulaparaname4) {
        this.actulaparaname4 = actulaparaname4;
    }

    public String getActulaparaname5() {
        return actulaparaname5;
    }

    public void setActulaparaname5(String actulaparaname5) {
        this.actulaparaname5 = actulaparaname5;
    }

    public String getActulaparaname6() {
        return actulaparaname6;
    }

    public void setActulaparaname6(String actulaparaname6) {
        this.actulaparaname6 = actulaparaname6;
    }

    public String getActulaparaname7() {
        return actulaparaname7;
    }

    public void setActulaparaname7(String actulaparaname7) {
        this.actulaparaname7 = actulaparaname7;
    }
}
