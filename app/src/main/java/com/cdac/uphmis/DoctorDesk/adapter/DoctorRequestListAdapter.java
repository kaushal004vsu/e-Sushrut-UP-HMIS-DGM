package com.cdac.uphmis.DoctorDesk.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.DoctorDesk.DeskActivity;
import com.cdac.uphmis.DoctorDesk.DoctorRequestListActivity;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.R;
import com.cdac.uphmis.chat.DoctorMessage;
import com.cdac.uphmis.chat.DoctorMessageAdapter;
import com.cdac.uphmis.covid19.ViewDocNewActivity;
import com.cdac.uphmis.opdLite.DeskHomeActivity;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.nikartm.support.ImageBadgeView;

import static android.content.ContentValues.TAG;
import static com.cdac.uphmis.util.AppUtilityFunctions.saveBase64Pdf;

public class DoctorRequestListAdapter extends BaseAdapter implements Filterable {
    private Context c;


    private ArrayList<DoctorReqListDetails> mOriginalValues; // Original Values
    private ArrayList<DoctorReqListDetails> mDisplayedValues;
    private boolean isUpComing = false;
    private LayoutInflater inflater;

    public DoctorRequestListAdapter(Context c, ArrayList<DoctorReqListDetails> doctorReqListDetailsArrayList, boolean isUpComing) {
        this.c = c;
        this.mOriginalValues = doctorReqListDetailsArrayList;
        this.mDisplayedValues = doctorReqListDetailsArrayList;
        inflater = LayoutInflater.from(c);


        this.isUpComing = isUpComing;

    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ViewHolder {
        TextView tvRequestId, tvRaisedBy, tvSlotDate, tvRequestedDate, tvCrno, tvPatName, tvStatus, tvDeptUnitName, tvViewMessage, tvConsultantName;//tvPatMobileNo, tvAccept, tvReject, tvStatus, tvSendMessage;
        //    ImageView btnCall, btnVideoCall, btnViewDocument;
        ImageView btnViewDocument;
        // LinearLayout llAcceptReject;

        TextView tvShowRequest;

        ProgressBar progressBar;
        ImageBadgeView smsBadgeView;

        TimePicker timePicker;
    }


    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();

//            view = inflater.inflate(R.layout.doctor_request_list_model, null);
            view = inflater.inflate(R.layout.doctor_req_list_model, null);

            holder.tvRequestId = view.findViewById(R.id.tv_request_id);
            holder.tvSlotDate = view.findViewById(R.id.tv_slot_date);
            holder.tvRaisedBy = view.findViewById(R.id.tv_raised_by);
//            holder.tvRequestedDate = view.findViewById(R.id.tv_requested_date);
            holder.tvCrno = view.findViewById(R.id.tv_crno);
            holder.progressBar = view.findViewById(R.id.progressbar);

            holder.tvPatName = view.findViewById(R.id.tv_patient_name);
            holder.tvStatus = view.findViewById(R.id.tv_status);
            holder.tvDeptUnitName = view.findViewById(R.id.tv_dept_unit_name);

            holder.tvShowRequest = view.findViewById(R.id.tv_show_request);
            holder.btnViewDocument = view.findViewById(R.id.btn_view_document);


            holder.tvViewMessage = view.findViewById(R.id.tv_view_messages);
            holder.tvConsultantName = view.findViewById(R.id.tv_consultant_name);

            holder.smsBadgeView = view.findViewById(R.id.sms_badge_count);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String displayDate = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = format1.parse(mDisplayedValues.get(i).getApptDate());
            SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
            displayDate = format2.format(date1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        holder.tvRequestId.setText("Raised on: " + mDisplayedValues.get(i).getpVisitdate());


        holder.smsBadgeView.setBadgeValue(0);
        if (!mDisplayedValues.get(i).getDocMessage().trim().isEmpty()) {
            holder.smsBadgeView.setVisibility(View.VISIBLE);
            holder.tvViewMessage.setVisibility(View.VISIBLE);
            showMessageCount(holder, mDisplayedValues.get(i).getDocMessage().trim());
        } else {
            holder.smsBadgeView.setVisibility(View.GONE);
            holder.tvViewMessage.setVisibility(View.GONE);
        }


//        holder.tvSlotDate.setText("Appointment Slot: " + displayDate + ", " + mDisplayedValues.get(i).getApptStartTime());
        holder.tvSlotDate.setText("Appointment Slot: " + displayDate);

        holder.tvDeptUnitName.setText("Unit: " + mDisplayedValues.get(i).getDeptUnitName());

        if (!mDisplayedValues.get(i).getIsDocUploaded().equalsIgnoreCase("0")) {
            holder.btnViewDocument.setVisibility(View.VISIBLE);


        } else {
            holder.btnViewDocument.setVisibility(View.GONE);
        }

        String raisedBy = "Raised By: ";
        if (mDisplayedValues.get(i).getRequestID().startsWith("P")) {
//            raisedBy = raisedBy + "Registered Patient";
            raisedBy = raisedBy + "Self (" + mDisplayedValues.get(i).getRequestID() + ")";
        } else if (mDisplayedValues.get(i).getRequestID().startsWith("N")) {
            raisedBy = raisedBy + "New Patient (" + mDisplayedValues.get(i).getRequestID() + ")";
        } else if (mDisplayedValues.get(i).getRequestID().startsWith("H")) {
            raisedBy = raisedBy + "Health Worker (" + mDisplayedValues.get(i).getRequestID() + ")";
        }
        holder.tvRaisedBy.setText(raisedBy);
//        holder.tvRequestedDate.setText("Req Date: " + mDisplayedValues.get(i).getpVisitdate());


        if (mDisplayedValues.get(i).getCRNo().length() != 15) {
            holder.tvCrno.setText("Temp Reg No: " + mDisplayedValues.get(i).getCRNo());
        } else {
            holder.tvCrno.setText("CR No: " + mDisplayedValues.get(i).getCRNo());
        }


        holder.tvPatName.setText(mDisplayedValues.get(i).getPatName() + " (" + mDisplayedValues.get(i).getPatGender() + "/" + mDisplayedValues.get(i).getPatAge() + "/" + mDisplayedValues.get(i).getPatMobileNo().substring(2) + ")");


        if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("0")) {

            holder.tvShowRequest.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.GONE);
            holder.tvConsultantName.setVisibility(View.GONE);
        } else {
            if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("1")) {
//                holder.tvStatus.setText("Approved");
                holder.tvStatus.setText("Tap to attend");
                holder.tvStatus.setTextColor(Color.parseColor("#199403"));
            } else if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("2")) {
                holder.tvStatus.setText("Completed");
                holder.tvStatus.setTextColor(Color.parseColor("#0652C4"));
            } else {
                holder.tvStatus.setText("Rejected");
                holder.tvStatus.setTextColor(Color.RED);
            }

            holder.tvShowRequest.setVisibility(View.GONE);

            holder.tvStatus.setVisibility(View.VISIBLE);


            if (!mDisplayedValues.get(i).getConsName().trim().equalsIgnoreCase("")) {
                holder.tvConsultantName.setText("Consultant Name: " + mDisplayedValues.get(i).getConsName());
            } else {
                holder.tvConsultantName.setText("");
            }
        }
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.doctor_message_model);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        final EditText edtDoctorMessage = dialog.findViewById(R.id.edt_doctor_message);
        //LinearLayout llAcceptReject = dialog.findViewById(R.id.ll_accept_reject);
        TextView tvAccept = dialog.findViewById(R.id.tv_accept);
        TextView tvReject = dialog.findViewById(R.id.tv_reject);
        TextView tvSendMessage = dialog.findViewById(R.id.tv_send_message);
        TextView tvSelectAttendTime = dialog.findViewById(R.id.tv_select_attend_time);

        final String[] formattedTime = {""};
        final String[] consultationTimeStamp = {""};

        tvSelectAttendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                final int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                TimePickerDialog picker = new TimePickerDialog(c,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat timeStamp = new SimpleDateFormat("dd-MMM-yy ");
                                String time = timeStamp.format(c) + sHour + ":" + sMinute + ":00";

                                SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
                                Date date = null;
                                try {
                                    date = fmt.parse(time);
                                } catch (ParseException e) {

                                    e.printStackTrace();
                                }

                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");


                                formattedTime[0] = fmtOut.format(date);
                                consultationTimeStamp[0] = fmt.format(date);
                                Log.i(TAG, "onTimeSet: " + String.valueOf(fmt.format(date)));
                                tvSelectAttendTime.setText("Approximate time of consultation " + formattedTime[0]);
                            }
                        }, hour, minutes, false);
                picker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        tvSelectAttendTime.setText("Please convey approximate time of consultation to patient by tapping icon & selecting time");
                    }
                });
                picker.show();
            }
        });

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

//        showPatientDetails(dialog, mDisplayedValues.get(i));


        holder.tvShowRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((mDisplayedValues.get(i).getScrResponse() + mDisplayedValues.get(i).getPatWeight() + mDisplayedValues.get(i).getPatHeight() + mDisplayedValues.get(i).getPatMedication() + mDisplayedValues.get(i).getPatPastDiagnosis() + mDisplayedValues.get(i).getPatAllergies() + mDisplayedValues.get(i).getRmrks()).trim().equalsIgnoreCase("")) {
                    //   Toast.makeText(c, "No information submitted.", Toast.LENGTH_SHORT).show();
                    tvTitle.setText("No information submitted.");
                    dialog.show();
                } else {
                    showPatientDetails(dialog, mDisplayedValues.get(i));
                    dialog.show();
                }
            }
        });

        if (isUpComing) {
            tvAccept.setVisibility(View.GONE);
            tvSelectAttendTime.setVisibility(View.GONE);

//            holder.tvShowRequest.setText("Reject/Send Message");
            holder.tvShowRequest.setText("View/Message");
        } else {
            tvSelectAttendTime.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.VISIBLE);

            holder.tvShowRequest.setText("Tap to approve");

        }
        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSendMessage.setEnabled(false);
                tvReject.setEnabled(false);
                if (tvSelectAttendTime.getText().toString().trim().equalsIgnoreCase("Please convey approximate time of consultation to patient by tapping icon & selecting time")) {
                    Toast.makeText(c, "Select approximate time of consultation", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (edtDoctorMessage.getText().toString().equalsIgnoreCase("")) {
//                    edtDoctorMessage.setError("Please enter a message.");
//                    return;
//                }
                ManagingSharedData msd = new ManagingSharedData(c);
                String encodedToken = "";
                try {
                    encodedToken = URLEncoder.encode(msd.getToken(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String message = "Approximate time of consultation " + formattedTime[0] + ". ";
                updateRequestStatus(mDisplayedValues.get(i).getRequestID(), "1", message + edtDoctorMessage.getText().toString(), encodedToken, mDisplayedValues.get(i), consultationTimeStamp[0]);
                //refreshActivity();
                dialog.dismiss();

            }
        });

        tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSendMessage.setEnabled(false);
                tvAccept.setEnabled(false);
//                if (!edtDoctorMessage.getText().toString().equalsIgnoreCase("")) {
//                    updateDoctorMessage(mDisplayedValues.get(i).getRequestID(), edtDoctorMessage.getText().toString());
//                }
                if (edtDoctorMessage.getText().toString().trim().equalsIgnoreCase("")) {
//                    Toast.makeText(c, "Please enter reason", Toast.LENGTH_SHORT).show();
                    edtDoctorMessage.setError("Please enter a message for patient providing reason for rejecting econsultation request.");
                } else {
                    ManagingSharedData msd = new ManagingSharedData(c);
                    String encodedToken = "";
                    try {
                        encodedToken = URLEncoder.encode(msd.getToken(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    updateRequestStatus(mDisplayedValues.get(i).getRequestID(), "4", edtDoctorMessage.getText().toString(), encodedToken, mDisplayedValues.get(i), "");
                    refreshActivity();
                    dialog.dismiss();
                }
            }
        });


        tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAccept.setEnabled(false);
                tvReject.setEnabled(false);
                if (!edtDoctorMessage.getText().toString().equalsIgnoreCase("")) {
//                    updateDoctorMessage(mDisplayedValues.get(i), edtDoctorMessage.getText().toString());
                    String doctorMessage = edtDoctorMessage.getText().toString();
                    if (doctorMessage.trim().isEmpty()) {
                        Toast.makeText(c, "Please enter message.", Toast.LENGTH_SHORT).show();
                    } else {
                        getMessage(mDisplayedValues.get(i), doctorMessage, "0");
                        edtDoctorMessage.getText().clear();
                        dialog.dismiss();
                        refreshActivity();
                    }
                } else {
                    edtDoctorMessage.setError("Please enter a message for patient.");
                }


            }
        });

        holder.btnViewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(c, ViewDocumentActivity.class);
                Intent intent = new Intent(c, ViewDocNewActivity.class);
                intent.putExtra("requestId", mDisplayedValues.get(i).getRequestID());
                c.startActivity(intent);
            }
        });

        final ViewHolder finalHolder = holder;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("1")) {

                        if ((DoctorRequestListActivity.deskArrayList.contains("OD") && DoctorRequestListActivity.deskArrayList.contains("ND")) || DoctorRequestListActivity.deskArrayList.size()==0)
                        {
                            selectDoctorDeskDialog(mDisplayedValues.get(i));
                        }
                        else if (DoctorRequestListActivity.deskArrayList.contains("OD"))
                        {
                            //go to new desk
                            Intent intent = new Intent(c, DeskHomeActivity.class);
                            intent.putExtra("patientdetails", (Serializable) (mDisplayedValues.get(i)));
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            c.startActivity(intent);
                        }
                        else if (DoctorRequestListActivity.deskArrayList.contains("ND"))
                        {
                            //go to old desk
                            Intent intent = new Intent(c, DeskActivity.class);
                            intent.putExtra("patientdetails", (Serializable) (mDisplayedValues.get(i)));
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            c.startActivity(intent);
                        }else
                        {
                            //go to old desk
                            Intent intent = new Intent(c, DeskActivity.class);
                            intent.putExtra("patientdetails", (Serializable) (mDisplayedValues.get(i)));
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            c.startActivity(intent);
                        }


                    }
                    if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("2")) {


                       /* Toast.makeText(c, "Please wait while downloading prescription.", Toast.LENGTH_SHORT).show();
                        view.setEnabled(false);
                        getPastPrescription(mDisplayedValues.get(i).getCRNo(), mDisplayedValues.get(i).getRequestID(), finalHolder.progressBar, view);
*/
                        Toast.makeText(c, "Please wait while downloading prescription.", Toast.LENGTH_SHORT).show();
                        view.setEnabled(false);
                        if (mDisplayedValues.get(i).getRequestStatusCompleteMode().equalsIgnoreCase("1")) {

                            getPastPrescription(mDisplayedValues.get(i).getCRNo(), mDisplayedValues.get(i).getRequestID(), finalHolder.progressBar, view);
                        } else {
                            getPastWebPrescription(mDisplayedValues.get(i), finalHolder.progressBar, view);

                            /*Intent intent = new Intent(c, ViewWebPrescriptionActivity.class);
                            intent.putExtra("details", mDisplayedValues.get(i));
                            intent.putExtra("user", "doctor");
                            c.startActivity(intent);*/
                        }


                    }
                    if (mDisplayedValues.get(i).getRequestStatus().equalsIgnoreCase("0")) {
                        if ((mDisplayedValues.get(i).getScrResponse() + mDisplayedValues.get(i).getPatWeight() + mDisplayedValues.get(i).getPatHeight() + mDisplayedValues.get(i).getPatMedication() + mDisplayedValues.get(i).getPatPastDiagnosis() + mDisplayedValues.get(i).getPatAllergies() + mDisplayedValues.get(i).getRmrks()).trim().equalsIgnoreCase("")) {
                            //Toast.makeText(c, "No information submitted.", Toast.LENGTH_SHORT).show();
                            tvTitle.setText("No information submitted.");
                            dialog.show();
                        } else {
                            showPatientDetails(dialog, mDisplayedValues.get(i));
                            dialog.show();
                        }
                    }
                } catch (Exception ex) {
                    Log.i("ex", "onClick: " + ex);
                }
            }
        });


        holder.tvViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getDoctorsMessages(mDisplayedValues.get(i).getDocMessage());


                getMessage(mDisplayedValues.get(i), "", "1");


            }
        });


        return view;
    }

    private void selectDoctorDeskDialog(DoctorReqListDetails doctorReqListDetails) {

        BottomSheetDialog dialog = new BottomSheetDialog(c, R.style.BottomSheetDialog);
        View contentView = ((Activity) c).getLayoutInflater().inflate(R.layout.select_doctor_desk_dialog, null);
        dialog.setContentView(contentView);

     //   FrameLayout bottomSheet = (FrameLayout) dialog.findViewById(R.id.design_bottom_sheet);
      //  BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);


        Button btnOldOpd = (Button) dialog.findViewById(R.id.btn_old_opd);
        Button btnNewOpd = (Button) dialog.findViewById(R.id.btn_new_opd);
        btnOldOpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, DeskActivity.class);

                intent.putExtra("patientdetails", (Serializable) doctorReqListDetails);

                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                c.startActivity(intent);
            }
        });
        btnNewOpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(c, DeskHomeActivity.class);
                intent.putExtra("patientdetails", (Serializable) doctorReqListDetails);

                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                c.startActivity(intent);
            }
        });
        dialog.show();

    }

    private void showMessageCount(ViewHolder holder, String docMessage) {
        try {
            Log.i(TAG, "showMessageCount: " + docMessage);
            String[] arMessage = docMessage.split("#");
            if (arMessage.length > 0) {
                holder.smsBadgeView.setBadgeValue(arMessage.length);
                holder.smsBadgeView.setVisibility(View.VISIBLE);
            } else {
                holder.smsBadgeView.setVisibility(View.GONE);
            }

            if (holder.tvViewMessage.getVisibility() == View.GONE) {
                holder.smsBadgeView.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void getMessage(DoctorReqListDetails doctorReqListDetails, String doctorMessage, String isView) {

        ArrayList<DoctorMessage> messageArrayList = new ArrayList<>();
        ManagingSharedData msd = new ManagingSharedData(c);

        Log.i(TAG, "getMessage: " + ServiceUrl.getUpdateDoctorMessage + "requestID=" + doctorReqListDetails.getRequestID() + "&sentBy=d&docMessage=" + doctorMessage + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&hospCode=" + msd.getHospCode() + "&isView=" + isView);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getUpdateDoctorMessage + "requestID=" + doctorReqListDetails.getRequestID() + "&sentBy=d&docMessage=" + doctorMessage + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&hospCode=" + msd.getHospCode() + "&isView=" + isView, new Response.Listener<String>() {
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
                                isMe = false;
                            } else {
                                isMe = true;
                            }
                            if (!message.isEmpty())
                                messageArrayList.add(new DoctorMessage(message, userName, timeStamp, isMe));


                        }
                        if (isView.equalsIgnoreCase("1")) {
                            showMessageDialog(doctorReqListDetails, messageArrayList);
                        }
                        if (isView.equalsIgnoreCase("0")) {
                            sendFCMPush("Message from " + msd.getUsername(), doctorMessage, doctorReqListDetails.getPatientToken());
                        }
                    } else {
                        Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    Log.i(TAG, "onResponse: " + ex);
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

    private void showMessageDialog(DoctorReqListDetails doctorReqListDetails, ArrayList<DoctorMessage> chatHistory) {
        DoctorMessageAdapter adapter;
//        final Dialog dialog = new Dialog(c);
        final BottomSheetDialog dialog = new BottomSheetDialog(c, R.style.bottomSheetStyleWrapperMessage);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.doctors_message_dialog);
       /* WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);*/


        ListView messagesContainer = (ListView) dialog.findViewById(R.id.messagesContainer);
        TextView btnDismiss = (TextView) dialog.findViewById(R.id.btn_dismiss);
        ImageButton btnSend = (ImageButton) dialog.findViewById(R.id.btn_send);
        EditText edtMessage = (EditText) dialog.findViewById(R.id.edt_message);


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
        adapter = new DoctorMessageAdapter(c, chatHistory);
        messagesContainer.setAdapter(adapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doctorMessage = edtMessage.getText().toString();
                if (doctorMessage.isEmpty()) {
                    Toast.makeText(c, "Please enter a message.", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(doctorReqListDetails, doctorMessage, "0", adapter);
                    dialog.dismiss();
                }
            }
        });


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (chatHistory.size() == 0) {
            Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
        } else {

            dialog.show();
        }
    }

    private void showPatientDetails(final Dialog dialog, DoctorReqListDetails patientDetails) {
        final TextView tvNameAgeGender = dialog.findViewById(R.id.tv_name_age_gender);
        final TextView tvWeight = dialog.findViewById(R.id.tv_weight);
        final TextView tvHeight = dialog.findViewById(R.id.tv_height);
        final TextView tvPatMedications = dialog.findViewById(R.id.tv_pat_medications);
        final TextView tvPastDiagonsis = dialog.findViewById(R.id.tv_past_diagonosis);
        final TextView tvAllergies = dialog.findViewById(R.id.tv_allergies);
        final TextView tvDescription = dialog.findViewById(R.id.tv_description);
        final TextView tvCovidScreeningResponse = dialog.findViewById(R.id.tv_covid_screening_response);


        tvNameAgeGender.setText(patientDetails.getPatName() + " (" + patientDetails.getPatGender() + "/" + patientDetails.getPatAge() + "/" + patientDetails.getPatMobileNo().substring(2) + ")");

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




        /*tvCovidScreeningResponse.setText(fever + cough + soreThroat + breathingDifficulty + foreignTravel);
        tvWeight.setText("Weight: " + patientDetails.getPatWeight());
        tvHeight.setText("Height: " + patientDetails.getPatHeight());
        tvPatMedications.setText("Medications: " + patientDetails.getPatMedication());
        tvPastDiagonsis.setText("Past Diagnosis: " + patientDetails.getPatPastDiagnosis());
        tvAllergies.setText("Allergies: " + patientDetails.getPatAllergies());
        tvDescription.setText("Problem Description: " + patientDetails.getRmrks());*/

        if (!patientDetails.getPatWeight().equalsIgnoreCase("")) {
            tvWeight.setText("Weight: " + patientDetails.getPatWeight());
            tvWeight.setVisibility(View.VISIBLE);
        }
        if (!patientDetails.getPatHeight().equalsIgnoreCase("")) {
            tvHeight.setText("Height: " + patientDetails.getPatHeight());
            tvHeight.setVisibility(View.VISIBLE);
        }

        if (!patientDetails.getPatMedication().equalsIgnoreCase("")) {
            tvPatMedications.setText("Medications: " + patientDetails.getPatMedication());
            tvPatMedications.setVisibility(View.VISIBLE);
        }
        if (!patientDetails.getPatPastDiagnosis().equalsIgnoreCase("")) {
            tvPastDiagonsis.setText("Past Diagnosis: " + patientDetails.getPatPastDiagnosis());
            tvPastDiagonsis.setVisibility(View.VISIBLE);
        }
        if (!patientDetails.getPatAllergies().equalsIgnoreCase("")) {
            tvAllergies.setText("Allergies: " + patientDetails.getPatAllergies());
            tvAllergies.setVisibility(View.VISIBLE);
        }
        if (!patientDetails.getRmrks().equalsIgnoreCase("")) {
            tvDescription.setText("Problem Description: " + patientDetails.getRmrks());
            tvDescription.setVisibility(View.VISIBLE);
        }
    }


   /* private void updateDoctorMessage(DoctorReqListDetails doctorReqListDetails, String doctorMessage) {
        ManagingSharedData msd = new ManagingSharedData(c);
        NukeSSLCerts.nuke();
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateDoctorMessage + doctorReqListDetails.getRequestID() + "&docMessage=" + doctorMessage + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&hospCode=" + msd.getHospCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaaa", "onResponse: " + response);

                sendFCMPush("Message from " + msd.getUsername(), doctorMessage, doctorReqListDetails.getPatientToken());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, "Unable to update status.Try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });
         MySingleton.getInstance(this).addToRequestQueue(request);
    }*/

    private void refreshActivity() {
        Intent intent = ((Activity) c).getIntent();
        ((Activity) c).finish();
        c.startActivity(intent);
    }


    private void updateRequestStatus(final String requestId, String requestStatus, final String docMessage, String doctorToken, DoctorReqListDetails doctorRequestListDetails, String consultationTimeStamp) {
        ManagingSharedData msd = new ManagingSharedData(c);
        //     NukeSSLCerts.nuke(c);
//        RequestQueue requestQueue = Volley.newRequestQueue(c);


        Log.i(TAG, "updateRequestStatus: " + ServiceUrl.updateRequestStatus + msd.getHospCode() + "&requestID=" + requestId + "&reqStatus=" + requestStatus + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&consltMobNo=" + msd.getPatientDetails().getMobileNo() + "&docMessage=" + docMessage + "&doctorToken=" + doctorToken);

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.updateRequestStatus + msd.getHospCode() + "&requestID=" + requestId + "&reqStatus=" + requestStatus + "&consltID=" + msd.getEmployeeCode() + "&consltName=" + msd.getUsername() + "&consltMobNo=" + msd.getPatientDetails().getMobileNo() + "&docMessage=" + docMessage + "&doctorToken=" + doctorToken + "&consultationTimeStamp=" + consultationTimeStamp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("bbbb", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("Status").equalsIgnoreCase("1")) {
                        //updateDoctorMessage(doctorRequestListDetails, docMessage);

                        if (!docMessage.isEmpty()) {
                            getMessage(doctorRequestListDetails, docMessage, "0");
                        }

                        if (requestStatus.equalsIgnoreCase("1")) {
                            //generateEpisode
                            generateEpisode(doctorRequestListDetails.getRequestID(), msd.getHospCode(), msd.getEmployeeCode(), doctorRequestListDetails);

                        }

                        //notification for rejected request
                        else if (requestStatus.equalsIgnoreCase("4")) {
                            sendFCMPush("eConsultation Request Rejected", "Your eConsultation request for " + doctorRequestListDetails.getDepartmentName() + " at " + doctorRequestListDetails.getHospitalName() + " has been rejected by " + msd.getUsername() + ". Please check \"Consultation & Status page\" for doctor's message.", doctorRequestListDetails.getPatientToken());
                            refreshActivity();
                        }
                    } else {
                        Toast.makeText(c, "Could not update request status.Please refresh your page.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, "Unable to update status.Try again after sometime.", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(c).addToRequestQueue(request);

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<DoctorReqListDetails>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<DoctorReqListDetails> filteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(mDisplayedValues); // saves the original data in mOriginalValues
                }


                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getPatName();
                        if (data.toLowerCase().contains(constraint.toString())) {

                            filteredArrList.add(new DoctorReqListDetails(mOriginalValues.get(i).getRequestID(), mOriginalValues.get(i).getCRNo(), mOriginalValues.get(i).getScrResponse(), mOriginalValues.get(i).getConsName(), mOriginalValues.get(i).getDeptUnitCode(), mOriginalValues.get(i).getDeptUnitName(), mOriginalValues.get(i).getHospCode(), mOriginalValues.get(i).getRequestStatus(), mOriginalValues.get(i).getPatMobileNo(), mOriginalValues.get(i).getConsMobileNo(), mOriginalValues.get(i).getPatDocs(), mOriginalValues.get(i).getDocMessage(), mOriginalValues.get(i).getCnsltntId(), mOriginalValues.get(i).getPatName(), mOriginalValues.get(i).getPatAge(), mOriginalValues.get(i).getPatGender(), mOriginalValues.get(i).getRmrks(), mOriginalValues.get(i).getEmail(), mOriginalValues.get(i).getpVisitdate(), mOriginalValues.get(i).getPatWeight(), mOriginalValues.get(i).getPatHeight(), mOriginalValues.get(i).getPatMedication(), mOriginalValues.get(i).getPatPastDiagnosis(), mOriginalValues.get(i).getPatAllergies(), mOriginalValues.get(i).getDepartmentId(), mOriginalValues.get(i).getDepartmentName(), mOriginalValues.get(i).getIsDocUploaded(), mOriginalValues.get(i).getPatientToken(), mOriginalValues.get(i).getDoctorToken(), mOriginalValues.get(i).getAppointmentNo(), mOriginalValues.get(i).getApptStartTime(), mOriginalValues.get(i).getApptEndTime(), mOriginalValues.get(i).getApptDate(), mOriginalValues.get(i).getHospitalName(), mOriginalValues.get(i).getIsEpisodeExist(), mOriginalValues.get(i).getEpisodeCode(), mOriginalValues.get(i).getEpisodeVisitNo(), mOriginalValues.get(i).getRequestStatusCompleteDate(), mOriginalValues.get(i).getRequestStatusCompleteMode()));
                        }
                    }
                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }
        };
        return filter;
    }


    public void getPastPrescription(final String uhid, final String requestId, final ProgressBar progressBar, final View view) {
        progressBar.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(c);
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
        Log.i(TAG, "getPastPrescription: " + ServiceUrl.getPastPrescription + msd.getHospCode() + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1");
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastPrescription + msd.getHospCode() + "&uhid=" + uhid + "&requestId=" + requestId + "&deptcontextvisit=1", response -> {

            Log.i("response is ", "onResponse: " + response);
            try {

                if (response.length() == 0) {
                    Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
                } else {
//                    saveBase64Pdf(response);
                    saveBase64Pdf(c, "prescription", "prescription", response);
                }

                progressBar.setVisibility(View.GONE);
                view.setEnabled(true);

            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                view.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            Toast.makeText(c, "FTP server is down , pls try after sometime", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            view.setEnabled(true);
            AppUtilityFunctions.handleExceptions(error, c);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(c).addToRequestQueue(request);


    }

    private void getPastWebPrescription(DoctorReqListDetails doctorReqListDetails, ProgressBar progressBar, final View view) {
        progressBar.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(c);
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPastWebPrescription + "hosp_code=" + doctorReqListDetails.getHcode() + "&Modval=5&CrNo=" + doctorReqListDetails.getCRNo() + "&episodeCode=" + doctorReqListDetails.getEpisodeCode() + "&visitNo=" + doctorReqListDetails.getPatVisitNo() + "&seatId=0&Entrydate=%22%22", response -> {

            Log.i("response is ", "onResponse: " + response);
            try {

                if (response.length() == 0) {
                    Toast.makeText(c, "FTP server is down , pls try after sometime.", Toast.LENGTH_SHORT).show();
                } else {
//                    saveBase64Pdf(response);
                    saveBase64Pdf(c, "prescription", "prescription", response);
                }

                progressBar.setVisibility(View.GONE);
                view.setEnabled(true);

            } catch (final Exception e) {
                Log.i("jsonexception", "onResponse: " + e);
                view.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }


        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            Toast.makeText(c, "FTP server is down , pls try after sometime", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            view.setEnabled(true);
            AppUtilityFunctions.handleExceptions(error, c);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(c).addToRequestQueue(request);

    }

   /* private void saveBase64Pdf(String data) {
        try {
            String path = c.getFilesDir() + "/prescription.pdf";
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


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    private void sendFCMPush(String title, String message, String patientToken) {

        String token = patientToken;

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


    private void generateEpisode(String requestId, String hospCode, String employeeCode, DoctorReqListDetails doctorRequestListDetails) {
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.generateEpisode + requestId + "&hospCode=" + hospCode + "&consltID=" + employeeCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "generateEpisoderesponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        ManagingSharedData msd = new ManagingSharedData(c);
                        // for accepted request
                        sendFCMPush("eConsultation Request Approved", "Your eConsultation request for " + doctorRequestListDetails.getDepartmentName() + " has been approved by " + msd.getUsername() + ". The doctor will call you soon, please visit “Consultation and Status” page and wait for join video call link to be shown.", doctorRequestListDetails.getPatientToken());

                        refreshActivity();
                    } else {
                        Toast.makeText(c, "Unable to generate episode code", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "generateEpisodeerror" + error);
            }
        });
        MySingleton.getInstance(c).addToRequestQueue(request);
    }


    private void sendMessage(DoctorReqListDetails patientRequestDetails, String message, String isView, DoctorMessageAdapter adapter) {

        ArrayList<DoctorMessage> messageArrayList = new ArrayList<>();
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
        ManagingSharedData msd = new ManagingSharedData(c);

        Log.i(TAG, "getMessage: " + ServiceUrl.getUpdateDoctorMessage + "requestID=" + patientRequestDetails.getRequestID() + "&sentBy=d&docMessage=" + message + "&consltID=&consltName=" + patientRequestDetails.getPatName() + "&hospCode=" + patientRequestDetails.getHospCode() + "&isView=" + isView);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getUpdateDoctorMessage + "requestID=" + patientRequestDetails.getRequestID() + "&sentBy=d&docMessage=" + message + "&consltID=&consltName=" + patientRequestDetails.getPatName() + "&hospCode=" + patientRequestDetails.getHospCode() + "&isView=" + isView, new Response.Listener<String>() {
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
                            if (sentBy.equalsIgnoreCase("d")) {
                                isMe = true;
                            } else {
                                isMe = false;
                            }
                            messageArrayList.add(new DoctorMessage(message, userName, timeStamp, isMe));


                        }
                        if (isView.equalsIgnoreCase("0")) {
                            adapter.notifyDataSetChanged();
                            sendFCMPush("Message From " + patientRequestDetails.getPatName(), message, patientRequestDetails.getDoctorToken());
                        }

                    } else {
                        Toast.makeText(c, "No messages found.", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    Toast.makeText(c, "Something went wrong.", Toast.LENGTH_SHORT).show();
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