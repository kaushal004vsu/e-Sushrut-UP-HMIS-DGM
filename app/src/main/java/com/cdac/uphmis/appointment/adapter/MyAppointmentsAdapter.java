package com.cdac.uphmis.appointment.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.PatientDrawerHomeActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.AppointmentRescheduleActivity;
import com.cdac.uphmis.appointment.model.MyAppointmentDetails;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAppointmentsAdapter extends BaseAdapter {
    Context c;
    ArrayList<MyAppointmentDetails> myAppointmentDetailsArrayList;
    RequestQueue requestQueue;

    public MyAppointmentsAdapter(Context c, ArrayList<MyAppointmentDetails> myAppointmentDetailsArrayList) {
        this.c = c;
        this.myAppointmentDetailsArrayList = myAppointmentDetailsArrayList;
        requestQueue = Volley.newRequestQueue(c);
    }

    @Override
    public int getCount() {
        return myAppointmentDetailsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return myAppointmentDetailsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.my_appointments_model, viewGroup, false);
        }

        final MyAppointmentDetails s = (MyAppointmentDetails) this.getItem(i);
        TextView appointmentNumber = view.findViewById(R.id.tv_appointment_number);
        final TextView appointmentDateTime = view.findViewById(R.id.tv_appointment_date_time);
        TextView appointmentFor = view.findViewById(R.id.tv_appointment_for);
        TextView status = view.findViewById(R.id.tv_status);
        TextView remarks = view.findViewById(R.id.tv_remarks);
        TextView tvHospitalName = view.findViewById(R.id.tv_hospital_name);

        tvHospitalName.setText(s.getHospName());

        LinearLayout llButtons = view.findViewById(R.id.ll_cancel_reschedule);
        llButtons.setVisibility(View.VISIBLE);

        TextView btnCancelAppointment = view.findViewById(R.id.btn_cancel_appointment);
        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAppointmentDialog(s);

            }
        });

        TextView btnRescheduleAppointment = view.findViewById(R.id.btn_reschedule_appointment);
        btnRescheduleAppointment.setOnClickListener(view1 -> {
//                RescheduleAppointmentFragment rescheduleAppointmentFragment=new RescheduleAppointmentFragment();
//                Bundle b = new Bundle();
//                b.putSerializable("UserValidateObject",s);
//                rescheduleAppointmentFragment.setArguments(b);
//                FragmentTransaction transaction=((Activity) c).getFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, rescheduleAppointmentFragment);
//                transaction.commit();
            Intent intent = new Intent(c, AppointmentRescheduleActivity.class);
            intent.putExtra("myAppointmentDetails", s);
            c.startActivity(intent);
        });



        appointmentNumber.setText("Appointment Number-Queue No : " + s.getAppointmentno());
        appointmentDateTime.setText("Appointment On : " + s.getAppointmentdate());
        appointmentFor.setText("Dept/Unit: " + s.getActulaparaname1());
//        status.setText("Status : " + s.getAppointmentstatus());
//        remarks.setText("Remarks : " + s.getRemarks());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status.setTextColor(c.getColor(android.R.color.holo_blue_dark));
        }
        if (s.getAppointmentstatus().equalsIgnoreCase("0")) {
            status.setText("Status : Cancelled");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                status.setTextColor(c.getColor(android.R.color.holo_red_dark));
            }
            llButtons.setVisibility(View.GONE);
        }
        if (s.getAppointmentstatus().equalsIgnoreCase("1")) {
            status.setText("Status : Booked");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                status.setTextColor(c.getColor(android.R.color.holo_green_dark));
            }
            llButtons.setVisibility(View.VISIBLE);
        }
        if (s.getAppointmentstatus().equalsIgnoreCase("2")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                status.setTextColor(c.getColor(android.R.color.holo_blue_dark));
            }
            status.setText("Status : Rescheduled");
            llButtons.setVisibility(View.VISIBLE);
        }
        if (s.getAppointmentstatus().equalsIgnoreCase("3")) {
            status.setText("Status : Confirmed");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                status.setTextColor(c.getColor(android.R.color.holo_green_dark));
            }
            llButtons.setVisibility(View.GONE);
        }
        if (s.getIs_previous_appointment().equals("1")){
            llButtons.setVisibility(View.GONE);
        }

        return view;
    }

    public void cancelAppointment(final String appointmentno, final String patcrno, final String episodecode, final String patfirstname, final String patmiddlename, final String patlastname, final String patguardianname, final String patgendercode, final String emailid, final String mobileno, final String appointmentqueueno, final String appointmenttime, final String appointmentstatus, final String statusremarks, final String slottype, final String remarks, final String appointmenttypeid, final String modulespecificcode, final String appointmentmode, final String modulespecifickeyname, final String patage, final String patspousename, final String appointmentdate, final String appointmentforid, final String appointmentforname, final String actulaparaid1, final String actulaparaid2, final String actulaparaid3, final String actulaparaid4, final String actulaparaid5, final String actulaparaid6, final String actulaparaid7, final String actulaparaname1, final String actulaparaname2, final String actulaparaname3, final String actulaparaname4, final String actulaparaname5, final String actulaparaname6, final String actulaparaname7,final String hospCode) {
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.cancelAppointmentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("SUCCESS").equals("true")) {
                            new LovelyStandardDialog(c, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                                    .setTopColorRes(R.color.colorPrimary)
                                    .setButtonsColorRes(R.color.colorPrimary)
                                    .setIcon(R.drawable.successicon)
                                    .setTitle("Appointment Cancelled Successfully")
                                    .setMessage("Your appointment number " + jsonObj.getString("APPTNO") + " is cancelled successfully.")
                                    .setPositiveButton("Ok", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            NewPatientHomeFragment newPatientHomeFragment = new NewPatientHomeFragment();
//                                            FragmentTransaction transaction = ((Activity) c).getFragmentManager().beginTransaction();
//                                            transaction.replace(R.id.container, newPatientHomeFragment);
//                                            transaction.commit();
                                            Intent intent = new Intent(c, PatientDrawerHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            c.startActivity(intent);
                                        }
                                    }).show();
                        } else {
//                            progressView.setVisibility(View.GONE);
                            Toast.makeText(c, "Unable to cancel appointment.Please try again later.", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(getView(), "Unable to make appointment.Please try again later.", Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressView.setVisibility(View.GONE);
                Log.i("error hai ", "onErrorResponse: " + error.getMessage());
                if (c != null) {
                    Toast.makeText(c, "Unable to connect server please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            //  protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                Map<String, String> data = new HashMap<>();
                data.put("appointmentNo", appointmentno);
                data.put("patCrNo", patcrno);
                data.put("episodeCode", episodecode);
                data.put("patFirstName", patfirstname);
                data.put("patMiddleName", patmiddlename);
                data.put("patLastName", patlastname);
                data.put("patGuardianName", patguardianname);
                data.put("patGenderCode", patgendercode);
                data.put("emailId", emailid);
                data.put("mobileNo", mobileno);
                data.put("appointmentQueueNo", appointmentqueueno);
                data.put("appointmentTime", appointmenttime);
                data.put("appointmentStatus", appointmentstatus);
                data.put("statusRemarks", statusremarks);
                data.put("slotType", slottype);
                data.put("remarks", remarks);
                data.put("appointmentTypeId", appointmenttypeid);
                data.put("moduleSpecificCode", modulespecificcode);
                data.put("appointmentMode", appointmentmode);
                data.put("moduleSpecificKeyName", modulespecifickeyname);
                data.put("patAge", patage);
                data.put("patSpouseName", patspousename);
                data.put("appointmentDate", appointmentdate);
                data.put("appointmentForId", appointmentforid);
                data.put("appointmentForName", appointmentforname);
                data.put("actualParaId1", actulaparaid1);
                data.put("actualParaId2", actulaparaid2);
                data.put("actualParaId3", actulaparaid3);
                data.put("actualParaId4", actulaparaid4);
                data.put("actualParaId5", actulaparaid5);
                data.put("actualParaId6", actulaparaid6);
                data.put("actualParaId7", actulaparaid7);
                data.put("actualParaName1", actulaparaname1);
                data.put("actualParaName2", actulaparaname2);
                data.put("actualParaName3", actulaparaname3);
                data.put("actualParaName4", actulaparaname4);
                data.put("actualParaName5", actulaparaname5);
                data.put("actualParaName6", actulaparaname6);
                data.put("actualParaName7", actulaparaname7);
                data.put("hcode",hospCode );
                Log.i("hashmap", "getParams: " + data);
                return data;
            }
        };

         MySingleton.getInstance(c).addToRequestQueue(request);
    }
    private void cancelAppointmentDialog(MyAppointmentDetails s) {
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cancel_appointment_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        //final Dialog dialog = new Dialog(c);
        //dialog.setContentView(R.layout.cancel_appointment_dialog);
        TextView btnBack = dialog.findViewById(R.id.btn_dialog_back);
        TextView btnProceed = dialog.findViewById(R.id.btn_dialog_proceed);
        final EditText edtRemarks = dialog.findViewById(R.id.edt_remarks);
        btnBack.setOnClickListener(view -> dialog.dismiss());
        btnProceed.setOnClickListener(view -> {
            if (edtRemarks.getText().toString().length() == 0) {
                Toast.makeText(c, "Please enter Remarks.", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                cancelAppointment(s.getAppointmentno(), s.getPatcrno(), s.getEpisodecode(), s.getPatfirstname(), s.getPatmiddlename(), s.getPatlastname(), s.getPatguardianname(), s.getPatgendercode(), s.getEmailid(), s.getMobileno(), s.getAppointmentqueueno(), s.getAppointmenttime(), s.getAppointmentstatus(), s.getStatusremarks(), s.getSlottype(), edtRemarks.getText().toString(), s.getAppointmenttypeid(), s.getModulespecificcode(), s.getAppointmentmode(), s.getModulespecifickeyname(), s.getPatage(), s.getPatspousename(), s.getAppointmentdate(), s.getAppointmentforid(), s.getAppointmentforname(), s.getActulaparaid1(), s.getActulaparaid2(), s.getActulaparaid3(), s.getActulaparaid4(), s.getActulaparaid5(), s.getActulaparaid6(), s.getActulaparaid7(), s.getActulaparaname1(), s.getActulaparaname2(), s.getActulaparaname3(), s.getActulaparaname4(), s.getActulaparaname5(), s.getActulaparaname6(), s.getActulaparaname7(),s.getHospCode());
            }
        });
        dialog.show();
    }
}
