package com.cdac.uphmis.appointment;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.R;
import com.cdac.uphmis.appointment.adapter.DateAdapter;
import com.cdac.uphmis.appointment.adapter.MyRecycleViewClickListener;
import com.cdac.uphmis.appointment.model.DateDetails;
import com.cdac.uphmis.appointment.model.DateSlotDetails;
import com.cdac.uphmis.appointment.model.MyAppointmentDetails;
import com.cdac.uphmis.appointment.model.OPDAppointmentDetails;
import com.cdac.uphmis.appointment.model.ShiftDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NukeSSLCerts;
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

public class AppointmentRescheduleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RecyclerView rvDate;
    TextView tvSelectedDate;
    ArrayList<DateDetails> arrayListDates;
    private String deptCode;
    MyAppointmentDetails myAppointmentDetails;
    GeometricProgressView progressView;
    private Button btnConfirm;
    ArrayList<ShiftDetails> shiftDetailsArrayList;
    private List<Calendar> datesArrayList;
    private ArrayList<String> holidayList;
    private TextView tvAvailableSlots;
    private LinearLayout llData;
    private TextView tvSlotDescription;
    private ImageView thumbImage;
    DateAdapter adapter;
    ArrayList<DateSlotDetails> arraySlotDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_reschedule);
        // To retrieve object in second Activity
        myAppointmentDetails = (MyAppointmentDetails) getIntent().getSerializableExtra("myAppointmentDetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getHolidayDates();
        btnConfirm = findViewById(R.id.btn_confirm);
        llData = findViewById(R.id.ll_data);
        progressView = (GeometricProgressView) findViewById(R.id.progress_view);
        shiftDetailsArrayList = new ArrayList<>();
        tvAvailableSlots = findViewById(R.id.tv_available_slots);
        tvSlotDescription = findViewById(R.id.tv_slot_description);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        thumbImage = findViewById(R.id.img_thumbs);
        rvDate = findViewById(R.id.rv_date);
        rvDate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDate.setItemAnimator(new DefaultItemAnimator());
        if (myAppointmentDetails.getActulaparaid1().substring(0, 5).length() == 5) {
            deptCode = myAppointmentDetails.getActulaparaid1().substring(0, 5);
        }
        rvDate.addOnItemTouchListener(new MyRecycleViewClickListener(this, new MyRecycleViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (progressView.getVisibility() != View.VISIBLE) {
                    adapter.row_index = position;
                    adapter.notifyDataSetChanged();
                    DateSlotDetails dateDetails = (DateSlotDetails) arraySlotDates.get(position);
                    tvSelectedDate.setText(dateDetails.getApp_date());
                    getShiftName(dateDetails.getApp_date(), myAppointmentDetails.getActualParaRefId());
                } else {
                    Toast.makeText(AppointmentRescheduleActivity.this, "Please Wait...", Toast.LENGTH_SHORT).show();
                }
            }
        }));
        getDates(0);

    }

    private void getDates(int row_index) {
        arraySlotDates= new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate= formatter.format(date);
        arraySlotDates.add(new DateSlotDetails(strDate,"","","",""+strDate));
        getShiftName(strDate, myAppointmentDetails.getActualParaRefId());
         StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getSlotsDateServer+ myAppointmentDetails.getHospCode()+"&pParaRefId="+ myAppointmentDetails.getActualParaRefId() , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("aaaa", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotList = jsonObject.getJSONArray("data");
                    if (slotList.length() == 0) {
                        if (this != null) {
                            Toast.makeText(AppointmentRescheduleActivity.this, "Slot not found!", Toast.LENGTH_SHORT).show();
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
                    adapter = new DateAdapter(AppointmentRescheduleActivity.this, arraySlotDates, row_index);
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
        progressView.setVisibility(View.VISIBLE);
        ManagingSharedData msd = new ManagingSharedData(this);
        Log.i("date", "getShiftName: " + date + "," + deptCode);
        shiftDetailsArrayList = new ArrayList<>();
        llData.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.shiftNameurl + "hospCode=" + myAppointmentDetails.getHospCode() + "&aptDate=" + date + "&deptUnitCode=" + deptCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);
                Log.i("aaaa", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotList = jsonObject.getJSONArray("slot_list");
                    if (slotList.length() == 0) {
                        if (this != null) {
                            btnConfirm.setVisibility(View.GONE);
                            tvAvailableSlots.setText(myAppointmentDetails.getActulaparaname1());
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
                            tvAvailableSlots.setText(myAppointmentDetails.getActulaparaname1());
                            tvSlotDescription.setText(getString(R.string.slot_not_available));
                            thumbImage.setBackgroundResource(R.drawable.thumbs_down);
                        } else {
                            thumbImage.setBackgroundResource(R.drawable.slots_available);
                            btnConfirm.setVisibility(View.VISIBLE);
                            tvAvailableSlots.setText(myAppointmentDetails.getActulaparaname1());
                            tvSlotDescription.setText(shiftDetailsArrayList.size() + " Slots Available, Please press confirm to raise appointment request.");
                        }
                    }
                    llData.setVisibility(View.VISIBLE);

                } catch (final JSONException e) {
                    Log.i("jsonexception", "onResponse: " + e);
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.VISIBLE);
                    btnConfirm.setVisibility(View.GONE);
                    tvAvailableSlots.setText(myAppointmentDetails.getActulaparaname1());
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
    public void btnConfirm(View view) {
        if (shiftDetailsArrayList.size() != 0) {
            OPDAppointmentDetails appointmentDetails = new OPDAppointmentDetails();
            appointmentDetails.setShiftId(shiftDetailsArrayList.get(0).getShift());
            appointmentDetails.setShiftST(shiftDetailsArrayList.get(0).getShiftst());
            appointmentDetails.setShiftET(shiftDetailsArrayList.get(0).getShiftet());
            appointmentDetails.setSlotST(shiftDetailsArrayList.get(0).getSlotst());
            appointmentDetails.setSlotET(shiftDetailsArrayList.get(0).getSlotet());
            appointmentDetails.setAppointmentDate(shiftDetailsArrayList.get(0).getDate());
            String patAgeUnit = myAppointmentDetails.getPatage().replaceAll("[0-9]", "");
            appointmentDetails.setPatAgeUnit(patAgeUnit);

            //call reschedule appointment service
//            if (checkOpdHours("10:01", "23:59", shiftDetailsArrayList.get(0).getDate())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentRescheduleActivity.this);
                builder.setMessage("Do you want to proceed?");
                builder.setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rescheduleAppointment(appointmentDetails);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), null);
                final AlertDialog dialog = builder.create();
                dialog.show();

//            } else {
//                new AlertDialog.Builder(AppointmentRescheduleActivity.this)
//                        .setTitle(getString(R.string.information))
//                        .setMessage(getString(R.string.date_vaidation_10am))
//                        .setPositiveButton(getString(R.string.ok), null)
//                        .show();
//
//            }
        } else {
            Toast.makeText(this, getString(R.string.no_slot_available_select_date), Toast.LENGTH_SHORT).show();
        }
    }

    public void rescheduleAppointment(OPDAppointmentDetails appointmentDetails) {
        RequestQueue requestQueue = Volley.newRequestQueue(AppointmentRescheduleActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, ServiceUrl.rescheduleappointmenturl
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("register user", "onResponse: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("SUCCESS").equals("true")) {
                            Intent intent = new Intent(AppointmentRescheduleActivity.this, RescheduleAppointmentSummaryActivity.class);
                            intent.putExtra("appointmentDetails", appointmentDetails);
                            intent.putExtra("myAppointmentDetails", myAppointmentDetails);
                            intent.putExtra("apptno", jsonObj.getString("APPTNO"));
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(AppointmentRescheduleActivity.this, getString(R.string.reschedule_appointment), Toast.LENGTH_SHORT).show();
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
                AppUtilityFunctions.handleExceptions(error, AppointmentRescheduleActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String, String>
                HashMap<String, String> data = new HashMap<>();

                data.put("appointmentNo", myAppointmentDetails.getAppointmentno());
                data.put("appointmentQueueNo", myAppointmentDetails.getAppointmentqueueno());
                data.put("appointmentForId", "1");
                data.put("patFirstName", myAppointmentDetails.getPatfirstname());
                data.put("patMiddleName", myAppointmentDetails.getPatmiddlename());
                data.put("patLastName", myAppointmentDetails.getPatlastname());
                data.put("patGuardianName", myAppointmentDetails.getPatguardianname());
                data.put("patSpouseName", myAppointmentDetails.getPatspousename());
                data.put("patDOB", "");
                data.put("appointmentDate", appointmentDetails.getAppointmentDate());
                data.put("emailId", myAppointmentDetails.getEmailid());
                data.put("mobileNo", myAppointmentDetails.getMobileno());
                data.put("appointmentTime", appointmentDetails.getSlotST());
                data.put("appointmentStatus", "2");
                data.put("slotType", "3");
                data.put("remarks", myAppointmentDetails.getRemarks());
                data.put("appointmentTypeId", "1");
                data.put("appointmentMode", "13");
                data.put("patAgeUnit", appointmentDetails.getPatAgeUnit());
                data.put("patAge", myAppointmentDetails.getPatage().replaceAll("\\D", ""));
                data.put("patGenderCode", myAppointmentDetails.getPatgendercode());
                data.put("allActualParameterId", "");
                data.put("shiftId", appointmentDetails.getShiftId());
                data.put("slotST", appointmentDetails.getSlotST());
                data.put("slotET", appointmentDetails.getSlotET());
                data.put("shiftST", appointmentDetails.getShiftST());
                data.put("shiftET", appointmentDetails.getShiftET());
                data.put("hcode", myAppointmentDetails.getHospCode());
                data.put("patCrNo", myAppointmentDetails.getPatcrno());
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
                    displayDate = "Tomorrow, " + format2.format(date1);

                } else {
                    SimpleDateFormat format2 = new SimpleDateFormat("EEE, dd MMM");
                    displayDate = format2.format(date1);

                    System.out.println(format2.format(date1));

                }

                System.out.println("weekday " + displayDate);

            return new DateSlotDetails(date, "", "","",displayDate);
//            } else {
//                System.out.println("weekend " + displayDate);
//                return new DateDetails(date, displayDate, "");
//            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return new DateSlotDetails(date, "", "","",displayDate);
        }

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
                Toast.makeText(AppointmentRescheduleActivity.this, "Error Loading Calendar.", Toast.LENGTH_SHORT).show();
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
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                AppointmentRescheduleActivity.this,
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


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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
        getShiftName(strDate, myAppointmentDetails.getActualParaRefId());

        adapter = new DateAdapter(getApplicationContext(), arraySlotDates, -1);
        rvDate.setAdapter(adapter);
    }


    public void btnDate(View view) {
        if (datesArrayList != null) {
            showDate(datesArrayList.toArray(new Calendar[datesArrayList.size()]));
        } else {
            getHolidayDates();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                AppUtilityFunctions.shareApp(AppointmentRescheduleActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
           /* case R.id.action_help:
                startActivity(new Intent(SlotSelectionActivity.this, HelpActivity.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}