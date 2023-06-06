package com.cdac.uphmis.covid19.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.AppointmentSuccefullActivity;
import com.cdac.uphmis.covid19.DocumentUploadActivity;
import com.cdac.uphmis.covid19.SlotSelectionActivity;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.covid19.model.ShiftDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;

public class GridAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    ArrayList<ShiftDetails> shiftDetailsArrayList;
    ScreeningDetails screeningDetails;


    int row_index = -1;

    public GridAdapter(Context context, ArrayList<ShiftDetails> shiftDetailsArrayList, ScreeningDetails screeningDetails) {
        this.context = context;
        this.shiftDetailsArrayList = shiftDetailsArrayList;
        this.screeningDetails = screeningDetails;
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
       /* if (row_index == -1) {
            SlotSelectionActivity.btnConfirm.setVisibility(View.GONE);
            SlotSelectionActivity.tvAppointmentConfirm.setVisibility(View.GONE);
        } else {
            SlotSelectionActivity.btnConfirm.setVisibility(View.VISIBLE);
            SlotSelectionActivity.tvAppointmentConfirm.setVisibility(View.VISIBLE);

        }
*/

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
                String confirmMessage="Appointment request for <b>" + screeningDetails.getPatName()+" ("+screeningDetails.getPatGender()+"/"+screeningDetails.getPatAge()+")</b> on <b>"  + displayDate  +" "+s.getSlotst() +"</b> in <b>" + screeningDetails.getDeptUnitName() + "</b>  at <b>"+screeningDetails.getHospName()+"</b>.Please click to confirm.";
                /*SlotSelectionActivity.tvAppointmentConfirm.setText(Html.fromHtml(confirmMessage));
                SlotSelectionActivity.btnConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        bookAppointment(screeningDetails.getRequestId(), screeningDetails.getCrno(), screeningDetails.getScrResponse(), screeningDetails.getConsName(), screeningDetails.getDeptUnitCode(), screeningDetails.getDeptUnitName(), screeningDetails.getRequestStatus(), screeningDetails.getPatMobileNo(), screeningDetails.getConsMobileNo(), screeningDetails.getPatDocs(), screeningDetails.getDocMessage(), screeningDetails.getConstId(), screeningDetails.getPatName(), screeningDetails.getPatAge(), screeningDetails.getPatGender(), screeningDetails.getEmail(), screeningDetails.getRemarks(), screeningDetails.getPatWeight(), screeningDetails.getPatHeight(), screeningDetails.getMedications(), screeningDetails.getPastdiagnosis(), screeningDetails.getPastAllergies(), screeningDetails.getUserId(), screeningDetails.getStateCode(), screeningDetails.getDistrictCode(), screeningDetails.getApptDeptUnit(), screeningDetails.getGuardianName(), screeningDetails.getPatientToken(), s.getDate(), s.getSlotst(), s.getSlotet(), s.getShift());
                    }
                });*/
            }
        });

        return rowView;
    }


    public class Holder {
        TextView btnTime;

    }

    private void bookAppointment(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String ptientToken, String apptDate, String slotst, String slotet, String shiftId) {
        try {
        //    NukeSSLCerts.nuke(context);
//            SlotSelectionActivity.btnConfirm.setEnabled(false);
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String url = ServiceUrl.bookAppointment;
            Log.i("url", "makeRequest: " + url);

            final String requestBody = requestBodyJson(requestId, crno, scrResponse, consName, deptUnitCode, deptUnitName, requestStatus, patMobileNo, consMobileNo, patDocs, docMessage, constId, patName, patAge, patGender, email, remarks, patWeight, patHeight, medications, pastdiagnosis, pastAllergies, userId, stateCode, districtCode, apptDeptUnitCode, guardianName, ptientToken, apptDate, slotst, slotet, shiftId);

            Log.i("requestBody", "bookAppointment: " + requestBody);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    SlotSelectionActivity.btnConfirm.setEnabled(true);
                    Log.i("response", "onResponse: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("Status");
                        String s = jsonObject.getString("Message");
                        String crno = jsonObject.getString("CrNo");
                        if (status) {
                            String finalRequestId = s.substring(s.indexOf("no") + 3, s.indexOf("raised") - 1);

                            String appointmentDetails=apptDate+" at "+slotst;

                                    documentUploadDialog(finalRequestId, crno,appointmentDetails);
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Cannot Raise Request")
                                    .setMessage(s)
                                    .setNegativeButton("Ok", null)
                                    .show();


                        }

                    } catch (Exception ex) {
                        Log.i("jsonexception", "onResponse: " + ex);

                        Toast.makeText(context, "Sorry! unable to raise your request.try after sometime.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("bookappintment", "onResponse: " + error);
                    AppUtilityFunctions.handleExceptions(error, context);
//                    SlotSelectionActivity.btnConfirm.setEnabled(true);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cdac", "");


                    return params;
                }

            };


             MySingleton.getInstance(context).addToRequestQueue(request);
        } catch (Exception ex) {
            Log.i("jsonexception", "makeRequest: ");
//            SlotSelectionActivity.btnConfirm.setEnabled(true);
        }
    }

    private String requestBodyJson(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String patientToken, String apptDate, String slotst, String slotet, String shiftId) {
        ManagingSharedData msd = new ManagingSharedData(context);
        Map<String, String> data = new HashMap<>();
        data.put("requestID", requestId);
        data.put("CRNo", crno);
        data.put("scrResponse", scrResponse);
        data.put("consName", consName);
        data.put("deptUnitCode", deptUnitCode);
        data.put("deptUnitName", deptUnitName);
        data.put("requestStatus", requestStatus);
        data.put("consMobileNo", consMobileNo);
        data.put("patDocs", patDocs);
        data.put("docMessage", docMessage);
        data.put("cnsltntId", constId);
        data.put("rmrks", remarks);
        data.put("email", email);
        data.put("patWeight", patWeight);
        data.put("patHeight", patHeight);
        data.put("patMedication", medications);
        data.put("patPastDiagnosis", pastdiagnosis);
        data.put("patAllergies", pastAllergies);
        data.put("userId", userId);
        data.put("patientToken", patientToken);
        data.put("address", "");

        data.put("hospCode", screeningDetails.getHospCode());
        data.put("patMobileNo", patMobileNo);
        data.put("patName", patName);
        data.put("patAge", patAge);
        data.put("patGender", patGender);

        data.put("appt_dept_unit", apptDeptUnitCode);
        data.put("state_code", stateCode);
        data.put("district_code", districtCode);
        data.put("country_code", "IND");
        data.put("pat_guardian", guardianName);
        data.put("appointmentDate", apptDate);
        data.put("appointmentTime", slotst);
        data.put("start_time", slotst);
        data.put("end_time", slotet);
        data.put("shiftId", shiftId);

        JSONObject jsonObject = new JSONObject(data);

        JSONObject patDtls = new JSONObject();
        try {

            patDtls.put("pat_dtls", jsonObject);
        } catch (Exception ex) {
            Log.i("jsonException", "requestBodyJson: " + ex);
        }
        return patDtls.toString();
    }


    private void documentUploadDialog(final String finalRequestId, String crno,String appointmentDetails) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.document_upload_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        TextView tvNo = dialog.findViewById(R.id.tv_no);
        TextView tvYes = dialog.findViewById(R.id.tv_yes);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, AppointmentSuccefullActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DocumentUploadActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();
    }



}