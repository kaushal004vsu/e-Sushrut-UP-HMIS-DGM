package com.cdac.uphmis.DoctorDesk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.uphmis.util.MySingleton;
import com.google.android.material.tabs.TabLayout;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.cdac.uphmis.DoctorDesk.adapter.DoctorRequestListAdapter;
import com.cdac.uphmis.DoctorDesk.model.DoctorReqListDetails;
import com.cdac.uphmis.DoctorLoginActivity;
import com.cdac.uphmis.R;
import com.cdac.uphmis.util.AppUtilityFunctions;
import com.cdac.uphmis.util.ManagingSharedData;
import com.cdac.uphmis.util.NukeSSLCerts;
import com.cdac.uphmis.util.ServiceUrl;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.cdac.uphmis.util.AppUtilityFunctions.statusBartheme;


public class DoctorRequestListActivity extends AppCompatActivity {
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final String TAG = "mainactivity";

    ListView lv;
    EditText etSearch;
    GeometricProgressView progressView;


    boolean doubleBackToExitPressedOnce = false;
    ManagingSharedData msd;

    TextView tvLastUpdatedOn;

    SwipeRefreshLayout mySwipeRefreshLayout;

//    Spinner sStatusFilter;

    String strResponse;

    private DoctorRequestListAdapter adapter;

    LinearLayout llData;
    ImageView imgNoResultFound;


    TabLayout tabLayout;
    TextView tvTotalRecordsFound;

    public static ArrayList<String> deskArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_request_list);
        

        Intent intent = getIntent();
        if( getIntent().getExtras() != null)
        {
            deskArrayList = (ArrayList<String>) intent.getSerializableExtra("desk");

        }
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        msd = new ManagingSharedData(this);
        etSearch = findViewById(R.id.etSearch);
        tvTotalRecordsFound = findViewById(R.id.tv_total_records_found);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


//        tabLayout.addTab(tabLayout.newTab().setText("All"));
        /*tabLayout.addTab(tabLayout.newTab().setText("Todays"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));*/

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.todays)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.past)));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tabLayout.getSelectedTabPosition() == 0) {
                    getTodaysRequestList();
                    etSearch.getText().clear();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    filteredList("0",true);
                    etSearch.getText().clear();
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    getPastList("1",false);
                    etSearch.getText().clear();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tvLastUpdatedOn = findViewById(R.id.tv_last_updated_on);
        TextView tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvDoctorName.setText(msd.getUsername());
        ImageButton btnLogout = findViewById(R.id.btn_logout);

        llData = findViewById(R.id.ll_data);
        imgNoResultFound = findViewById(R.id.img_no_result_found);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msd.logOut();
                startActivity(new Intent(DoctorRequestListActivity.this, DoctorLoginActivity.class));
                finish();
            }
        });
        if (CheckPermissions()) {

        } else {
            RequestPermissions();
        }


        lv = findViewById(R.id.lv);
        progressView = findViewById(R.id.progress_view);

        getRequestList();

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                Selection.setSelection(etSearch.getText(), etSearch.getText().length());


            }
        });
//        sStatusFilter = findViewById(R.id.srelation);

        final String arStatus[] = {getString(R.string.all), getString(R.string.approved), getString(R.string.completed), getString(R.string.rejected), getString(R.string.unattended)};
        final ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arStatus);

//        adapterRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sStatusFilter.setAdapter(adapterRelation);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        getRequestList();

//                        adapterRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sStatusFilter.setAdapter(adapterRelation);
                        TabLayout.Tab tab = tabLayout.getTabAt(0);
                        tab.select();
                    }
                }
        );

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv.getChildAt(0) != null) {
                    mySwipeRefreshLayout.setEnabled(lv.getFirstVisiblePosition() == 0 && lv.getChildAt(0).getTop() == 0);
                }
            }
        });



    }


    public boolean CheckPermissions() {
        int result0 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);

        return result0 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(DoctorRequestListActivity.this, new String[]{RECORD_AUDIO, READ_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        // Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void btnSpeechSearch(View view) {
        getSpeechInput();
    }

    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() .toString());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.i("result", "onActivityResult: " + result);

                }
                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            //handle click
            getSpeechInput();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getRequestList() {
        tvLastUpdatedOn.setText("Last updated on: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        //NukeSSLCerts.nuke(this);
        final ArrayList<DoctorReqListDetails> healthWorkerRequestListDetailsArrayList = new ArrayList<>();
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest request = new StringRequest(Request.Method.GET, ServiceUrl.viewRequestListByEmployeeCode + msd.getEmployeeCode() + "&hospCode=" + msd.getHospCode(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                try {
                    strResponse = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    response = new String(response.getBytes("ISO-8859-1"), "utf-8");
                    Log.i(TAG, "decodedresponse: " + strResponse);

                    mySwipeRefreshLayout.setRefreshing(false);
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
                        String deptCode = c.getString("deptCode");
                        String deptName = c.getString("deptName");
                        String isDocUploaded = c.getString("isDocUploaded");
                        String patientToken = c.getString("patientToken");
                        String doctorToken = c.getString("doctorToken");


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


                        patName = AppUtilityFunctions.capitalizeString(patName);
/*
//                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo));
                        Date todaysDate = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        if (apptDate.substring(0, 10).equalsIgnoreCase(formatter.format(todaysDate))) {
                            healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo));
                        }*/

                        if (requestStatus.equalsIgnoreCase("0")||requestStatus.equalsIgnoreCase("1")) {
                            Date todaysDate = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            if (apptDate.substring(0, 10).equalsIgnoreCase(formatter.format(todaysDate))) {
                                healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo,requestStatusCompleteDate,requestStatusCompleteMode));
                            }
                        }

                    }
                    tvTotalRecordsFound.setText(healthWorkerRequestListDetailsArrayList.size() + " records found");
                    adapter = new DoctorRequestListAdapter(DoctorRequestListActivity.this, healthWorkerRequestListDetailsArrayList,false);
                    lv.setAdapter(adapter);
                    progressView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    llData.setVisibility(View.VISIBLE);
                    imgNoResultFound.setVisibility(View.GONE);
                } catch (Exception ex) {
                    mySwipeRefreshLayout.setRefreshing(false);
                    Log.i(TAG, "exception" + ex);
                    progressView.setVisibility(View.GONE);
                    llData.setVisibility(View.GONE);
                    imgNoResultFound.setVisibility(View.VISIBLE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mySwipeRefreshLayout.setRefreshing(false);
                Log.i(TAG, "onErrorResponse: " + error);
                progressView.setVisibility(View.GONE);
                llData.setVisibility(View.GONE);
                imgNoResultFound.setVisibility(View.VISIBLE);
                AppUtilityFunctions.handleExceptions(error, DoctorRequestListActivity.this);
                //noResultFound.setVisibility(View.VISIBLE);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void filteredList(String status,boolean isUpComing) {

        ArrayList<DoctorReqListDetails> healthWorkerRequestListDetailsArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(strResponse);
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
                String deptCode = c.getString("deptCode");
                String deptName = c.getString("deptName");
                String isDocUploaded = c.getString("isDocUploaded");
                String patientToken = c.getString("patientToken");
                String doctorToken = c.getString("doctorToken");


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

                patName = AppUtilityFunctions.capitalizeString(patName);
//                if (requestStatus.equalsIgnoreCase(status)) {
                if (requestStatus.equalsIgnoreCase("0")||requestStatus.equalsIgnoreCase("4")) {
                    /*Date todaysDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");*/

                    Date appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(apptDate);
                    Date todaysDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(todaysDate));

                    Log.i(TAG, "compareTo"+appointmentDate.compareTo(date2));
                    if(appointmentDate.compareTo(date2)>0)
                    {
                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo,requestStatusCompleteDate,requestStatusCompleteMode));
                    }


                   /* if (!apptDate.substring(0, 10).equalsIgnoreCase(formatter.format(todaysDate))) {
                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo));
                    }*/
                }


            }
            tvTotalRecordsFound.setText(healthWorkerRequestListDetailsArrayList.size() + " records found");
            adapter = new DoctorRequestListAdapter(DoctorRequestListActivity.this, healthWorkerRequestListDetailsArrayList,isUpComing);
            lv.setAdapter(adapter);

        } catch (Exception ex) {

        }


    }


    private void getTodaysRequestList() {
        ArrayList<DoctorReqListDetails> healthWorkerRequestListDetailsArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(strResponse);
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
                String deptCode = c.getString("deptCode");
                String deptName = c.getString("deptName");
                String isDocUploaded = c.getString("isDocUploaded");
                String patientToken = c.getString("patientToken");
                String doctorToken = c.getString("doctorToken");


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
                patName = AppUtilityFunctions.capitalizeString(patName);


                if (requestStatus.equalsIgnoreCase("0")||requestStatus.equalsIgnoreCase("1")) {
                    Date todaysDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    if (apptDate.substring(0, 10).equalsIgnoreCase(formatter.format(todaysDate))) {
                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo,requestStatusCompleteDate,requestStatusCompleteMode));
                    }
                }
            }
            tvTotalRecordsFound.setText(healthWorkerRequestListDetailsArrayList.size() + " records found");
            adapter = new DoctorRequestListAdapter(DoctorRequestListActivity.this, healthWorkerRequestListDetailsArrayList,false);
            lv.setAdapter(adapter);

        } catch (Exception ex) {

        }
    }



    private void getPastList(String status,boolean isUpComing) {

        ArrayList<DoctorReqListDetails> healthWorkerRequestListDetailsArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(strResponse);
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
                String deptCode = c.getString("deptCode");
                String deptName = c.getString("deptName");
                String isDocUploaded = c.getString("isDocUploaded");
                String patientToken = c.getString("patientToken");
                String doctorToken = c.getString("doctorToken");


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

                patName = AppUtilityFunctions.capitalizeString(patName);
                if (requestStatus.equalsIgnoreCase("2")||requestStatus.equalsIgnoreCase("4")) {


                    Date appointmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(apptDate);
                    Date todaysDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(todaysDate));

                    Log.i(TAG, "compareTo"+appointmentDate.compareTo(date2));
                    if(appointmentDate.compareTo(date2)<=0) {
                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo,requestStatusCompleteDate,requestStatusCompleteMode));
                    }






//                    Date todaysDate = new Date();
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                    if (!apptDate.substring(0, 10).equalsIgnoreCase(formatter.format(todaysDate))) {

//                        healthWorkerRequestListDetailsArrayList.add(new DoctorReqListDetails(requestID, crNo, scrResponse, consName, deptUnitCode, deptUnitName, hospCode, requestStatus, "91" + patMobileNo, consMobileNo, patDocs, docMessage, cnsltntId, patName, patAge, patGender, rmrks, email, date, patWeight, patHeight, patMedication, patPastDiagnosis, patAllergies, deptCode, deptName, isDocUploaded, patientToken, doctorToken, appointmentNo, apptStartTime, apptEndTime, apptDate, hospitalName, isEpisodeExist, episodeCode, episodeVisitNo));

//                    }
                }
            }
            tvTotalRecordsFound.setText(healthWorkerRequestListDetailsArrayList.size() + " records found");
            adapter = new DoctorRequestListAdapter(DoctorRequestListActivity.this, healthWorkerRequestListDetailsArrayList,isUpComing);
            lv.setAdapter(adapter);

        } catch (Exception ex) {

        }


    }
    public void btnListPage(View view) {

        finish();

    }


}
