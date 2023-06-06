package com.cdac.uphmis.labBasedAppointment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.OPDAppointmentSlotSelectionActivity;
import com.cdac.uphmis.appointment.model.DepartmentDetails;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.ManagingSharedData;

import java.util.ArrayList;

public class LabBasedAppointmentAdapter extends BaseAdapter {
    Context c;
    ArrayList<LabBasedAppointmentDetails> departmentArrayList;

    public LabBasedAppointmentAdapter(Context c, ArrayList<LabBasedAppointmentDetails> departmentArrayList) {
        this.c = c;
        this.departmentArrayList = departmentArrayList;


    }

    @Override
    public int getCount() {
        return departmentArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return departmentArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.row_lab_based, viewGroup, false);
        }
        final LabBasedAppointmentDetails s = (LabBasedAppointmentDetails) this.getItem(i);
        TextView tv_test_name = view.findViewById(R.id.tv_test_name);
        TextView tv_lab_name = view.findViewById(R.id.tv_lab_name);
        TextView tv_req_date = view.findViewById(R.id.tv_req_date);

        tv_test_name.setText(s.getTestname());
        tv_lab_name.setText(s.getLabname());
        tv_req_date.setText(s.getReqdate());

        view.setOnClickListener(view1 -> {
            /*For registered patients*/
            ManagingSharedData msd = new ManagingSharedData(c);
            PatientDetails patientDetails=msd.getPatientDetails();
            OPDAppointmentDetails appointmentDetails = new OPDAppointmentDetails();
            // if (appointmentDetails != null) {
            // String testname, labname, reqdate, reqdno, testcode, labcode, deptname, unitname, deptcode,
            //      unitcode, genderCode, crno, patname, episodecode, age, actualpararefid, preffereddate,hospCode,hospName;
            appointmentDetails.setAppointmentForId("2");
            appointmentDetails.setTestname(departmentArrayList.get(i).getTestname().trim());
            appointmentDetails.setLabname(departmentArrayList.get(i).getLabname().trim());
            appointmentDetails.setReqdate(departmentArrayList.get(i).getReqdate().trim());
            appointmentDetails.setReqdno(departmentArrayList.get(i).getReqdno().trim());
            appointmentDetails.setTestcode(departmentArrayList.get(i).getTestcode().trim());
            appointmentDetails.setDeptUnitName(departmentArrayList.get(i).getLabname().trim());
            appointmentDetails.setDeptUnitCode(departmentArrayList.get(i).getUnitcode().trim());
            appointmentDetails.setPatGenderCode(departmentArrayList.get(i).getGenderCode().trim());
            appointmentDetails.setPatCrNo(departmentArrayList.get(i).getCrno().trim());
            appointmentDetails.setPatFirstName(departmentArrayList.get(i).getPatname().trim());
            appointmentDetails.setEpisodecode(departmentArrayList.get(i).getEpisodecode().trim());
            //appointmentDetails.setPatAge(departmentArrayList.get(i).getAge().trim());
            appointmentDetails.setActualParameterReferenceId(departmentArrayList.get(i).getActualpararefid().trim());
            appointmentDetails.setPreffereddate(departmentArrayList.get(i).getPreffereddate().trim());
            appointmentDetails.setHcode(departmentArrayList.get(i).getHospCode().trim());
            appointmentDetails.setHospName(departmentArrayList.get(i).getHospName().trim());

            appointmentDetails.setPatGuardianName(patientDetails.getFathername());
            appointmentDetails.setPatSpouseName(patientDetails.getSPOUSE_NAME());
            String name = patientDetails.getFirstname();

            if (name.split("\\w+").length > 1) {
                appointmentDetails.setPatLastName(name.substring(name.lastIndexOf(" ") + 1));
                appointmentDetails.setPatFirstName(name.substring(0, name.lastIndexOf(' ')));
            } else {
                appointmentDetails.setPatFirstName(name);
                appointmentDetails.setPatLastName("");
            }
            appointmentDetails.setPatMiddleName("");
            if(patientDetails.getAge()!=null){
                String[] arAge = patientDetails.getAge().trim().split(" ");
                appointmentDetails.setPatAge(arAge[0]);
            }
            appointmentDetails.setAppointmentForId("2");
            appointmentDetails.setPatAgeUnit("Yr");
            appointmentDetails.setMobileNo(patientDetails.getMobileNo());
            appointmentDetails.setEmailId(patientDetails.getEmailId());
            appointmentDetails.setRemarks("");
              /*  if(departmentArrayList.get(i).getAge()!=null){
                    String[] arAge = departmentArrayList.get(i).getAge().trim().split("");
                    appointmentDetails.setAge(arAge[0]);
                }*/

            Intent intent = new Intent(c, OPDAppointmentSlotSelectionActivity.class);
            intent.putExtra("appointmentDetails", appointmentDetails);
            c.startActivity(intent);
            //  }
        });
        return view;
    }
}
