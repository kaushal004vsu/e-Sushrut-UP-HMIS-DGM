package com.cdac.uphmis.covid19;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdac.uphmis.R;
import com.cdac.uphmis.covid19.adapter.DateAdapter;
import com.cdac.uphmis.covid19.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.covid19.model.DateDetails;
import com.cdac.uphmis.covid19.model.ScreeningDetails;
import com.cdac.uphmis.covid19.model.ShiftDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NetworkStats;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

public class SlotSelectionActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    RecyclerView rvDate;
    TextView tvSelectedDate;


    ArrayList<DateDetails> arrayListDates;
    private String deptCode;


    ScreeningDetails screeningDetails;


    GeometricProgressView progressView;
    private TextView tvAvailableSlots;
    public Button btnConfirm;

    private LinearLayout llData;

    private TextView tvSlotMessage;

    ArrayList<ShiftDetails> shiftDetailsArrayList;

    private TextView tvSlotDescription;
    private ImageView thumbImage;


    private List<Calendar> datesArrayList;
    private ArrayList<String> holidayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_selection);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //NukeSSLCerts.nuke(this);
       // requestQueue = Volley.newRequestQueue(this);

        getHolidayDates();
        btnConfirm = findViewById(R.id.btn_confirm);
        llData = findViewById(R.id.ll_data);
        tvSlotMessage = findViewById(R.id.tv_slot_message);
//        tvSlotMessage.setText(Html.fromHtml(getResources().getString(R.string.tv_slot_message)));     old default hardcoded opd timings

        progressView = (GeometricProgressView) findViewById(R.id.progress_view);

        shiftDetailsArrayList = new ArrayList<>();
        // To retrieve object in second Activity
        screeningDetails = (ScreeningDetails) getIntent().getSerializableExtra("PatientDetails");

        tvAvailableSlots = findViewById(R.id.tv_available_slots);
        tvSlotDescription = findViewById(R.id.tv_slot_description);
        tvSelectedDate = findViewById(R.id.tv_selected_date);

        thumbImage = findViewById(R.id.img_thumbs);
        rvDate = findViewById(R.id.rv_date);
        rvDate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDate.setItemAnimator(new DefaultItemAnimator());


        deptCode = screeningDetails.getDeptUnitCode();
        rvDate.addOnItemTouchListener(new MyRecycleViewClickListener(this, new MyRecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DateDetails dateDetails = (DateDetails) arrayListDates.get(position);
                tvSelectedDate.setText(dateDetails.getDisplayDate());
                getShiftName(dateDetails.getDate(), deptCode);
            }
        }));

        Log.i("slotactivity", "onCreate: " + screeningDetails.getDistrictCode());

        //first time load datesArrayList and set to horizontal reclyclerview
//        getDates(0);

        //  getTodaysDate();
        try {
            tvSlotMessage.setText(Html.fromHtml(screeningDetails.getOPDTimings()));
            tvSlotMessage.setVisibility(View.GONE);
        }catch(Exception ex){ex.printStackTrace();}

    }

    private void getDates(int row_index) {
        arrayListDates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Log.i("days", "onCreate: ");
            DateDetails dateDetails = getCalculatedDate("dd-MMM-yyyy", i);
            if ((!dateDetails.getDisplayDate().equalsIgnoreCase(""))) {

                if (arrayListDates.size() == 0) {
                    tvSelectedDate.setText(dateDetails.getDisplayDate());
                    getShiftName(dateDetails.getDate(), deptCode);
                }

                if (holidayList.contains(dateDetails.getDate())) {
                    Log.i("holiday", "getDates: " + dateDetails.getDate());
                } else {
                    arrayListDates.add(dateDetails);
                }

            }
        }
        DateAdapter adapter = new DateAdapter(getApplicationContext(), arrayListDates, row_index);
        rvDate.setAdapter(adapter);
    }

    public void getShiftName(String date, String deptCode) {
        progressView.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(this);
        Log.i("date", "getShiftName: " + date + "," + deptCode);
//        gridView.setVisibility(View.GONE);
        shiftDetailsArrayList = new ArrayList<>();
        llData.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getSlots + screeningDetails.getHospCode() + "&aptDate=" + date + "&deptUnitCode=" + deptCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //shiftDetails = response;
                progressView.setVisibility(View.GONE);

                Log.i("aaaa", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotList = jsonObject.getJSONArray("slot_list");

                    if (slotList.length() == 0) {
                        if (this != null) {
                            btnConfirm.setVisibility(View.GONE);
                            tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                            tvSlotDescription.setText(getString(R.string.slot_not_available));
                            thumbImage.setBackgroundResource(R.drawable.thumbs_down);

                        }
                    } else {
                        thumbImage.setBackgroundResource(R.drawable.slots_available);
                        btnConfirm.setVisibility(View.VISIBLE);
                        tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                        tvSlotDescription.setText(""+slotList.length()+" " + getString(R.string.slots_available));

                        for (int i = 0; i < slotList.length(); i++) {
                            JSONObject c = slotList.getJSONObject(i);
                            String date = c.getString("DT");
                            String shift = c.getString("SHIFT");
                            String shiftName = c.getString("SHIFTNAME");
                            String shiftst = c.getString("SHIFTST");
                            String shiftet = c.getString("SHIFTET");
                            String slotst = c.getString("SLOTST");
                            String slotet = c.getString("SLOTET");
                            String freeslotdetail = c.getString("FREESLOTDETAIL");
                            String slotstatus = c.getString("SLOTSTATUS");
                            String opdslots = c.getString("OPDSLOTS");
                            String ipdslots = c.getString("IPDSLOTS");
                            String opdbookedslots = c.getString("OPDBOOKEDSLOTS");
                            String ipdbookedslots = c.getString("IPDBOOKEDSLOTS");
                            shiftDetailsArrayList.add(new ShiftDetails(date, shift, shiftName, shiftst, shiftet, slotst, slotet, freeslotdetail, slotstatus, opdslots, ipdslots, opdbookedslots, ipdbookedslots));

                        }


                    }

                    llData.setVisibility(View.VISIBLE);

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void btnDate(View view) {


        if (datesArrayList != null) {
            showDate(datesArrayList.toArray(new Calendar[datesArrayList.size()]));
        } else {
            getHolidayDates();
        }

      /*  int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        String dateString = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
                        Log.i("date", "onDateSet: " + dateString);


                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = null;
                        try {
                            date = (Date) formatter.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        String strDate = newFormat.format(date);
                        Log.i("final string", "onDateSet: " + strDate);
                        DateDetails dateDetails;
                        String displayDate = null;
                        try {
                            SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                            Date date1 = format1.parse(strDate);
                            SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                            displayDate = format2.format(date1);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dateDetails = new DateDetails(strDate, displayDate, "");
                        tvSelectedDate.setText(dateDetails.getDisplayDate());
                        getShiftName(strDate, deptCode);
                        getDates(-1);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();*/
    }


   /* public void getTodaysDate() {
        DateDetails dateDetails = getCalculatedDate("dd-MMM-yyyy", 0);
        tvSelectedDate.setText(dateDetails.getDisplayDate());
        getShiftName(dateDetails.getDate(), deptCode);

    }*/


    public DateDetails getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);

        cal.add(Calendar.DAY_OF_YEAR, days);

        String date = s.format(new Date(cal.getTimeInMillis()));


        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
        String displayDate = "";
        try {
            if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                Date date1 = format1.parse(date);

                if (days == 0) {
                    SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");
                    displayDate = "Today, " + format2.format(date1);

                } else if (days == 1) {
                    SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");
                    displayDate = "Tomorrow, " + format2.format(date1);

                } else {
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = format2.format(date1);

                    System.out.println(format2.format(date1));

                }

                System.out.println("weekday " + displayDate);
//                if (isHoliday(days)) {
//                    return new DateDetails(date, "", "");
//                } else {
                return new DateDetails(date, displayDate, "");
//                }

            } else {
                System.out.println("weekend " + displayDate);
                return new DateDetails(date, displayDate, "");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return new DateDetails(date, displayDate, "");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);
        MenuItem item=menu.findItem(R.id.action_network_stats);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N)
        {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(SlotSelectionActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
            case R.id.action_network_stats:
                if (checkSelfPermission(READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please give phone state permission.", Toast.LENGTH_SHORT).show();
                }else {
                    NetworkStats.appUpdateDialog(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void btnConfirm(View view) {
        if (shiftDetailsArrayList.size() != 0) {
//TODO teleconsultation OPD hours check

//            if (checkOpdHours("17:00", "23:59", shiftDetailsArrayList.get(0).getSlotst())) {
            if (shiftDetailsArrayList.get(0).getDate()==null||shiftDetailsArrayList.get(0).getDate().isEmpty())
            {
                Toast.makeText(this, "Appointment date cannot be blank.", Toast.LENGTH_SHORT).show();
                return;
            }
                bookAppointment(screeningDetails.getRequestId(), screeningDetails.getCrno(), screeningDetails.getScrResponse(), screeningDetails.getConsName(), screeningDetails.getDeptUnitCode(), screeningDetails.getDeptUnitName(), screeningDetails.getRequestStatus(), screeningDetails.getPatMobileNo(), screeningDetails.getConsMobileNo(), screeningDetails.getPatDocs(), screeningDetails.getDocMessage(), screeningDetails.getConstId(), screeningDetails.getPatName(), screeningDetails.getPatAge(), screeningDetails.getPatGender(), screeningDetails.getEmail(), screeningDetails.getRemarks(), screeningDetails.getPatWeight(), screeningDetails.getPatHeight(), screeningDetails.getMedications(), screeningDetails.getPastdiagnosis(), screeningDetails.getPastAllergies(), screeningDetails.getUserId(), screeningDetails.getStateCode(), screeningDetails.getDistrictCode(), screeningDetails.getApptDeptUnit(), screeningDetails.getGuardianName(), screeningDetails.getPatientToken(), shiftDetailsArrayList.get(0).getDate(), shiftDetailsArrayList.get(0).getSlotst(), shiftDetailsArrayList.get(0).getSlotet(), shiftDetailsArrayList.get(0).getShift());
//            } else {
//                new AlertDialog.Builder(SlotSelectionActivity.this)
//                        .setTitle("Information")
//                        .setMessage("You are raising request outside OPD hours. Please note that request will be attended only during OPD hours, please choose date accordingly.")
//                        .setPositiveButton("OK", null)
//                        .show();
//
//            }


        } else {
            Toast.makeText(this, "No slots available for selected date.Please select another date.", Toast.LENGTH_SHORT).show();
        }
    }


    private void bookAppointment(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String ptientToken, String apptDate, String slotst, String slotet, String shiftId) {
        try {

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

                            String appointmentDetails = apptDate;

                            documentUploadDialog(finalRequestId, crno, appointmentDetails);
                        } else {
                            new AlertDialog.Builder(SlotSelectionActivity.this)
                                    .setTitle("Cannot Raise Request")
                                    .setMessage(s)
                                    .setNegativeButton("Ok", null)
                                    .show();


                        }

                    } catch (Exception ex) {
                        Log.i("jsonexception", "onResponse: " + ex);

                        Toast.makeText(SlotSelectionActivity.this, "Sorry! unable to raise your request.try after sometime.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("bookappintment", "onResponse: " + error);
                    AppUtilityFunctions.handleExceptions(error, SlotSelectionActivity.this);
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


             MySingleton.getInstance(this).addToRequestQueue(request);
        } catch (Exception ex) {
            Log.i("jsonexception", "makeRequest: ");

//            SlotSelectionActivity.btnConfirm.setEnabled(true);
        }
    }

    private String requestBodyJson(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String patientToken, String apptDate, String slotst, String slotet, String shiftId) {
        ManagingSharedData msd = new ManagingSharedData(this);
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


    private void documentUploadDialog(final String finalRequestId, String crno, String appointmentDetails) {
        final Dialog dialog = new Dialog(this);
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
                Intent intent = new Intent(SlotSelectionActivity.this, AppointmentSuccefullActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                startActivity(intent);
                finish();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(SlotSelectionActivity.this, DocumentUploadActivity.class);
                Intent intent = new Intent(SlotSelectionActivity.this, DocumentUpldNewActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private boolean checkOpdHours(String openTime, String CloseTime, String slotTime) {
        try {
            Log.i("slotTime", "checkOpdHours: " + slotTime);
            Date time1 = new SimpleDateFormat("HH:mm").parse(openTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm").parse(CloseTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

//            String someRandomTime = "01:00:00";
            Date d = new SimpleDateFormat("HH:mm").parse(slotTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();

            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                Log.i("showAlert", "checkOpdHours: ");
                return false;

            } else {
                Log.i("dontShowAlert", "checkOpdHours: " + x);

                return true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

        String dateString = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
        Log.i("date", "onDateSet: " + dateString);


        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate = newFormat.format(date);
        Log.i("final string", "onDateSet: " + strDate);
        DateDetails dateDetails;
        String displayDate = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 = format1.parse(strDate);
            SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
            displayDate = format2.format(date1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dateDetails = new DateDetails(strDate, displayDate, "");
        Log.i("getDisplayDate", "onDateSet: " + dateDetails.getDisplayDate());
        tvSelectedDate.setText(dateDetails.getDisplayDate());
        getShiftName(strDate, deptCode);

        DateAdapter adapter = new DateAdapter(getApplicationContext(), arrayListDates, -1);
        rvDate.setAdapter(adapter);
//        getDates(-1);
    }

    private void getHolidayDates() {

        Toast.makeText(this, "Please wait while loading calendar.", Toast.LENGTH_SHORT).show();
        ManagingSharedData msd = new ManagingSharedData(this);
        datesArrayList = new ArrayList<>();
        holidayList = new ArrayList<>();

        Log.d("hliday",ServiceUrl.getHolidayList + msd.getHospCode());
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHolidayList + msd.getHospCode(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (status.equalsIgnoreCase("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                String holidayDate = c.getString("VARHOLIDAYDATE");
                            //    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                              //  SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

//                            holidayDate = holidayDate.replace("2019", "2020");

                             //   Date date = format1.parse(holidayDate);
                             //   System.out.println(format2.format(date));

                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(holidayDate);
                                Calendar calendar = dateToCalendar(date);
                                datesArrayList.add(calendar);

                                SimpleDateFormat format3 = new SimpleDateFormat("dd-MMM-yyyy");
                                holidayList.add(format3.format(date));
                            }

                            getDates(0);

                        }else
                        {
                            getDates(0);
                        }


                } catch (Exception ex) {

                    ex.printStackTrace();
                    Toast.makeText(SlotSelectionActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                finish();
                Toast.makeText(SlotSelectionActivity.this, "Error Loading Calendar.", Toast.LENGTH_SHORT).show();
            }
        });

         MySingleton.getInstance(this).addToRequestQueue(request);


    }

    private void showDate(Calendar[] disabledDates) {

        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Hour = calendar.get(Calendar.HOUR_OF_DAY);
        int Minute = calendar.get(Calendar.MINUTE);
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                SlotSelectionActivity.this,
                Year, // Initial year selection
                Month, // Initial month selection
                Day // Inital day selection
        );
        datePickerDialog.setMinDate(calendar);


        // Setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        datePickerDialog.setMinDate(min_date_c);


        // Setting Max Date to next 2 years
        Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.YEAR, Year + 2);
        datePickerDialog.setMaxDate(max_date_c);

        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                Calendar[] disabledDays = new Calendar[1];
                disabledDays[0] = loopdate;
//                Log.i("aa", "showDate: " + disabledDates[0]);
                datePickerDialog.setDisabledDays(disabledDays);
            }
        }

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        datePickerDialog.setDisabledDays(disabledDates);
        datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    private boolean isHoliday(int j) {
        for (int i = 0; i < arrayListDates.size(); i++) {
            if (holidayList.contains(arrayListDates.get(i).getDate())) {
                Log.i("displayDate", "btnDate: " + arrayListDates.get(i).getDisplayDate());
                Log.i("date", "btnDate: " + arrayListDates.get(i).getDate());
                Log.i("holidayDate", "btnDate: " + holidayList.get(i));

                return true;
            }
        }
        return false;
    }
}

/*
public class SlotSelectionActivity extends AppCompatActivity {
    RecyclerView rvDate;
    TextView tvSelectedDate;


    ArrayList<DateDetails> arrayListDates;
    private String deptCode;


    ScreeningDetails screeningDetails;


    GeometricProgressView progressView;
    private TextView tvAvailableSlots;

    private Button btnConfirm;

    private LinearLayout llData;

    ArrayList<ShiftDetails> shiftDetailsArrayList;

    private TextView tvSlotDescription;
    private ImageView thumbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_selection);
        Toolbar toolbar =findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        btnConfirm = findViewById(R.id.btn_confirm);
        llData = findViewById(R.id.ll_data);
        TextView tvSlotMessage = findViewById(R.id.tv_slot_message);
        tvSlotMessage.setText(Html.fromHtml(getResources().getString(R.string.tv_slot_message)));
        progressView =  findViewById(R.id.progress_view);

        shiftDetailsArrayList = new ArrayList<>();
        // To retrieve object in second Activity
        screeningDetails = (ScreeningDetails) getIntent().getSerializableExtra("PatientDetails");

        tvAvailableSlots = findViewById(R.id.tv_available_slots);
        tvSlotDescription = findViewById(R.id.tv_slot_description);
        tvSelectedDate = findViewById(R.id.tv_selected_date);

        thumbImage = findViewById(R.id.img_thumbs);

        rvDate = findViewById(R.id.rv_date);
        rvDate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDate.setItemAnimator(new DefaultItemAnimator());



        deptCode = screeningDetails.getDeptUnitCode();
        rvDate.addOnItemTouchListener(new MyRecycleViewClickListener(this, (view, position) -> {
            DateDetails dateDetails = arrayListDates.get(position);
            tvSelectedDate.setText(dateDetails.getDisplayDate());
            getShiftName(dateDetails.getDate(), deptCode);
        }));

        Timber.i("onCreate: %s", screeningDetails.getDistrictCode());
        getDates(0);

        getTodaysDate();

    }

    private void getDates(int row_index) {

        arrayListDates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayListDates.add((getCalculatedDate("dd-MMM-yyyy", i)));
        }
        DateAdapter adapter = new DateAdapter(getApplicationContext(), arrayListDates, row_index);
        rvDate.setAdapter(adapter);


    }


    public void getShiftName(String date, String deptCode) {
        progressView.setVisibility(View.VISIBLE);
        Timber.i("getShiftName: " + date + "," + deptCode);

        shiftDetailsArrayList = new ArrayList<>();
        llData.setVisibility(View.GONE);
        NukeSSLCerts.nuke();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getSlots + screeningDetails.getHospCode() + "&aptDate=" + date + "&deptUnitCode=" + deptCode, response -> {
            //shiftDetails = response;
            progressView.setVisibility(View.GONE);

            Timber.i("onResponse: %s", response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray slotList = jsonObject.getJSONArray("slot_list");

                if (slotList.length() == 0) {
                        btnConfirm.setVisibility(View.GONE);
                        tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                        tvSlotDescription.setText("No slot is available for the selected date, Kindly select another date.");
                        thumbImage.setBackgroundResource(R.drawable.thumbs_down);


                } else {
                    thumbImage.setBackgroundResource(R.drawable.slots_available);
                    btnConfirm.setVisibility(View.VISIBLE);
                    tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                    tvSlotDescription.setText(slotList.length() + " Slots Available, Please press confirm to raise appointment request.");
                    for (int i = 0; i < slotList.length(); i++) {
                        JSONObject c = slotList.getJSONObject(i);
                        String date1 = c.getString("DT");
                        String shift = c.getString("SHIFT");
                        String shiftName = c.getString("SHIFTNAME");
                        String shiftst = c.getString("SHIFTST");
                        String shiftet = c.getString("SHIFTET");
                        String slotst = c.getString("SLOTST");
                        String slotet = c.getString("SLOTET");
                        String freeslotdetail = c.getString("FREESLOTDETAIL");
                        String slotstatus = c.getString("SLOTSTATUS");
                        String opdslots = c.getString("OPDSLOTS");
                        String ipdslots = c.getString("IPDSLOTS");
                        String opdbookedslots = c.getString("OPDBOOKEDSLOTS");
                        String ipdbookedslots = c.getString("IPDBOOKEDSLOTS");
                        shiftDetailsArrayList.add(new ShiftDetails(date1, shift, shiftName, shiftst, shiftet, slotst, slotet, freeslotdetail, slotstatus, opdslots, ipdslots, opdbookedslots, ipdbookedslots));

                    }


                }

                llData.setVisibility(View.VISIBLE);

            } catch (final JSONException e) {
                progressView.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
            }


        }, error -> {
            Timber.i("onErrorResponse: %s", error);
            progressView.setVisibility(View.GONE);
            llData.setVisibility(View.GONE);

        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void btnDate(View view) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {


                    String dateString = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
//                    Log.i("date", "onDateSet: " + dateString);


                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    String strDate = newFormat.format(date);
//                    Log.i("final string", "onDateSet: " + strDate);
                    DateDetails dateDetails;
                    String displayDate = null;
                    try {
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                        Date date1 = format1.parse(strDate);
                        SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                        displayDate = format2.format(date1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    dateDetails = new DateDetails(strDate, displayDate, "");
                    tvSelectedDate.setText(dateDetails.getDisplayDate());
                    getShiftName(strDate, deptCode);
                    getDates(-1);

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }


    public void getTodaysDate() {
        DateDetails dateDetails = getCalculatedDate("dd-MMM-yyyy", 0);
        tvSelectedDate.setText(dateDetails.getDisplayDate());
        getShiftName(dateDetails.getDate(), deptCode);

    }


    public DateDetails getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);

        String date = s.format(new Date(cal.getTimeInMillis()));

//        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
//        String displayDate=dayFormat.format(new Date(cal.getTimeInMillis()));

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
        String displayDate = "";
        try {
            Date date1 = format1.parse(date);

            if (days == 0) {


                SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");
                displayDate = "Today, " + format2.format(date1);

            } else if (days == 1) {


                SimpleDateFormat format2 = new SimpleDateFormat("dd MMM");
                displayDate = "Tomorrow, " + format2.format(date1);

            } else {


                SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                displayDate = format2.format(date1);

                System.out.println(format2.format(date1));

            }

            return new DateDetails(date, displayDate, "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new DateDetails(date, displayDate, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(SlotSelectionActivity.this);
                return true;
            case R.id.action_help:
                startActivity(new Intent(SlotSelectionActivity.this, HelpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void btnConfirm(View view) {
        if (shiftDetailsArrayList.size() != 0) {

            if (checkOpdHours("17:00", "23:59", shiftDetailsArrayList.get(0).getSlotst())) {
                bookAppointment(screeningDetails.getRequestId(), screeningDetails.getCrno(), screeningDetails.getScrResponse(), screeningDetails.getConsName(), screeningDetails.getDeptUnitCode(), screeningDetails.getDeptUnitName(), screeningDetails.getRequestStatus(), screeningDetails.getPatMobileNo(), screeningDetails.getConsMobileNo(), screeningDetails.getPatDocs(), screeningDetails.getDocMessage(), screeningDetails.getConstId(), screeningDetails.getPatName(), screeningDetails.getPatAge(), screeningDetails.getPatGender(), screeningDetails.getEmail(), screeningDetails.getRemarks(), screeningDetails.getPatWeight(), screeningDetails.getPatHeight(), screeningDetails.getMedications(), screeningDetails.getPastdiagnosis(), screeningDetails.getPastAllergies(), screeningDetails.getUserId(), screeningDetails.getStateCode(), screeningDetails.getDistrictCode(), screeningDetails.getApptDeptUnit(), screeningDetails.getGuardianName(), screeningDetails.getPatientToken(), shiftDetailsArrayList.get(0).getDate(), shiftDetailsArrayList.get(0).getSlotst(), shiftDetailsArrayList.get(0).getSlotet(), shiftDetailsArrayList.get(0).getShift());
            } else {
                new AlertDialog.Builder(SlotSelectionActivity.this)
                        .setTitle("Information")
                        .setMessage("You are raising request outside OPD hours. Please note that request will be attended only during OPD hours, please choose date accordingly.")
                        .setPositiveButton("OK", null)
                        .show();

            }


        } else {
            Toast.makeText(this, "No slots_available for selected date.Please select another date.", Toast.LENGTH_SHORT).show();
        }
    }


    private void bookAppointment(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String ptientToken, String apptDate, String slotst, String slotet, String shiftId) {
        try {
            NukeSSLCerts.nuke();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String url = ServiceUrl.bookAppointment;
            Timber.i("makeRequest: " + url);

            final String requestBody = requestBodyJson(requestId, crno, scrResponse, consName, deptUnitCode, deptUnitName, requestStatus, patMobileNo, consMobileNo, patDocs, docMessage, constId, patName, patAge, patGender, email, remarks, patWeight, patHeight, medications, pastdiagnosis, pastAllergies, userId, stateCode, districtCode, apptDeptUnitCode, guardianName, ptientToken, apptDate, slotst, slotet, shiftId);

            Timber.i("bookAppointment: " + requestBody);
            StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

                Timber.i("onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("Status");
                    String s = jsonObject.getString("Message");
                    String crno1 = jsonObject.getString("CrNo");
                    if (status) {
                        String finalRequestId = s.substring(s.indexOf("no") + 3, s.indexOf("raised") - 1);

                        String appointmentDetails = apptDate;

                        documentUploadDialog(finalRequestId, crno1, appointmentDetails);
                    } else {
                        new AlertDialog.Builder(SlotSelectionActivity.this)
                                .setTitle("Cannot Raise Request")
                                .setMessage(s)
                                .setNegativeButton("Ok", null)
                                .show();


                    }

                } catch (Exception ex) {
                    Log.i("jsonexception", "onResponse: " + ex);

                    Toast.makeText(SlotSelectionActivity.this, "Sorry! unable to raise your request.try after sometime.", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                Log.i("bookappintment", "onResponse: " + error);
                AppUtilityFunctions.handleExceptions(error, SlotSelectionActivity.this);
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cdac", "");


                    return params;
                }

            };


             MySingleton.getInstance(this).addToRequestQueue(request);
        } catch (Exception ex) {
            Timber.i("makeRequest: %s", ex);

        }
    }

    private String requestBodyJson(final String requestId, final String crno, String scrResponse, String consName, String deptUnitCode, String deptUnitName, String requestStatus, String patMobileNo, String consMobileNo, String patDocs, String docMessage, String constId, String patName, String patAge, String patGender, String email, String remarks, String patWeight, String patHeight, String medications, String pastdiagnosis, String pastAllergies, String userId, String stateCode, String districtCode, String apptDeptUnitCode, String guardianName, String patientToken, String apptDate, String slotst, String slotet, String shiftId) {
        ManagingSharedData msd = new ManagingSharedData(this);
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


    private void documentUploadDialog(final String finalRequestId, String crno, String appointmentDetails) {
        final Dialog dialog = new Dialog(this);
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
                Intent intent = new Intent(SlotSelectionActivity.this, AppointmentSuccefullActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                startActivity(intent);
                finish();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SlotSelectionActivity.this, DocumentUploadActivity.class);
                intent.putExtra("requestId", finalRequestId);
                intent.putExtra("crno", crno);
                intent.putExtra("screeningDetails", screeningDetails);
                intent.putExtra("apptDetails", appointmentDetails);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    private boolean checkOpdHours(String openTime, String CloseTime, String slotTime) {
        try {
            Timber.i("checkOpdHours: " + slotTime);
            Date time1 = new SimpleDateFormat("HH:mm").parse(openTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm").parse(CloseTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

//            String someRandomTime = "01:00:00";
            Date d = new SimpleDateFormat("HH:mm").parse(slotTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();

            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                Timber.i("checkOpdHours: ");
                return false;

            } else {
                Timber.i("checkOpdHours: " + x);

                return true;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
*/
