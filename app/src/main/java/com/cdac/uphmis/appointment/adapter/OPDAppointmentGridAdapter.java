package com.cdac.uphmis.appointment.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.AppointmentSummaryActivity;
import com.cdac.uphmis.appointment.OPDAppointmentSlotSelectionActivity;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.appointment.model.ShiftDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class OPDAppointmentGridAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<ShiftDetails> shiftDetailsArrayList;
    OPDAppointmentDetails screeningDetails;
    int row_index = -1;
    ManagingSharedData msd;
    public OPDAppointmentGridAdapter(Context context, ArrayList<ShiftDetails> shiftDetailsArrayList, OPDAppointmentDetails screeningDetails) {
        this.context = context;
        this.shiftDetailsArrayList = shiftDetailsArrayList;
        this.screeningDetails = screeningDetails;
        this.msd = new ManagingSharedData(context);
    }

    @Override
    public int getCount() {
        return shiftDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return shiftDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ShiftDetails s = (ShiftDetails) this.getItem(position);

        Holder holder = new Holder();
        View rowView;

        rowView = layoutInflater.inflate(R.layout.single_item, null);
        holder.btnTime = (TextView) rowView.findViewById(R.id.btn_time);
        holder.btnTime.setText(shiftDetailsArrayList.get(position).getSlotst());

        if (row_index == position) {

            holder.btnTime.setBackgroundResource(R.drawable.selected_date_bacground);

        } else {
            holder.btnTime.setBackgroundResource(R.drawable.slot_background);

        }
//        if (row_index == -1) {
//            OPDAppointmentSlotSelectionActivity.btnConfirm.setVisibility(View.GONE);
//            OPDAppointmentSlotSelectionActivity.tvAppointmentConfirm.setVisibility(View.GONE);
//        } else {
//            OPDAppointmentSlotSelectionActivity.btnConfirm.setVisibility(View.VISIBLE);
//            OPDAppointmentSlotSelectionActivity.tvAppointmentConfirm.setVisibility(View.VISIBLE);
//
//        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                Log.i("gridadapter", "onClick: " + s.getSlotst());

                String displayDate = "";
                try {
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date1 = format1.parse(s.getDate());
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = format2.format(date1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String  hospName=context.getResources().getString(R.string.hosp_name);
                String confirmMessage = "Appointment request for <b>" + screeningDetails.getPatFirstName() + " " + screeningDetails.getPatMiddleName() + " " + screeningDetails.getPatLastName() + " (" + screeningDetails.getPatGenderCode() + "/" + screeningDetails.getPatAge() + ")</b> on <b>" + displayDate + " " + s.getSlotst() + "</b> in <b>" + screeningDetails.getDeptUnitName() + "</b>  at <b>" + hospName + "</b>.Please click to confirm.";
//                OPDAppointmentSlotSelectionActivity.tvAppointmentConfirm.setText(Html.fromHtml(confirmMessage));

                screeningDetails.setShiftId(shiftDetailsArrayList.get(position).getShift());
                screeningDetails.setShiftST(shiftDetailsArrayList.get(position).getShiftst());
                screeningDetails.setShiftET(shiftDetailsArrayList.get(position).getShiftet());
                screeningDetails.setSlotST(shiftDetailsArrayList.get(position).getSlotst());
                screeningDetails.setSlotET(shiftDetailsArrayList.get(position).getSlotet());
                screeningDetails.setAppointmentDate(shiftDetailsArrayList.get(position).getDate());

             /*   OPDAppointmentSlotSelectionActivity.btnConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (checkOpdHours("10:01", "23:59", shiftDetailsArrayList.get(0).getDate())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to proceed?");
                        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    save(screeningDetails.getPatFirstName(), screeningDetails.getPatMiddleName(), screeningDetails.getPatLastName(), screeningDetails.getPatAge(), screeningDetails.getPatAgeUnit(), screeningDetails.getPatGuardianName(), screeningDetails.getPatSpouseName(), screeningDetails.getMobileNo(), screeningDetails.getEmailId(), screeningDetails.getRemarks(), screeningDetails.getPatGenderCode(), screeningDetails.getDeptUnitCode(), screeningDetails.getDeptUnitName(), screeningDetails.getDeptLocation(), screeningDetails.getShiftId(), screeningDetails.getShiftST(), screeningDetails.getShiftET(), screeningDetails.getSlotST(), screeningDetails.getSlotET(), screeningDetails.getActualParameterReferenceId(), screeningDetails.getAppointmentDate(), screeningDetails.getPatCrNo());

                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Information")
                                    .setMessage("Appointments can only be booked before 10:00 am on the same day. Please book appointment for another date.")
                                    .setPositiveButton("OK", null)
                                    .show();

                        }
                    }
                });*/
            }
        });

        return rowView;
    }


    public class Holder {
        TextView btnTime;

    }

    public void save(final String firstName, final String middleName, final String lastName, final String age, final String patAgeUnit, final String fatherName, final String spouseName, final String mobileNo, final String email, final String remarks, final String genderId, final String departmentUnitCode, final String departUnitName, final String departLocation, final String shiftId, final String shiftSt, final String shiftet, final String slotSt, final String slotEt, final String actualparameterreferenceid, final String appointmentDate, final String crno) {


        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.makeAppointment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("register user", "onResponse: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("SUCCESS").equals("true")) {

                            Intent intent = new Intent(context, AppointmentSummaryActivity.class);
                            intent.putExtra("appointmentDetails", screeningDetails);
                            intent.putExtra("apptno", jsonObj.getString("APPTNO"));

                            context.startActivity(intent);
                            ((Activity) context).finish();

                        } else {

                            Toast.makeText(context,R.string.failed_appointment, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error.getMessage());

                AppUtilityFunctions.handleExceptions(error, context);
            }
        }) {
            @Override
            //  protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                HashMap data = new HashMap<>();


                data.put("appointmentForId", "1");
                data.put("patFirstName", firstName);
                data.put("patMiddleName", middleName);
                data.put("patLastName", lastName);
                data.put("patGuardianName", fatherName);
                data.put("patSpouseName", spouseName);
                data.put("patDOB", "");
                data.put("appointmentDate", appointmentDate);
                data.put("emailId", email);
                data.put("mobileNo", mobileNo);
                data.put("appointmentTime", slotSt);
                data.put("appointmentStatus", "1");
                data.put("slotType", "3");
                data.put("remarks", remarks);
                data.put("appointmentTypeId", "1");
                data.put("appointmentMode", "13");
                data.put("patAgeUnit", patAgeUnit);
                data.put("patAge", age);
                data.put("patGenderCode", genderId);
                data.put("allActualParameterId", "");
                data.put("shiftId", shiftId);
                data.put("slotST", slotSt);
                data.put("slotET", slotEt);
                data.put("actualParameterReferenceId", actualparameterreferenceid);
                data.put("shiftST", shiftSt);
                data.put("shiftET", shiftet);
                data.put("hcode", ServiceUrl.hospId);
                data.put("deptUnitCode", departmentUnitCode);
                data.put("deptUnitName", departUnitName);
                data.put("deptLocation", departLocation);
                data.put("patCrNo", crno);


                Log.i("hashmap", "getParams: " + data);


                return data;

            }

        };

         MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private boolean checkOpdHours(String openTime, String CloseTime, String slotDate) {
        try {
            Log.i("slotDate", "checkOpdHours: " + slotDate);
            Date time1 = new SimpleDateFormat("HH:mm").parse(openTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm").parse(CloseTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            Date currentdate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            System.out.println(format.format(currentdate));

            Date d = new SimpleDateFormat("HH:mm").parse(format.format(currentdate));
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();



            String dateInString =new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            Log.i("dateInString", "checkOpdHours: "+dateInString);
            if (dateInString.equalsIgnoreCase(slotDate))
            {

            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                Log.i("showAlert", "checkOpdHours: ");
                return false;

            } else {
                Log.i("dontShowAlert", "checkOpdHours: " + x);

                return true;
            }


            }else
            {
                Log.i("dontShowAlert", "checkOpdHours: " + x);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}