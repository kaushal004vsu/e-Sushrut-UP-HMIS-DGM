package com.cdac.uphmis.appointment;

import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.model.DateSlotDetails;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.appointment.adapter.DateAdapter;
import com.cdac.uphmis.appointment.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.appointment.model.DateDetails;
import com.cdac.uphmis.appointment.model.ShiftDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.ServiceUrl;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OPDAppointmentSlotSelectionActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    RecyclerView rvDate;
    TextView tvSelectedDate;

    ArrayList<DateDetails> arrayListDates;
    private String deptCode;
    OPDAppointmentDetails screeningDetails;

    GeometricProgressView progressView;
    private TextView tvAvailableSlots;
    public Button btnConfirm;

    private LinearLayout llData;
    ArrayList<ShiftDetails> shiftDetailsArrayList;

    private TextView tvSlotDescription;
    private ImageView thumbImage;

    private List<Calendar> datesArrayList;
    private ArrayList<String> holidayList;

    DateAdapter adapter;
    ArrayList<DateSlotDetails> arraySlotDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opdappointment_slot_selection);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //NukeSSLCerts.nuke(this);
        // requestQueue = Volley.newRequestQueue(this);
        btnConfirm = findViewById(R.id.btn_confirm);
        llData = findViewById(R.id.ll_data);

        progressView = (GeometricProgressView) findViewById(R.id.progress_view);

        shiftDetailsArrayList = new ArrayList<>();
        // To retrieve object in second Activity
        screeningDetails = (OPDAppointmentDetails) getIntent().getSerializableExtra("appointmentDetails");

        tvAvailableSlots = findViewById(R.id.tv_available_slots);
        tvSlotDescription = findViewById(R.id.tv_slot_description);
        tvSelectedDate = findViewById(R.id.tv_selected_date);

        thumbImage = findViewById(R.id.img_thumbs);
        rvDate = findViewById(R.id.rv_date);
        rvDate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDate.setItemAnimator(new DefaultItemAnimator());

        deptCode = screeningDetails.getDeptUnitCode();
        rvDate.addOnItemTouchListener(new MyRecycleViewClickListener(this, (view, position) -> {
            DateSlotDetails dateDetails = arraySlotDates.get(position);
            if (progressView.getVisibility() != View.VISIBLE) {
                adapter.row_index = position;
                adapter.notifyDataSetChanged();
                tvSelectedDate.setText(dateDetails.getDisplayDate());

                Log.i("getshiftname", "onCreate: addOnItemTouchListener"+dateDetails.getApp_date());
                getShiftName(dateDetails.getApp_date(), screeningDetails.getActualParameterReferenceId());
            } else {
                Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        }));
         // getHolidayDates();
        getDates(0);
    }

    private void getDates(int row_index) {
        arraySlotDates= new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate= formatter.format(date);
        arraySlotDates.add(new DateSlotDetails(strDate,"","","",""+strDate));
        getShiftName(strDate, screeningDetails.getActualParameterReferenceId());
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getSlotsDateServer+screeningDetails.getHcode()+"&pParaRefId="+screeningDetails.getActualParameterReferenceId() , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaaa", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotList = jsonObject.getJSONArray("data");
                    if (slotList.length() == 0) {
                        if (this != null) {
                            Toast.makeText(OPDAppointmentSlotSelectionActivity.this, "Slot not found!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        for (int i = 0; i < slotList.length(); i++) {

                            JSONObject c = slotList.getJSONObject(i);
                            String app_date = c.optString("APP_DATE");
                            String t_app = c.optString("T_APP");
                            String book_app = c.optString("BOOK_APP");
                            String avl_app = c.optString("AVL_APP");
                            arraySlotDates.add(new DateSlotDetails(app_date,t_app,""+book_app,""+avl_app,""));
                        }
                    }
                    adapter = new DateAdapter(OPDAppointmentSlotSelectionActivity.this, arraySlotDates, row_index);
                    rvDate.setAdapter(adapter);
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.VISIBLE);

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                }
            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
            progressView.setVisibility(View.GONE);
            llData.setVisibility(View.GONE);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);

    }
    public void getShiftName(String date, String deptCode) {
        shiftDetailsArrayList = new ArrayList<>();
        progressView.setVisibility(View.VISIBLE);
        llData.setVisibility(View.GONE);


        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.shiftNameurl + "hospCode=" + screeningDetails.getHcode() + "&aptDate=" + date + "&deptUnitCode=" + deptCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                           for (int i = 0; i < slotList.length(); i++) {
                            JSONObject c = slotList.getJSONObject(i);
                            String date = c.getString("DT");
                            String shift = c.getString("SHIFT");
                            String shiftName = c.getString("GETSHIFTNAME");
                            String shiftst = c.getString("SHIFTST");
                            String shiftet = c.getString("SHIFTET");
                            String slotst = c.getString("SLOTST");
                            String slotet = c.getString("SLOTET");
                            String freeslotdetail = c.optString("FREESLOTDETAIL");
                            String slotstatus = c.getString("GETSLOTSTATUS");
                            String opdslots = c.getString("OPDSLOTS");
                            String ipdslots = c.getString("IPDSLOTS");
                            String opdbookedslots = c.getString("OPDBOOKEDSLOTS");
                            String ipdbookedslots = c.getString("IPDBOOKEDSLOTS");
                            if (slotstatus.equalsIgnoreCase("1"))
                            shiftDetailsArrayList.add(new ShiftDetails(date, shift, shiftName, shiftst, shiftet, slotst, slotet, freeslotdetail, slotstatus, opdslots, ipdslots, opdbookedslots, ipdbookedslots));
                        }
                        if (shiftDetailsArrayList.size() == 0) {
                            btnConfirm.setVisibility(View.GONE);
                            tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                            tvSlotDescription.setText(getString(R.string.slot_not_available));
                            thumbImage.setBackgroundResource(R.drawable.thumbs_down);
                        } else {
                            thumbImage.setBackgroundResource(R.drawable.slots_available);
                            btnConfirm.setVisibility(View.VISIBLE);
                            tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                            tvSlotDescription.setText("" + shiftDetailsArrayList.size() + getString(R.string.slots_available));
                        }
                    }
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.VISIBLE);

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.VISIBLE);
                    btnConfirm.setVisibility(View.GONE);
                    tvAvailableSlots.setText(screeningDetails.getDeptUnitName());
                    tvSlotDescription.setText(getString(R.string.slot_not_available));
                    thumbImage.setBackgroundResource(R.drawable.thumbs_down);
                }
            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error);
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
        if (datesArrayList != null) {
            if (progressView.getVisibility() != View.VISIBLE) {
                if (adapter != null) {
                    adapter.row_index = -1;
                    adapter.notifyDataSetChanged();
                }
                showDate(datesArrayList.toArray(new Calendar[datesArrayList.size()]));
            } else {
                Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
            }
        } else {
            getHolidayDates();
        }

    }


    public DateSlotDetails getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);

        cal.add(Calendar.DAY_OF_YEAR, days);

        String date = s.format(new Date(cal.getTimeInMillis()));


        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
        String displayDate = "";
        try {
//            if (!(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                Date date1 = format1.parse(date);

                if (days == 0) {
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = "Today, " + format2.format(date1);

                } else if (days == 1) {
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = "Tomorrow,   " + format2.format(date1);

                } else {
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = format2.format(date1);

                    System.out.println(format2.format(date1));

                }

                System.out.println("weekday " + displayDate);

                return new DateSlotDetails("", "", "","",displayDate);


//            } else {
//                System.out.println("weekend " + displayDate);
//                return new DateDetails(date, displayDate, "");
//            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return new DateSlotDetails("", "", "","",displayDate);
        }

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

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(OPDAppointmentSlotSelectionActivity.this);
                return true;
           /* case R.id.action_help:
                startActivity(new Intent(SlotSelectionActivity.this, HelpActivity.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void btnConfirm(View view) {
        if (shiftDetailsArrayList.size() != 0) {
            screeningDetails.setShiftId(shiftDetailsArrayList.get(0).getShift());
            screeningDetails.setShiftST(shiftDetailsArrayList.get(0).getShiftst());
            screeningDetails.setShiftET(shiftDetailsArrayList.get(0).getShiftet());
            screeningDetails.setSlotST(shiftDetailsArrayList.get(0).getSlotst());
            screeningDetails.setSlotET(shiftDetailsArrayList.get(0).getSlotet());
            screeningDetails.setAppointmentDate(shiftDetailsArrayList.get(0).getDate());
//            if (checkOpdHours("10:01", "23:59", shiftDetailsArrayList.get(0).getDate())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.do_u_want_to_proceed));
            builder.setPositiveButton(getString(R.string.proceed), (dialog, which) -> {
//                    Log.i("appointmentdate", "onClick: "+screeningDetails.getAppointmentDate());
                String age;
                if (screeningDetails.getPatAge() == null) {
                    age = "";
                } else {
                    age = screeningDetails.getPatAge();
                }
                save(screeningDetails.getAppointmentForId(), screeningDetails.getPatFirstName(), screeningDetails.getPatMiddleName(), screeningDetails.getPatLastName(), age, screeningDetails.getPatAgeUnit(), screeningDetails.getPatGuardianName(), screeningDetails.getPatSpouseName(), screeningDetails.getMobileNo(), screeningDetails.getEmailId(), screeningDetails.getRemarks(), screeningDetails.getPatGenderCode(), screeningDetails.getDeptUnitCode(), screeningDetails.getDeptUnitName(), screeningDetails.getDeptLocation(), screeningDetails.getShiftId(), screeningDetails.getShiftST(), screeningDetails.getShiftET(), screeningDetails.getSlotST(), screeningDetails.getSlotET(), screeningDetails.getActualParameterReferenceId(), screeningDetails.getAppointmentDate(), screeningDetails.getPatCrNo(), "0", screeningDetails.getTariffId(), screeningDetails.getCharge(), "3");
            });
            builder.setNegativeButton(getString(R.string.cancel), null);
            final AlertDialog dialog = builder.create();
            dialog.show();
           /* } else {
                new AlertDialog.Builder(OPDAppointmentSlotSelectionActivity.this)
                        .setTitle("Information")
                        .setMessage("Appointments can only be booked before 10:00 am on the same day. Please book appointment for another date.")
                        .setPositiveButton("OK", null)
                        .show();
            }*/


        } else {
            Toast.makeText(this, getString(R.string.no_slot_available_select_date), Toast.LENGTH_SHORT).show();
        }
    }



    public void save(final String appointmentForId, final String firstName, final String middleName, final String lastName, final String age, final String patAgeUnit, final String fatherName, final String spouseName, final String mobileNo, final String email, final String remarks, final String genderId, final String departmentUnitCode, final String departUnitName, final String departLocation, final String shiftId, final String shiftSt, final String shiftet, final String slotSt, final String slotEt, final String actualparameterreferenceid, final String appointmentDate, final String crno, String isFeesPaid, String tariffId, String paymentAmount, String paymentType) {
        Log.i("appointmentDate", "appointmentDate: " + appointmentDate);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.makeAppointment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("register user", "onResponse: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("SUCCESS").equals("true")) {
                            Intent intent = new Intent(OPDAppointmentSlotSelectionActivity.this, AppointmentSummaryActivity.class);
                            intent.putExtra("appointmentDetails", screeningDetails);
                            intent.putExtra("apptno", jsonObj.getString("APPTNO"));
                            startActivity(intent);
                            finish();
                        } else {
                            String message = jsonObj.getString("msg");
                            if (message.trim().isEmpty()) {
                                message = getString(R.string.failed_appointment);
                            }
                            new AlertDialog.Builder(OPDAppointmentSlotSelectionActivity.this)
                                    .setTitle(getString(R.string.failed))
                                    .setMessage(message)
                                    .setPositiveButton("OK", null)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OPDAppointmentSlotSelectionActivity.this, getString(R.string.failed_appointment), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }, error -> {
            Log.i("error", "onErrorResponse: " + error.getMessage());

            AppUtilityFunctions.handleExceptions(error, OPDAppointmentSlotSelectionActivity.this);
        }) {
            @Override
            //  protected Map<String, String> getParams() throws AuthFailureError {
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                HashMap data = new HashMap<>();

                data.put("appointmentForId", appointmentForId);
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
                data.put("hcode", screeningDetails.getHcode());
                data.put("deptUnitCode", departmentUnitCode);
                data.put("deptUnitName", departUnitName);
                data.put("deptLocation", (departLocation != null) ? departLocation : "");
                data.put("patCrNo", crno);

                data.put("is_fees_paid", "0");
                data.put("tariff_id", tariffId);
                data.put("payment_fee", paymentAmount);
                data.put("payment_ref_no", "");


                data.put("payment_type", paymentType);//payment type =3 in case of tariff charge is 0.00
                data.put("transaction_id", "");
                Log.i("hashmap", "getParams: " + data);


                return data;

            }

        };

        MySingleton.getInstance(this).addToRequestQueue(request);
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


            String dateInString = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
            Log.i("dateInString", "checkOpdHours: " + dateInString);
            if (dateInString.equalsIgnoreCase(slotDate)) {

                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {

                    Log.i("showAlert", "checkOpdHours: ");
                    return false;

                } else {
                    Log.i("dontShowAlert", "checkOpdHours: " + x);

                    return true;
                }


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

        Log.i("getshiftname", "onCreate: onDateSet");
        getShiftName(strDate, screeningDetails.getActualParameterReferenceId());

        // adapter = new DateAdapter(getApplicationContext(), arrayListDates, -1);
        //rvDate.setAdapter(adapter);
        adapter.row_index = -1;
        adapter.notifyDataSetChanged();
    }

    private void getHolidayDates() {
        Toast.makeText(this, "Please wait while loading calendar.", Toast.LENGTH_SHORT).show();
        ManagingSharedData msd = new ManagingSharedData(this);
        datesArrayList = new ArrayList<>();
        holidayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getHolidayList + ServiceUrl.hospId, new Response.Listener<String>() {
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
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

                            Date date = format1.parse(holidayDate);
                            System.out.println(format2.format(date));
                            Calendar calendar = dateToCalendar(date);
                            datesArrayList.add(calendar);

                            SimpleDateFormat format3 = new SimpleDateFormat("dd-MMM-yyyy");
                            holidayList.add(format3.format(date));
                        }

                        getDates(0);

                    } else {
                        getDates(0);
                    }
                } catch (Exception ex) {
                    getDates(0);
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "onErrorResponse: " + error);
                finish();
                Toast.makeText(OPDAppointmentSlotSelectionActivity.this, "Error Loading Calendar.", Toast.LENGTH_SHORT).show();
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
                OPDAppointmentSlotSelectionActivity.this,
                Year, // Initial year selection
                Month, // Initial month selection
                Day // Inital day selection
        );
        datePickerDialog.setMinDate(calendar);


        // Setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        datePickerDialog.setMinDate(min_date_c);


        // Setting Max Date to next 2 years
      /*  Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.YEAR, Year + 2);
        datePickerDialog.setMaxDate(max_date_c);*/
        Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.DAY_OF_MONTH, Day);
        max_date_c.set(Calendar.MONTH, Month + 1);
        max_date_c.set(Calendar.YEAR, Year);

        datePickerDialog.setMaxDate(max_date_c);

        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY) {
                Calendar[] disabledDays = new Calendar[1];
                disabledDays[0] = loopdate;
                datePickerDialog.setDisabledDays(disabledDays);
            }
        }

        datePickerDialog.setOnCancelListener(dialogInterface -> {

        });

        datePickerDialog.setDisabledDays(disabledDates);
        datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


}
