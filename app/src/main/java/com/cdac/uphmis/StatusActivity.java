package com.cdac.uphmis;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cdac.uphmis.adapter.StatusAdapter;
import com.cdac.uphmis.appointment.adapter.MyAppointmentsAdapter;
import com.cdac.uphmis.appointment.model.MyAppointmentDetails;

import com.cdac.uphmis.covid19.model.PatientRequestDetails;
import com.cdac.uphmis.model.PatientDetails;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.MySingleton;
import com.cdac.uphmis.util.NetworkStats;
import com.cdac.uphmis.util.ServiceUrl;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.Manifest.permission.READ_PHONE_STATE;
import static com.android.volley.VolleyLog.TAG;

public class StatusActivity extends AppCompatActivity {
    private ListView lvData;
    private LinearLayout noResultFound;
    private ManagingSharedData msd;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private String strUserId = "0";
    private TabLayout tabLayout;

    private ArrayList<String> fetaureIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));

        }
        msd = new ManagingSharedData(this);
        TextView tvName=findViewById(R.id.name);
        TextView tvCrno=findViewById(R.id.cr);

        tvName.setText(msd.getPatientDetails().getFirstname());
        tvCrno.setText(msd.getPatientDetails().getCrno());
        //NukeSSLCerts.nuke(this);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Teleconsultation"));
        tabLayout.addTab(tabLayout.newTab().setText("Appointment"));

        lvData = findViewById(R.id.lv_upcoming_request_list);
        noResultFound = findViewById(R.id.ll_no_record_found);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                () -> myOncreaeView()
        );

        myOncreaeView();


    }

    private void myOncreaeView() {
        Intent intent = getIntent();
        if (intent != null) {
            fetaureIdArrayList = (ArrayList<String>) intent.getSerializableExtra("fetaureIdArrayList");
            if (fetaureIdArrayList != null && fetaureIdArrayList.contains("CS")) {
                tabLayout.setVisibility(View.GONE);
                getAppointmentsFromCr(msd.getPatientDetails().getCrno());
            } else if (fetaureIdArrayList != null && fetaureIdArrayList.contains("AS")) {
                getRequestList(msd.getPatientDetails().getMobileNo(), msd.getPatientDetails().getCrno());
                tabLayout.setVisibility(View.GONE);
            }else
            {
                getRequestList(msd.getPatientDetails().getMobileNo(), msd.getPatientDetails().getCrno());
            }
        }
        if (msd.getuserId() != null) {
            strUserId = msd.getuserId();
        }

        lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lvData.getChildAt(0) != null) {
                    mySwipeRefreshLayout.setEnabled(lvData.getFirstVisiblePosition() == 0 && lvData.getChildAt(0).getTop() == 0);
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                PatientDetails registeredPatientDetails = (RegisteredPatientDetails) forSpinner.getSelectedItem();
                if (tabLayout.getSelectedTabPosition() == 0) {
                    getRequestList(msd.getPatientDetails().getMobileNo(), msd.getPatientDetails().getCrno());
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    getAppointmentsFromCr(msd.getPatientDetails().getCrno());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getRequestList(String mobileNumber, String crno) {
        Objects.requireNonNull(tabLayout.getTabAt(0)).select();
        showLoading();
        final ArrayList<PatientRequestDetails> upComnigArrayList = new ArrayList<>();
        final ArrayList<PatientRequestDetails> pastArrayList = new ArrayList<>();

        final StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.viewRequestListByPatMobNo + mobileNumber + "&userId=" + strUserId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);

                try {
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String requestID = c.getString("requestID");
                        String crNo = c.getString("CRNo");
                        String scrResponse = c.getString("scrResponse");
                        String consName = c.getString("consName");
                        String deptUnitCode = c.getString("deptUnitCode");
                        String deptUnitName = c.getString("deptUnitName");
                        String hospCode = c.getString("hospCode");
                        String requestStatus = c.getString("requestStatus");
                        String patMobileNo = c.getString("patMobileNo");
                        String consMobileNo = c.getString("consMobileNo");
                        String patDocs = c.getString("patDocs");
                        String docMessage = c.getString("docMessage");
                        String cnsltntId = c.getString("cnsltntId");
                        String patName = c.getString("patName");
                        String patAge = c.getString("patAge");
                        String patGender = c.getString("patGender");
                        String rmrks = c.getString("rmrks");
                        String email = c.getString("email");
                        String date = c.getString("date");
                        String patWeight = c.getString("patWeight");
                        String patHeight = c.getString("patHeight");
                        String patMedication = c.getString("patMedication");
                        String patPastDiagnosis = c.getString("patPastDiagnosis");
                        String patAllergies = c.getString("patAllergies");
                        String deptName = c.getString("deptName");
                        String deptCode = c.getString("deptCode");
                        String appointmentNo = c.getString("appointmentNo");
                        String apptStartTime = c.getString("apptStartTime");
                        String apptEndTime = c.getString("apptEndTime");
                        String apptDate = c.getString("apptDate");
                        String hospitalName = c.getString("hospitalName");
                        String isEpisodeExist = c.getString("isEpisodeExist");
                        String episodeCode = c.getString("episodeCode");
                        String episodeVisitNo = c.getString("episodeVisitNo");
                        String requestStatusCompleteDate = c.getString("requestStatusCompleteDate");
                        String requestStatusCompleteMode = c.getString("requestStatusCompleteMode");

                        String patientToken = c.optString("patientToken");
                        String doctorToken = c.optString("doctorToken");
                        String consultationTime = c.optString("consultationTime");

                        patName = AppUtilityFunctions.capitalizeString(patName);

                        if (crno.equalsIgnoreCase(crNo)) {

                            if (apptDate.equalsIgnoreCase("")) {
                                pastArrayList.add(new PatientRequestDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptName, deptCode, appointmentNo, apptStartTime, apptEndTime, date, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode, true, patientToken, doctorToken, consultationTime));
                            } else {
                                try {
                                    Date appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(apptDate);
                                    Date todaysDate = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(todaysDate));


                                    if (appointmentDate.compareTo(date2) < 0 || requestStatus.equalsIgnoreCase("2")) {
                                        Log.i(TAG, "past " + apptStartTime);
                                        pastArrayList.add(new PatientRequestDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptName, deptCode, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode, true, patientToken, doctorToken, consultationTime));
                                    } else {
                                        Log.i(TAG, "upcoming " + apptStartTime);
                                        upComnigArrayList.add(new PatientRequestDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptName, deptCode, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo, requestStatusCompleteDate, requestStatusCompleteMode, false, patientToken, doctorToken, consultationTime));
                                    }
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex);

                                }
                            }

                        }

                    }
                    if (upComnigArrayList.size() == 0 && pastArrayList.size() == 0) {
                        noRecordsFound();
                    } else {

                        StatusAdapter adapter = new StatusAdapter(com.cdac.uphmis.StatusActivity.this);
                        adapter.addSectionHeaderItem("Upcoming");
                        adapter.addItem(upComnigArrayList, false);
                        adapter.addSectionHeaderItem("Past");
                        adapter.addItem(pastArrayList, true);
                        lvData.setAdapter(adapter);
                        hideloading();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    noRecordsFound();
                }
            }
        }, error -> {
            hideloading();
            Log.i(TAG, "onErrorResponse: " + error);

        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void getAppointmentsFromCr(String crno) {
        showLoading();
        final ArrayList<MyAppointmentDetails> myAppointmentDetailsArrayList = new ArrayList();
        Log.d("req_app",ServiceUrl.getPreviousAppointmentsByCRNoUrl + "hospCode=100&patCRNo=" + crno);
        StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.getPreviousAppointmentsByCRNoUrl + "hospCode=100&patCRNo=" + crno, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response is ", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray appointmentList = jsonObject.getJSONArray("appointments_list");

                    if (appointmentList.length() == 0) {
                        noRecordsFound();
                    } else {

                        for (int i = 0; i < appointmentList.length(); i++) {
                            JSONObject c = appointmentList.getJSONObject(i);
                            String is_previous_appointment = c.optString("IS_PREVIOUS_APPOINTMENT");
                            String appointmentno = c.optString("APPOINTMENTNO");
                            String patcrno = c.optString("PATCRNO");
                            String episodecode = c.optString("EPISODECODE");
                            String patfirstname = c.optString("PATFIRSTNAME");
                            String patmiddlename = c.optString("PATMIDDLENAME");
                            String patlastname = c.optString("PATLASTNAME");
                            String patguardianname = c.optString("PATGUARDIANNAME");
                            String patgendercode = c.optString("PATGENDERCODE");
                            String emailid = c.optString("EMAILID");
                            String mobileno = c.optString("MOBILENO");
                            String appointmentqueueno = c.optString("APPOINTMENTQUEUENO");
                            String appointmenttime = c.optString("APPOINTMENTTIME");
                            String appointmentstatus = c.optString("APPOINTMENTSTATUS");
                            String statusremarks = c.optString("STATUSREMARKS");
                            String slottype = c.optString("SLOTTYPE");
                            String remarks = c.optString("REMARKS");
                            String appointmenttypeid = c.optString("APPOINTMENTTYPEID");
                            String modulespecificcode = c.optString("MODULESPECIFICCODE");
                            String appointmentmode = c.optString("APPOINTMENTMODE");
                            String modulespecifickeyname = c.optString("MODULESPECIFICKEYNAME");
                            String patage = c.optString("PATAGE");
                            String patspousename = c.optString("PATSPOUSENAME");
                            String appointmentdate = c.optString("APPOINTMENTDATE");
                            String appointmentforid = c.optString("APPOINTMENTFORID");
                            String appointmentforname = c.optString("APPOINTMENTFORNAME");
                            String actulaparaid1 = c.optString("ACTUALPARAID1");
                            String actulaparaid2 = c.optString("ACTUALPARAID2");
                            String actulaparaid3 = c.optString("ACTUALPARAID3");
                            String actulaparaid4 = c.optString("ACTUALPARAID4");
                            String actulaparaid5 = c.optString("ACTUALPARAID5");
                            String actulaparaid6 = c.optString("ACTUALPARAID6");
                            String actulaparaid7 = c.optString("ACTUALPARAID7");
                            String actulaparaname1 = c.optString("ACTUALPARANAME1");
                            String actulaparaname2 = c.optString("ACTUALPARANAME2");
                            String actulaparaname3 = c.optString("ACTUALPARANAME3");
                            String actulaparaname4 = c.optString("ACTUALPARANAME4");
                            String actulaparaname5 = c.optString("ACTUALPARANAME5");
                            String actulaparaname6 = c.optString("ACTUALPARANAME6");
                            String actulaparaname7 = c.optString("ACTUALPARANAME7");


                            String isFeesPaid = c.optString("IS_FEES_PAID");
                            String hospCode = c.optString("HOSP_CODE");
                            String hospName = c.optString("HOSP_NAME");
                            String actualParaRefId = c.optString("PARA_REFID");


                            myAppointmentDetailsArrayList.add(new MyAppointmentDetails(is_previous_appointment,appointmentno, patcrno, episodecode, patfirstname, patmiddlename, patlastname, patguardianname, patgendercode, emailid, mobileno, appointmentqueueno, appointmenttime, appointmentstatus, statusremarks, slottype, remarks, appointmenttypeid, modulespecificcode, appointmentmode, modulespecifickeyname, patage, patspousename, appointmentdate, appointmentforid, appointmentforname, actulaparaid1, actulaparaid2, actulaparaid3, actulaparaid4, actulaparaid5, actulaparaid6, actulaparaid7, actulaparaname1, actulaparaname2, actulaparaname3, actulaparaname4, actulaparaname5, actulaparaname6, actulaparaname7, isFeesPaid, hospCode, hospName,actualParaRefId));


                        }

                        MyAppointmentsAdapter adapter = new MyAppointmentsAdapter(com.cdac.uphmis.StatusActivity.this, myAppointmentDetailsArrayList);
                        lvData.setAdapter(adapter);
                        hideloading();

                    }
                } catch (final JSONException e) {
                    noRecordsFound();
                    Log.i("jsonexception", "onResponse: " + e);
                }


            }
        }, error -> {
            noRecordsFound();
            Log.i("error", "onErrorResponse: " + error);
            AppUtilityFunctions.handleExceptions(error, com.cdac.uphmis.StatusActivity.this);
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showLoading() {
        mySwipeRefreshLayout.setRefreshing(true);
        lvData.setVisibility(View.GONE);
        noResultFound.setVisibility(View.GONE);
    }

    private void hideloading() {
        mySwipeRefreshLayout.setRefreshing(false);
        lvData.setVisibility(View.VISIBLE);
        noResultFound.setVisibility(View.GONE);
    }

    private void noRecordsFound() {
        mySwipeRefreshLayout.setRefreshing(false);
        lvData.setVisibility(View.GONE);
        noResultFound.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.patient_toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_network_stats);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
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
                AppUtilityFunctions.shareApp(com.cdac.uphmis.StatusActivity.this);
                return true;
            case R.id.action_info:
                AppUtilityFunctions.showInfoLinksDialog(this);
                return true;
            case R.id.action_network_stats:
                if (checkSelfPermission(READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please give phone state permission.", Toast.LENGTH_SHORT).show();
                } else {
                    NetworkStats.appUpdateDialog(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

}