package com.cdac.uphmis.appointment.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.OPDAppointmentSlotSelectionActivity;
import com.cdac.uphmis.appointment.model.DepartmentDetails;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.model.PatientDetails;

import java.util.ArrayList;

public class AppointmentDepartmentAdapter extends BaseAdapter {
    Context c;
    ArrayList<DepartmentDetails> departmentArrayList;
    RequestQueue requestQueue;
    PatientDetails patientDetails;
    String hospitalCode;
    public AppointmentDepartmentAdapter(Context c, ArrayList<DepartmentDetails> departmentArrayList, PatientDetails patientDetails,String hospitalCode) {
        this.c = c;
        this.departmentArrayList = departmentArrayList;
        requestQueue = Volley.newRequestQueue(c);
        this.patientDetails = patientDetails;
        this.hospitalCode=hospitalCode;
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
            view = LayoutInflater.from(c).inflate(R.layout.appointment_department_model, viewGroup, false);
        }
        final DepartmentDetails s = (DepartmentDetails) this.getItem(i);
        TextView tvDeptName = view.findViewById(R.id.tv_dept_name);
        TextView tvInchargeName = view.findViewById(R.id.tv_incharge_name);
       // ImageView imgChrge = view.findViewById(R.id.imp_chrge);

        tvDeptName.setText(s.getDeptname());
        if (!s.getInchargeName().trim().isEmpty())
        tvInchargeName.setText("Incharge: "+s.getInchargeName());


        ColorGenerator generator = ColorGenerator.MATERIAL;
        // generate random color
       // int color = generator.getColor(i);
       // TextDrawable drawable = TextDrawable.builder() .roundRect(10).build("₹ "+s.getCharge(), color);
        //   .buildRound(currentItem.getTOKEN_NO(), color);

       // imgChrge.setImageDrawable(drawable);

        view.setOnClickListener(view1 -> {
            /*For registered patients*/

            OPDAppointmentDetails appointmentDetails = new OPDAppointmentDetails();
            if (appointmentDetails != null) {
                String name = patientDetails.getFirstname();

                if (name.split("\\w+").length > 1) {
                   // appointmentDetails.setPatLastName(name.substring(name.lastIndexOf(" ") + 1));
                    appointmentDetails.setPatFirstName(name.substring(0, name.lastIndexOf(' ')));
                } else {
                    appointmentDetails.setPatFirstName(name);
                   // appointmentDetails.setPatLastName("");
                }
                appointmentDetails.setPatMiddleName(patientDetails.getMiddleName());
                appointmentDetails.setPatLastName(patientDetails.getLastName());




                appointmentDetails.setAppointmentForId("1");

                appointmentDetails.setPatMiddleName("");
                if(patientDetails.getAge()!=null){
                    String[] arAge = patientDetails.getAge().trim().split(" ");
                    appointmentDetails.setPatAge(arAge[0]);
                }
                appointmentDetails.setPatGuardianName(patientDetails.getFathername());
                appointmentDetails.setPatSpouseName(patientDetails.getSPOUSE_NAME());
                appointmentDetails.setMobileNo(patientDetails.getMobileNo());
                appointmentDetails.setEmailId(patientDetails.getEmailId());
                appointmentDetails.setRemarks("");
                appointmentDetails.setPatCrNo(patientDetails.getCrno());
                appointmentDetails.setPatGenderCode(patientDetails.getGender());
                appointmentDetails.setPatAgeUnit("Yr");
                //department parameters
                appointmentDetails.setDeptUnitCode(s.getUnitcode());
                appointmentDetails.setDeptLocation(s.getLoCode());
                appointmentDetails.setDeptUnitName(s.getDeptname());
                appointmentDetails.setHcode(hospitalCode);
                appointmentDetails.setActualParameterReferenceId(s.getActualparameterreferenceid());
                appointmentDetails.setTariffId(s.getTariffId());
                appointmentDetails.setCharge(s.getCharge());
                Intent intent = new Intent(c, OPDAppointmentSlotSelectionActivity.class);
                intent.putExtra("appointmentDetails", appointmentDetails);
                c.startActivity(intent);
            }
        });
        return view;
    }
}
