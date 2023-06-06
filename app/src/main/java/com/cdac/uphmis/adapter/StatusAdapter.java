package com.cdac.uphmis.adapter;

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
import android.view.MotionEvent;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.BuildConfig;
import com.cdac.uphmis.R;
import com.cdac.uphmis.chat.DoctorMessage;
import com.cdac.uphmis.chat.DoctorMessageAdapter;
import com.cdac.uphmis.covid19.model.PatientRequestDetails;
import com.cdac.uphmis.precriptionView.PrescriptionListActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import java.util.TreeSet;


import static android.content.ContentValues.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;

import ru.nikartm.support.ImageBadgeView;

public class StatusAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    ArrayList<PatientRequestDetails> healthWorkerRequestListDetailsArrayList;
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    Context c;

    //boolean isPast;
    public StatusAdapter(Context c) {
        mInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.c = c;
    }


    public StatusAdapter(Context c, ArrayList<PatientRequestDetails> healthWorkerRequestListDetailsArrayList, boolean isPast) {
        mInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.c = c;
        this.healthWorkerRequestListDetailsArrayList = healthWorkerRequestListDetailsArrayList;
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<PatientRequestDetails> healthWorkerRequestListDetailsArrayList, boolean isPast) {
        if (this.healthWorkerRequestListDetailsArrayList == null) {
            this.healthWorkerRequestListDetailsArrayList = healthWorkerRequestListDetailsArrayList;
        }

        this.healthWorkerRequestListDetailsArrayList.addAll(healthWorkerRequestListDetailsArrayList);


        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(String headerName) {
        if (healthWorkerRequestListDetailsArrayList == null) {
            this.healthWorkerRequestListDetailsArrayList = new ArrayList<>();
        }
        healthWorkerRequestListDetailsArrayList.add(new PatientRequestDetails("", "", "", "", "", "", "", headerName, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", true, "", "",""));
        sectionHeader.add(healthWorkerRequestListDetailsArrayList.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return healthWorkerRequestListDetailsArrayList.size();
    }

    @Override
    public PatientRequestDetails getItem(int position) {
        return healthWorkerRequestListDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(i);

        if (view == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    view = mInflater.inflate(R.layout.upcoming_request_list_model, null);

                    holder.progressBar = view.findViewById(R.id.progressbar);
                    holder.tvUnitname = view.findViewById(R.id.tv_unit_name);
                    holder.tvPatName = view.findViewById(R.id.tv_patient_name);
                    holder.tvConsultantName = view.findViewById(R.id.tv_consultant_name);
                    holder.tvRequestStatus = view.findViewById(R.id.tv_request_status);
                    holder.tvRequestedDate = view.findViewById(R.id.tv_requested_date);
                    holder.tvDoctorMessage = view.findViewById(R.id.tv_doc_message);
                    holder.smsBadgeCount = view.findViewById(R.id.sms_badge_count);

                    holder.tvViewPrescription = view.findViewById(R.id.tv_view_prescription);
                    holder.tvDeptName = view.findViewById(R.id.tv_dept_name);
                    holder.tvJoinCall = view.findViewById(R.id.tv_join_call);
                    holder.tvAppointmentDateTime = view.findViewById(R.id.tv_appointment_date);
                    holder.tvRateUs = view.findViewById(R.id.tv_rate_us);
                    holder.tvUnattendedMessage = view.findViewById(R.id.tv_unattended_message);
                    holder.tvConsultationTime = view.findViewById(R.id.tv_consultation_time);
                    break;
                case TYPE_HEADER:
                    view = mInflater.inflate(R.layout.status_header_item, null);
                    holder.tvHeader = (TextView) view.findViewById(R.id.tv_header);
                    break;
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (rowType == TYPE_ITEM) {
            PatientRequestDetails healthWorkerRequestListDetails = (PatientRequestDetails) this.getItem(i);


            holder.tvJoinCall.setVisibility(View.GONE);
            holder.tvUnattendedMessage.setVisibility(View.GONE);
            holder.tvConsultationTime.setVisibility(View.GONE);

            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = format1.parse(healthWorkerRequestListDetails.getApptDate().substring(0, 10));
                SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                String displayDate = format2.format(date1);
                holder.tvAppointmentDateTime.setText(displayDate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            holder.tvUnitname.setText(healthWorkerRequestListDetails.getDeptUnitName());
            holder.tvDeptName.setText(healthWorkerRequestListDetails.getHospitalName());

            holder.tvViewPrescription.setVisibility(View.GONE);
            holder.tvRequestedDate.setText(healthWorkerRequestListDetails.getRequestID() + "/" + healthWorkerRequestListDetails.getDate());

            if (!healthWorkerRequestListDetails.getDocMessage().equalsIgnoreCase("")) {
                holder.tvDoctorMessage.setVisibility(View.VISIBLE);
                showMessageCount(holder, healthWorkerRequestListDetails.getDocMessage());
            } else {
                holder.tvDoctorMessage.setVisibility(View.GONE);
                holder.smsBadgeCount.setVisibility(View.GONE);
            }


            if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("2")) {
                holder.tvRateUs.setVisibility(View.VISIBLE);
            } else {
                holder.tvRateUs.setVisibility(View.GONE);
            }


            holder.tvRateUs.setOnClickListener(view1 -> showRateUsDialog(healthWorkerRequestListDetails.getRequestID(), healthWorkerRequestListDetails.getHospCode()));


            ViewHolder finalHolder1 = holder;
            holder.tvDoctorMessage.setOnClickListener(view12 -> {

                //view doctor's message
                sendMessage(healthWorkerRequestListDetails, "", "1", finalHolder1);
            });


            holder.tvPatName.setText(healthWorkerRequestListDetails.getPatName() + " (CR No: " + healthWorkerRequestListDetails.getCRNo() + ")");
            if (!healthWorkerRequestListDetails.getConsName().trim().equalsIgnoreCase("")) {
                holder.tvConsultantName.setText("Your Appointment with " + healthWorkerRequestListDetails.getConsName());
                holder.tvConsultantName.setVisibility(View.VISIBLE);
            } else {
                holder.tvConsultantName.setText("");
                holder.tvConsultantName.setVisibility(View.GONE);
            }


            if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("0")) {
                holder.tvRequestStatus.setText("");
                holder.tvRequestStatus.setVisibility(View.GONE);
                if (healthWorkerRequestListDetails.isPast()) {
                    holder.tvRequestStatus.setVisibility(View.VISIBLE);
                    holder.tvRequestStatus.setText("Unattended");
                    holder.tvUnattendedMessage.setVisibility(View.VISIBLE);
                } else {
                    holder.tvUnattendedMessage.setVisibility(View.GONE);
                }
                holder.tvRequestStatus.setTextColor(Color.parseColor("#F3AA16"));
            } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("1")) {
                holder.tvRequestStatus.setText("Approved");
                holder.tvRequestStatus.setTextColor(Color.parseColor("#199403"));

                //enableVideoCall(healthWorkerRequestListDetails.getDocMessage());
                holder.tvJoinCall.setVisibility(View.VISIBLE);
                if (healthWorkerRequestListDetails.isPast()) {
                    holder.tvRequestStatus.setText("Unattended");
                    holder.tvRequestStatus.setTextColor(Color.parseColor("#F3AA16"));
                    //    holder.tvJoinCall.setVisibility(View.GONE);
                    holder.tvUnattendedMessage.setVisibility(View.VISIBLE);
                }

                holder.tvConsultationTime.setText(healthWorkerRequestListDetails.getConsultationTime());
                if (healthWorkerRequestListDetails.getConsultationTime().equalsIgnoreCase("Join Call"))
                {
                    holder.tvConsultationTime.setVisibility(View.GONE);
                    holder.tvJoinCall.setVisibility(View.VISIBLE);

                }else
                {
                    holder.tvConsultationTime.setVisibility(View.VISIBLE);
                    holder.tvJoinCall.setVisibility(View.GONE);
                }


                holder.tvConsultantName.setVisibility(View.VISIBLE);
            } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("2")) {
                holder.tvRequestStatus.setText("Completed");
                holder.tvRequestStatus.setTextColor(Color.parseColor("#0652C4"));
                holder.tvConsultantName.setVisibility(View.VISIBLE);
                holder.tvViewPrescription.setVisibility(View.VISIBLE);
            } else if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("4")) {
                holder.tvRequestStatus.setTextColor(Color.RED);
                holder.tvRequestStatus.setText("Rejected");
                holder.tvConsultantName.setVisibility(View.VISIBLE);
            }


            ViewHolder finalHolder = holder;
            holder.tvViewPrescription.setOnClickListener(view13 -> {
                Toast.makeText(c, "Please wait while downloading prescription.", Toast.LENGTH_SHORT).show();
                finalHolder.progressBar.setVisibility(View.VISIBLE);
                if (healthWorkerRequestListDetails.getRequestStatusCompleteMode().equalsIgnoreCase("1")) {

                    getPastPrescription(healthWorkerRequestListDetails.getHospCode(), healthWorkerRequestListDetails.getCRNo(), healthWorkerRequestListDetails.getRequestID(), finalHolder.progressBar);
                } else {
                    getPastWebPrescription(healthWorkerRequestListDetails, finalHolder.progressBar);

                }
            });


            holder.tvJoinCall.setOnClickListener(view14 -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
                builder1.setMessage("Please note that video consultation is available only when the doctor is online.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                                jitsiVideoCall(healthWorkerRequestListDetails.getRequestID(), healthWorkerRequestListDetails.getConsName());
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel", null);
                AlertDialog alert11 = builder1.create();
                alert11.show();

            });
            view.setOnClickListener(view15 -> {
                if (healthWorkerRequestListDetails.getRequestStatus().equalsIgnoreCase("0") && healthWorkerRequestListDetails.isPast() != true) {
                    showPatientRequisitionDetails(healthWorkerRequestListDetails);
                }


            });
        } else if (rowType == TYPE_HEADER) {
            holder.tvHeader.setText(healthWorkerRequestListDetailsArrayList.get(i).getRequestStatus());
//            holder.tvHeader.setText("hello");
        }


        return view;
    }

    private void enableVideoCall(String docMessage) {
        try {
            String[] arMessage = docMessage.split("#");
            if (arMessage.length > 0) {
                String message = arMessage[arMessage.length - 1].split("::")[3].trim();


                String consultationTime = message.substring(message.length() - 10);
//consultationTime="10:30 AM";
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

                Date EndTime = dateFormat.parse(consultationTime);

                Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));

                if (CurrentTime.after(EndTime)) {
                    System.out.println("timeeee end ");
                    Log.i(TAG, "after time: " + CurrentTime + " , " + EndTime);
                } else {
                    Log.i(TAG, "befor time: " + CurrentTime);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showMessageCount(ViewHolder holder, String docMessage) {
        try {
            String[] arMessage = docMessage.split("#");
            if (arMessage.length > 0) {
                holder.smsBadgeCount.setBadgeValue(arMessage.length);
                holder.smsBadgeCount.setVisibility(View.VISIBLE);

                //   holder.tvConsultationTime.setText(message);

            } else {
                holder.smsBadgeCount.setVisibility(View.GONE);
            }

            if (holder.tvDoctorMessage.getVisibility() == View.GONE) {
                holder.smsBadgeCount.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void getPastWebPrescription(PatientRequestDetails healthWorkerRequestListDetails, ProgressBar progressBar) {

        //        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.i(TAG, "getPastPrescription: " + ServiceUrl.getPastWebPrescription);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastWebPrescription + "hosp_code=" + healthWorkerRequestListDetails.getHospCode() + "&Modval=5&CrNo=" + healthWorkerRequestListDetails.getCRNo() + "&episodeCode=" + healthWorkerRequestListDetails.getEpisodeCode() + "&visitNo=" + healthWorkerRequestListDetails.getEpisodeVisitNo() + "&seatId=0&Entrydate=%22%22", response -> {

            Log.i("response is ", "onResponse: " + response);
            try {

                if (response.length() == 0) {
                    Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
                } else {
//                    saveBase64Pdf(response);
                    saveBase64Pdf(c, "prescription", "prescription", response);

                }
                progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                progressBar.setVisibility(View.GONE);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, c);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(c).addToRequestQueue(request);
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


    /*    String fever = String.valueOf(patientDetails.getScrResponse().charAt(0));
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
        } */

        if (patientDetails.getScrResponse().length() < 10) {
            patientDetails.setScrResponse(String.format("%-10s", patientDetails.getScrResponse()));
            Log.i(TAG, "btnPatientDetails: " + patientDetails.getScrResponse());
        }

        String fever = String.valueOf(patientDetails.getScrResponse().charAt(0));
        String cough = String.valueOf(patientDetails.getScrResponse().charAt(1));
        String soreThroat = String.valueOf(patientDetails.getScrResponse().charAt(2));
        String breathingDifficulty = String.valueOf(patientDetails.getScrResponse().charAt(3));
//        String foreignTravel = String.valueOf(patientDetails.getScrResponse().charAt(4));


        String congestion = String.valueOf(patientDetails.getScrResponse().charAt(4));
        String bodyAche = String.valueOf(patientDetails.getScrResponse().charAt(5));
        String pinkEyes = String.valueOf(patientDetails.getScrResponse().charAt(6));
        String smell = String.valueOf(patientDetails.getScrResponse().charAt(7));
        String hearingImpairment = String.valueOf(patientDetails.getScrResponse().charAt(8));
        String gastroIntestinal = String.valueOf(patientDetails.getScrResponse().charAt(9));

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

//new parameters
        if (congestion.equalsIgnoreCase("N")) {
            congestion = "Chest Congestion or Runny Nose : " + "No";
        } else if (congestion.equalsIgnoreCase("Y")) {
            congestion = "Chest Congestion or Runny Nose : " + "Yes, ";
        } else {
            congestion = "";
        }

        if (bodyAche.equalsIgnoreCase("N")) {
            bodyAche = "Body Ache : " + "No";
        } else if (bodyAche.equalsIgnoreCase("Y")) {
            bodyAche = "Body Ache : " + "Yes, ";
        } else {
            bodyAche = "";
        }

        if (pinkEyes.equalsIgnoreCase("N")) {
            pinkEyes = "Pink Eyes : " + "No";
        } else if (pinkEyes.equalsIgnoreCase("Y")) {
            pinkEyes = "Pink Eyes : " + "Yes, ";
        } else {
            pinkEyes = "";
        }

        if (smell.equalsIgnoreCase("N")) {
            smell = "Loss of Senses of Smell and Taste : " + "No";
        } else if (smell.equalsIgnoreCase("Y")) {
            smell = "Loss of Senses of Smell and Taste : " + "Yes, ";
        } else {
            smell = "";
        }

        if (hearingImpairment.equalsIgnoreCase("N")) {
            hearingImpairment = "Hearing Impairment : " + "No";
        } else if (hearingImpairment.equalsIgnoreCase("Y")) {
            hearingImpairment = "Hearing Impairment : " + "Yes, ";
        } else {
            hearingImpairment = "";
        }

        if (gastroIntestinal.equalsIgnoreCase("N")) {
            gastroIntestinal = "Gastrointestinal Symptoms : " + "No";
        } else if (gastroIntestinal.equalsIgnoreCase("Y")) {
            gastroIntestinal = "Gastrointestinal Symptoms : " + "Yes, ";
        } else {
            gastroIntestinal = "";
        }


//        tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + foreignTravel);
        tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + congestion + bodyAche + pinkEyes + smell + hearingImpairment + gastroIntestinal);
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


    private void getDoctorsMessages(ArrayList<DoctorMessage> chatHistory, PatientRequestDetails patientRequestDetails, ViewHolder holder) {

        DoctorMessageAdapter adapter;
//        final Dialog dialog = new Dialog(c);
        final BottomSheetDialog dialog = new BottomSheetDialog(c, R.style.bottomSheetStyleWrapperMessage);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.patients_message_dialog);
        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);*/
        ListView messagesContainer = (ListView) dialog.findViewById(R.id.messagesContainer);
        TextView btnDismiss = dialog.findViewById(R.id.btn_dismiss);
        EditText edtMessage = dialog.findViewById(R.id.edt_message);
        ImageButton btnSend = dialog.findViewById(R.id.btn_send);
        adapter = new DoctorMessageAdapter(c, chatHistory);
        messagesContainer.setAdapter(adapter);


        messagesContainer.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        btnDismiss.setOnClickListener(view -> {
            dialog.dismiss();
        });


        if (chatHistory.size() == 0) {
            Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
        } else {
            holder.smsBadgeCount.setBadgeValue(chatHistory.size());
            dialog.show();
        }

        btnSend.setOnClickListener(view -> {
            String patientMessage = edtMessage.getText().toString();
            if (patientMessage.isEmpty()) {
                Toast.makeText(c, "Please enter a message.", Toast.LENGTH_SHORT).show();
            } else {
                sendMessage(patientRequestDetails, patientMessage, "0", holder);
                dialog.dismiss();
            }
        });

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
//        RequestQueue requestQueue = Volley.newRequestQueue(c);

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
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.i(TAG, "getPastPrescription: " + ServiceUrl.getPastPrescription + hospCode + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastPrescription + hospCode + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response is ", "onResponse: " + response);
                try {

                    if (response.length() == 0) {
                        Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            AppUtilityFunctions.handleExceptions(error, c);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(c).addToRequestQueue(request);


    }

    /*public void saveBase64Pdf(String data) {
        try {
            String path = c.getFilesDir().getAbsolutePath() + "/prescription.pdf";
            final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/prescription .pdf");
            byte[] pdfAsBytes = Base64.decode(data, 0);
            FileOutputStream os;
            os = new FileOutputStream(path, false);
            os.write(pdfAsBytes);
            viewPdf("prescription");
            os.flush();
            os.close();

        } catch (Exception ex) {
            Log.i("pdf exception", "saveBase64Pdf: " + ex);
        }
    }

    private void viewPdf(String fileName) {

        // Setting the intent for pdf reader
        String path = getReportPath(fileName, "pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(path);
            Uri uri = FileProvider.getUriForFile(c.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            c.startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "application/pdf");
            intent = Intent.createChooser(intent, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        }
    }*/

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
//                .setWelcomePageEnabled(false)
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


    private void sendMessage(PatientRequestDetails patientRequestDetails, String message, String isView, ViewHolder holder) {

        ArrayList<DoctorMessage> messageArrayList = new ArrayList<>();
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
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
                        if (isView.equalsIgnoreCase("0")) {
                            sendFCMPush("Message From " + patientRequestDetails.getPatName(), message, patientRequestDetails.getDoctorToken());
                        }
                        getDoctorsMessages(messageArrayList, patientRequestDetails, holder);

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

    private void sendFCMPush(String title, String message, String consToken) {

        String token = consToken;

        JSONObject obj = null;


        try {


            obj = new JSONObject();
            JSONObject objData1 = new JSONObject();

            // objData.put("data", msg);
            objData1.put("title", title);
            objData1.put("content", message);
            objData1.put("navigateTo", "");


            obj.put("data", objData1);
            obj.put("to", token);

            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServiceUrl.testurl + "callPushNotfication/call", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
//                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }


    public static class ViewHolder {
        public TextView tvHeader;
        public TextView tvUnitname;
        public TextView tvPatName;
        public TextView tvConsultantName;
        public TextView tvRequestStatus;
        public TextView tvRequestedDate;
        public TextView tvDoctorMessage;
        public TextView tvViewPrescription;
        public TextView tvDeptName;
        public TextView tvJoinCall;
        public TextView tvAppointmentDateTime;
        public TextView tvRateUs;
        public TextView tvUnattendedMessage;
        public TextView tvConsultationTime;
        public ProgressBar progressBar;


        private ImageBadgeView smsBadgeCount;

    }
}