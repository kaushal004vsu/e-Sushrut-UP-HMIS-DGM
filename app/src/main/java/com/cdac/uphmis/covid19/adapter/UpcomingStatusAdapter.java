package com.cdac.uphmis.covid19.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.chat.DoctorMessage;
import com.cdac.uphmis.chat.DoctorMessageAdapter;
import com.cdac.uphmis.covid19.model.PatientRequestDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;

public class UpcomingStatusAdapter extends BaseAdapter {

    Context c;
    ArrayList<PatientRequestDetails> healthWorkerRequestListDetailsArrayList;
    boolean isPast;


    public UpcomingStatusAdapter(Context c, ArrayList<PatientRequestDetails> healthWorkerRequestListDetailsArrayList, boolean isPast) {
        this.c = c;
        this.healthWorkerRequestListDetailsArrayList = healthWorkerRequestListDetailsArrayList;
        this.isPast = isPast;

    }

    @Override
    public int getCount() {
        return healthWorkerRequestListDetailsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return healthWorkerRequestListDetailsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.upcoming_request_list_model, viewGroup, false);
        }
        final PatientRequestDetails healthWorkerRequestListDetails = (PatientRequestDetails) this.getItem(i);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar);
        TextView tvUnitname = view.findViewById(R.id.tv_unit_name);
        TextView tvPatName = view.findViewById(R.id.tv_patient_name);
        TextView tvConsultantName = view.findViewById(R.id.tv_consultant_name);
        TextView tvRequestStatus = view.findViewById(R.id.tv_request_status);
        TextView tvRequestedDate = view.findViewById(R.id.tv_requested_date);
        TextView tvDoctorMessage = view.findViewById(R.id.tv_doc_message);
        TextView tvViewPrescription = view.findViewById(R.id.tv_view_prescription);
        TextView tvDeptName = view.findViewById(R.id.tv_dept_name);
        TextView tvJoinCall = view.findViewById(R.id.tv_join_call);
        TextView tvAppointmentDateTime = view.findViewById(R.id.tv_appointment_date);
        TextView tvRateUs = view.findViewById(R.id.tv_rate_us);
        TextView tvUnattendedMessage = view.findViewById(R.id.tv_unattended_message);
        tvJoinCall.setVisibility(View.GONE);
        tvUnattendedMessage.setVisibility(View.GONE);

        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format1.parse(healthWorkerRequestListDetails.getApptDate().substring(0, 10));
            SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
            String displayDate = format2.format(date1);
            tvAppointmentDateTime.setText(displayDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        tvUnitname.setText("Unit Name: " + healthWorkerRequestListDetails.getDeptUnitName());
        tvDeptName.setText(healthWorkerRequestListDetails.getDeptName() + ", " + healthWorkerRequestListDetails.getHospitalName());

        tvViewPrescription.setVisibility(View.GONE);
        tvRequestedDate.setText(healthWorkerRequestListDetails.getRequestID() + "/" + healthWorkerRequestListDetails.getDate());

        if (!healthWorkerRequestListDetails.getDocMessage().equalsIgnoreCase("")) {
            tvDoctorMessage.setVisibility(View.VISIBLE);
        } else {
            tvDoctorMessage.setVisibility(View.GONE);
        }


        if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("2")) {
            tvRateUs.setVisibility(View.VISIBLE);
        } else {
            tvRateUs.setVisibility(View.GONE);
        }


        tvRateUs.setOnClickListener(view1 -> showRateUsDialog(healthWorkerRequestListDetails.getRequestID(), healthWorkerRequestListDetails.getHospCode()));


        tvDoctorMessage.setOnClickListener(view12 -> {

            //view doctor's message
            sendMessage(healthWorkerRequestListDetails, "", "1");
        });


        tvPatName.setText(healthWorkerRequestListDetails.getPatName() + " (CR No: " + healthWorkerRequestListDetails.getCRNo() + ")");
        if (!healthWorkerRequestListDetails.getConsName().trim().equalsIgnoreCase("")) {
            tvConsultantName.setText("Appointment with " + healthWorkerRequestListDetails.getConsName());
            tvConsultantName.setVisibility(View.VISIBLE);
        } else {
            tvConsultantName.setText("");
            tvConsultantName.setVisibility(View.GONE);
        }


        if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("0")) {
            tvRequestStatus.setText("");
            tvRequestStatus.setVisibility(View.GONE);
            if (isPast) {
                tvRequestStatus.setVisibility(View.VISIBLE);
                tvRequestStatus.setText("Unattended");
                tvUnattendedMessage.setVisibility(View.VISIBLE);
            } else {
                tvUnattendedMessage.setVisibility(View.GONE);
            }
            tvRequestStatus.setTextColor(Color.parseColor("#F3AA16"));
        } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("1")) {
            tvRequestStatus.setText("Approved");
            tvRequestStatus.setTextColor(Color.parseColor("#199403"));

            tvJoinCall.setVisibility(View.VISIBLE);
            if (isPast) {
                tvRequestStatus.setText("Unattended");
                tvRequestStatus.setTextColor(Color.parseColor("#F3AA16"));
                tvJoinCall.setVisibility(View.GONE);
                tvUnattendedMessage.setVisibility(View.VISIBLE);
            }
            tvConsultantName.setVisibility(View.VISIBLE);
        } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("2")) {
            tvRequestStatus.setText("Completed");
            tvRequestStatus.setTextColor(Color.parseColor("#0652C4"));
            tvConsultantName.setVisibility(View.VISIBLE);
            tvViewPrescription.setVisibility(View.VISIBLE);
        } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("4")) {
            tvRequestStatus.setTextColor(Color.RED);
            tvRequestStatus.setText("Rejected");
            tvConsultantName.setVisibility(View.VISIBLE);
        }


        tvViewPrescription.setOnClickListener(view13 -> {

            if (healthWorkerRequestListDetails.getRequestStatusCompleteMode().equalsIgnoreCase("1")) {
                Toast.makeText(c, "Please wait while downloading prescription.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                getPastPrescription(healthWorkerRequestListDetails.getHospCode(), healthWorkerRequestListDetails.getCRNo(), healthWorkerRequestListDetails.getRequestID(), progressBar);
            } else {
               /* Intent intent = new Intent(c, ViewWebPrescriptionActivity.class);
                intent.putExtra("details", healthWorkerRequestListDetailsArrayList.get(i));
                intent.putExtra("user", "patient");
                c.startActivity(intent);*/
            }
        });


        tvJoinCall.setOnClickListener(view14 -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
            builder1.setMessage("Please note that video consultation is available only when the doctor is online.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    c.getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                            jitsiVideoCall(healthWorkerRequestListDetails.getRequestID(), healthWorkerRequestListDetails.getConsName());
                        }
                    });

            builder1.setNegativeButton(
                    c.getString(R.string.cancel), null);
            AlertDialog alert11 = builder1.create();
            alert11.show();

        });
        view.setOnClickListener(view15 -> {
            if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("0") && isPast != true) {
                showPatientRequisitionDetails(healthWorkerRequestListDetails);
            }


        });
        return view;
    }

    private void showPatientRequisitionDetails(PatientRequestDetails patientDetails) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.patient_list_requisition_model);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        final TextView tvTittle = (TextView) dialog.findViewById(R.id.tv_tittle);
        final TextView tvWeight = (TextView) dialog.findViewById(R.id.tv_weight);
        final TextView tvHeight = (TextView) dialog.findViewById(R.id.tv_height);
        final TextView tvPatMedications = (TextView) dialog.findViewById(R.id.tv_pat_medications);
        final TextView tvPastDiagonsis = (TextView) dialog.findViewById(R.id.tv_past_diagonosis);
        final TextView tvAllergies = (TextView) dialog.findViewById(R.id.tv_allergies);
        final TextView tvDescription = (TextView) dialog.findViewById(R.id.tv_description);
        final TextView tvCovidScreeningResponse = (TextView) dialog.findViewById(R.id.tv_covid_screening_response);


        String fever = String.valueOf(patientDetails.getScrResponse().charAt(0));
        String cough = String.valueOf(patientDetails.getScrResponse().charAt(1));
        String soreThroat = String.valueOf(patientDetails.getScrResponse().charAt(2));
        String breathingDifficulty = String.valueOf(patientDetails.getScrResponse().charAt(3));
        String foreignTravel = String.valueOf(patientDetails.getScrResponse().charAt(4));

        if (fever.equalsIgnoreCase("N")) {
            fever = "Fever : " + "No, ";
        } else if (fever.equalsIgnoreCase("Y")) {
            fever = "Fever : " + "Yes, ";
        } else {
            fever = "";
        }

        if (cough.equalsIgnoreCase("N")) {
            cough = "Cough : " + "No, ";
        } else if (cough.equalsIgnoreCase("Y")) {
            cough = "Cough : " + "Yes, ";
        } else {
            cough = "";
        }

        if (soreThroat.equalsIgnoreCase("N")) {
            soreThroat = "Sore Throat : " + "No, ";
        } else if (soreThroat.equalsIgnoreCase("Y")) {
            soreThroat = "Sore Throat : " + "Yes, ";
        } else {
            soreThroat = "";
        }

        if (breathingDifficulty.equalsIgnoreCase("N")) {
            breathingDifficulty = "Breathing Difficulty : " + "No, ";
        } else if (breathingDifficulty.equalsIgnoreCase("Y")) {
            breathingDifficulty = "Breathing Difficulty : " + "Yes, ";
        } else {
            breathingDifficulty = "";
        }


        if (foreignTravel.equalsIgnoreCase("N")) {
            foreignTravel = "Foreign Travel/Contact with foreigners in last 14 days : " + "No";
        } else if (foreignTravel.equalsIgnoreCase("Y")) {
            foreignTravel = "Foreign Travel/Contact with foreigners in last 14 days : " + "Yes, ";
        } else {
            foreignTravel = "";
        }


        tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + foreignTravel);

        Button btnOk = dialog.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if ((patientDetails.getScrResponse() + patientDetails.getPatWeight() + patientDetails.getPatHeight() + patientDetails.getPatMedication() + patientDetails.getPatPastDiagnosis() + patientDetails.getPatAllergies() + patientDetails.getRmrks()).trim().equalsIgnoreCase("")) {
            //  Toast.makeText(c, "No information submitted.", Toast.LENGTH_SHORT).show();

            tvTittle.setText("No information submitted.");

        } else {

            if (!patientDetails.getPatWeight().equalsIgnoreCase("")) {
                tvWeight.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatHeight().equalsIgnoreCase("")) {
                tvHeight.setVisibility(View.VISIBLE);
            }

            if (!patientDetails.getPatMedication().equalsIgnoreCase("")) {
                tvPatMedications.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatPastDiagnosis().equalsIgnoreCase("")) {
                tvPastDiagonsis.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getPatAllergies().equalsIgnoreCase("")) {
                tvAllergies.setVisibility(View.VISIBLE);
            }
            if (!patientDetails.getRmrks().equalsIgnoreCase("")) {
                tvDescription.setVisibility(View.VISIBLE);
            }
            tvWeight.setText("Weight: " + patientDetails.getPatWeight());
            tvHeight.setText("Height: " + patientDetails.getPatHeight());
            tvPatMedications.setText("Medications: " + patientDetails.getPatMedication());
            tvPastDiagonsis.setText("Past Diagnosis: " + patientDetails.getPatPastDiagnosis());
            tvAllergies.setText("Allergies: " + patientDetails.getPatAllergies());
            tvDescription.setText("Problem Description: " + patientDetails.getRmrks());


        }
        dialog.show();
    }


    private void getDoctorsMessages(ArrayList<DoctorMessage> chatHistory, PatientRequestDetails patientRequestDetails) {

        DoctorMessageAdapter adapter;
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.patients_message_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        ListView messagesContainer = (ListView) dialog.findViewById(R.id.messagesContainer);
        ImageButton btnDismiss = dialog.findViewById(R.id.btn_dismiss);
        EditText edtMessage = dialog.findViewById(R.id.edt_message);
        ImageButton btnSend = dialog.findViewById(R.id.btn_send);
        adapter = new DoctorMessageAdapter(c, chatHistory);
        messagesContainer.setAdapter(adapter);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientMessage = edtMessage.getText().toString();
                if (patientMessage.isEmpty()) {
                    Toast.makeText(c, "Please enter a message.", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(patientRequestDetails, patientMessage, "0");
                    dialog.dismiss();
//                   chatHistory.add(new DoctorMessage(patientMessage,patientRequestDetails.getPatName(),"",true));
//                   notifyDataSetChanged();
                }
            }
        });

        if (chatHistory.size() == 0) {
            Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
        }
        else  {
            dialog.show();
        }

    }


    private void showRateUsDialog(String requestId, String hospCode) {

        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rate_us_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        EditText edtReview = dialog.findViewById(R.id.edt_review);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0) {
                    Toast.makeText(c, "Please give ratings", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.i(TAG, "onClick: " + ratingBar.getRating());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("requestId", requestId);
                        jsonObject.put("hospCode", hospCode);
                        jsonObject.put("remarks", edtReview.getText().toString());
                        jsonObject.put("feedback", String.valueOf(ratingBar.getRating()));
                        callFeedbackService(jsonObject);
                        Toast.makeText(c, "Feedback has been submitted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } catch (JSONException ex) {
                        Log.i(TAG, "onClick: jsonException");
                    }

                }

            }
        });

        dialog.show();

    }

    private void callFeedbackService(JSONObject jsonObject) {
        RequestQueue requestQueue = Volley.newRequestQueue(c);

        String url = ServiceUrl.feedbackUrl;
        Log.i("url", "makeRequest: " + url);

        final String requestBody = jsonObject.toString();

        Log.i("requestBody", "bookAppointment: " + requestBody);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> Log.i("feedbackresponse", "onResponse: " + response), error -> {
            Log.i("bookappintment", "onResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, c);

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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cdac", "");


                return params;
            }

        };



        MySingleton.getInstance(c).addToRequestQueue(request);
    }


    public void getPastPrescription(String hospCode, final String uhid, final String requestId, ProgressBar progressBar) {
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.i(TAG, "getPastPrescription: " + ServiceUrl.getPastPrescription + hospCode + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastPrescription + hospCode + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response is ", "onResponse: " + response);
                try {

                    if (response.length() == 0) {
                        Toast.makeText(c, "FTP server is down , pls tryn after sometime.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveBase64Pdf(c, "prescription", "prescription", response);
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (final Exception e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressBar.setVisibility(View.GONE);
                }


            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            Toast.makeText(c, "FTP server is down , pls tryn after sometime.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, c);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(c).addToRequestQueue(request);


    }



    private void jitsiVideoCall(String requestId, String consultantName) {
        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
//            serverURL = new URL("https://meet.jit.si");
            serverURL = new URL("https://mconsultancy.uat.dcservices.in/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setSubject(consultantName)
                //.setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom("https://mconsultancy.uat.dcservices.in/" + requestId)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(c, options);
    }


    private void sendMessage(PatientRequestDetails patientRequestDetails, String message, String isView) {

        ArrayList<DoctorMessage> messageArrayList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        ManagingSharedData msd = new ManagingSharedData(c);

        Log.i(TAG, "getMessage: " + ServiceUrl.getUpdateDoctorMessage + "requestID=" + patientRequestDetails.getRequestID() + "&sentBy=p&docMessage=" + message + "&consltID=&consltName=" + patientRequestDetails.getPatName() + "&hospCode=" + patientRequestDetails.getHospCode() + "&isView=" + isView);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getUpdateDoctorMessage + "requestID=" + patientRequestDetails.getRequestID() + "&sentBy=p&docMessage=" + message + "&consltID=&consltName=" + patientRequestDetails.getPatName() + "&hospCode=" + patientRequestDetails.getHospCode() + "&isView=" + isView, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    Log.i(TAG, "messageresponse" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("Status");
                    JSONObject data = jsonObject.getJSONObject("Data");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = data.getJSONArray("docMessage");
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String timeStamp = c.getString("TimeStamp");
                            String userName = c.getString("UserName");
                            String sentBy = c.getString("SentBy");
                            String message = c.getString("Message");
                            boolean isMe = false;
                            if (sentBy.equalsIgnoreCase("p")) {
                                isMe = true;
                            } else {
                                isMe = false;
                            }
                            messageArrayList.add(new DoctorMessage(message, userName, timeStamp, isMe));


                        }
                        getDoctorsMessages(messageArrayList, patientRequestDetails);

                    } else {
                        Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(c).addToRequestQueue(request);
    }




}